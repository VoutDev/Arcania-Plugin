package me.vout.core.arcania.service;

import com.carrotsearch.hppc.LongHashSet;
import com.carrotsearch.hppc.cursors.LongCursor;
import me.vout.core.arcania.providers.ArcaniaProvider;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class BlockTaggingService {
    private final JavaPlugin plugin;
    private final File dataFile;
    private FileConfiguration dataConfig; // In-memory representation of the YAML file
    private final Map<UUID, LongHashSet> playerPlacedBlocksByWorld;
    public BlockTaggingService() {
        this.plugin = ArcaniaProvider.getPlugin().getJavaPlugin();
        this.dataFile = new File(plugin.getDataFolder(), "player_placed_blocks.yml");
        this.playerPlacedBlocksByWorld = new HashMap<>();
    }

    public void initialize() {
        if (!dataFile.exists()) {
            try {
                if (dataFile.getParentFile().mkdirs()) {
                    if (!dataFile.createNewFile()) {
                        plugin.getLogger().log(Level.SEVERE, "Could not create player_placed_blocks.yml");
                    }

                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not create player_placed_blocks.yml", e);
            }
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        plugin.getLogger().info("Block tag service initialized. Data will be loaded per-world on demand.");

        // Check if "blocks" exists AND is a list
    }


    public void shutdown() {

        dataConfig.set("worlds", null); // Clear old data

        // --- SAVING LOGIC ---
        // Iterate through each world's LongSet
        for (Map.Entry<UUID, LongHashSet> entry : playerPlacedBlocksByWorld.entrySet()) {
            UUID worldUUID = entry.getKey();
            LongHashSet worldLongSet = entry.getValue();

            // Convert the LongSet to a List<String>
            List<String> keysToSave = new ArrayList<>(worldLongSet.size());
            for (LongCursor cursor : worldLongSet) {
                keysToSave.add(String.valueOf(cursor.value));
            }

            // Set the list of strings under the world's UUID path
            dataConfig.set("worlds." + worldUUID.toString(), keysToSave);
        }

        try {
            dataConfig.save(dataFile);
            plugin.getLogger().info("Saved player placed block tags for " + playerPlacedBlocksByWorld.size() + " worlds.");
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save player placed block data!", e);
        }
    }

    public void loadWorldData(UUID worldUUID) {
        // Only load if it's not already in memory
        if (!playerPlacedBlocksByWorld.containsKey(worldUUID)) {
            String pathToList = "worlds." + worldUUID.toString();

            List<String> loadedKeysAsStrings = dataConfig.getStringList(pathToList); // <-- Correctly gets list for THIS world

            LongHashSet worldLongSet = new LongHashSet(loadedKeysAsStrings.size());

            for (String keyString : loadedKeysAsStrings) {
                try {
                    worldLongSet.add(Long.parseLong(keyString));
                } catch (NumberFormatException e) {
                    plugin.getLogger().warning("Skipping invalid block tag key for world " + worldUUID + ": '" + keyString + "' - " + e.getMessage());
                }
            }
            playerPlacedBlocksByWorld.put(worldUUID, worldLongSet); // Store in our in-memory map
            plugin.getLogger().info("Loaded " + worldLongSet.size() + " block tags for world " + worldUUID + ".");
        }
    }

    // --- Static helper for generating the long key ---
    // Make sure this lives in a central utility class in your core module if needed elsewhere,
    // or keep it private here. This should match the logic from the forum post.
    private static long getLongKey(Block block) {
        long combined = 0L;
        // World is NOT encoded in this simple long.
        // You would need a Map<UUID, LongSet> if you want to track per-world.
        // For simplicity, this example assumes single world or you handle world separation elsewhere.
        // If your server has multiple worlds, you will need a Map<UUID, LongSet>
        // as the top level, or encode World UUID (or hash) into the long if it fits.
        // For now, let's assume it's per-world. If not, this LongSet handles one world.

        // Assuming Y coordinates are within 0-255 (8 bits)
        // X and Z coords are 28 bits each (enough for 134 million blocks from origin)
        combined |= ((long) block.getY()) << 56;
        combined |= ((long) block.getX() & 0xFFFFFFFL) << 28; // Use 0xFFFFFFFL for 28-bit mask
        combined |= ((long) block.getZ() & 0xFFFFFFFL);       // No shift needed for Z
        return combined;
    }

    private static long getLongKey(Location loc) {
        return getLongKey(loc.getBlock()); // Re-use the Block version for Location
    }

    private LongHashSet getOrCreateLongSetForWorld(UUID worldUUID) {
        // ComputeIfAbsent is great here: gets the set if it exists, otherwise creates and puts a new one
        return playerPlacedBlocksByWorld.computeIfAbsent(worldUUID, k -> new LongHashSet());
    }

    private void addInternal(Location loc) {
        getOrCreateLongSetForWorld(Objects.requireNonNull(loc.getWorld()).getUID()).add(getLongKey(loc));
    }

    private boolean isInternal(Location loc) {
        LongHashSet worldLongSet = playerPlacedBlocksByWorld.get(Objects.requireNonNull(loc.getWorld()).getUID());
        return worldLongSet != null && worldLongSet.contains(getLongKey(loc));
    }

    private void removeInternal(Location loc) {
        LongHashSet worldLongSet = playerPlacedBlocksByWorld.get(Objects.requireNonNull(loc.getWorld()).getUID());
        if (worldLongSet != null) {
            worldLongSet.remove(getLongKey(loc));
            // If a world's set becomes empty, remove the entry
             if (worldLongSet.isEmpty()) {
                 playerPlacedBlocksByWorld.remove(loc.getWorld().getUID());
             }
        }
    }

    public void tagBlockAsPlayerPlaced(Block block) { addInternal(block.getLocation()); }
    public boolean isPlayerPlaced(Block block) { return isInternal(block.getLocation()); }
    public void removePlayerPlacedTag(Block block) { removeInternal(block.getLocation()); }
}
