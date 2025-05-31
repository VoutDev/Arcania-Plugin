package me.vout.paper.arcania.util;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.vout.paper.arcania.Arcania;

public class BlockBreakQueue {
    private static final ConcurrentHashMap<Player, Queue<BlockBreakData>> playerQueues = new ConcurrentHashMap<>();
    private static final HashMap<Player, Float> cummulativePlayerExperience = new HashMap<>();
    private static final int BATCH_SIZE = 25; // Number of blocks to process per tick
    private static final long PROCESS_INTERVAL = 1L; // Process every tick
    private static boolean isProcessing = false;

    public static class BlockBreakData {
        private final ItemStack tool;
        private final Block block;
        private final List<ItemStack> drops;
        private final float experience;
        private final Location dropLocation;
        private final boolean hasMagnet;

        public BlockBreakData(ItemStack tool, Block block, List<ItemStack> drops, float experience, Location dropLocation, boolean hasMagnet) {
            this.tool = tool;
            this.block = block;
            this.drops = drops;
            this.experience = experience;
            this.dropLocation = dropLocation;
            this.hasMagnet = hasMagnet;
        }
    }

    public static void queueBlockBreak(Player player, BlockBreakData breakData) {
        playerQueues.computeIfAbsent(player, k -> new ConcurrentLinkedQueue<>()).add(breakData);
        startProcessingIfNeeded();
    }

    private static void startProcessingIfNeeded() {
        if (isProcessing) return;
        isProcessing = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (playerQueues.isEmpty()) {
                    isProcessing = false;
                    this.cancel();
                    return;
                }

                // Process a batch for each player
                for (Player player : playerQueues.keySet()) {
                    Queue<BlockBreakData> queue = playerQueues.get(player);
                    int processed = 0;

                    while (!queue.isEmpty() && processed < BATCH_SIZE) {
                        BlockBreakData data = queue.poll();
                        if (data == null) continue;

                        // Process the block break
                        processBlockBreak(player, data);
                        processed++;
                    }

                    // Remove the player's queue if it's empty
                    if (queue.isEmpty()) {
                        playerQueues.remove(player);
                    }
                }
            }
        }.runTaskTimer(Arcania.getInstance(), 0L, PROCESS_INTERVAL);
    }

    private static void processBlockBreak(Player player, BlockBreakData data) {
        // Set block to air
        data.block.setType(Material.AIR, true);

        // break all the blocks
        if (data.block.hasMetadata("arcania:veinmined")) {
            data.block.removeMetadata("arcania:veinmined", Arcania.getInstance());
        }
        
        if (data.block.hasMetadata("arcania:quarried")) {
            data.block.removeMetadata("arcania:quarried", Arcania.getInstance());
        }

        // Handle drops
        if (data.hasMagnet) {
            // Give items directly to player
            data.drops.forEach(itemStack -> InventoryHelper.giveOrDrop(player, itemStack));
        } else {
            // Drop items naturally
            data.drops.forEach(itemStack -> 
                data.block.getWorld().dropItemNaturally(data.dropLocation, itemStack)
            );
        }

        // Spawn experience
        if (data.experience > 0) {
            cummulativePlayerExperience.put(player, cummulativePlayerExperience.getOrDefault(player, 0f) + data.experience);
            float cummulativeExperience = cummulativePlayerExperience.get(player);
            // Arcania.getInstance().getLogger().log(Level.INFO, "cummulativeExperience: " + cummulativeExperience);
            if (cummulativeExperience >= 1) {
                Location expLocation = data.dropLocation.clone().add(0.5, 0.5, 0.5);
                int experienceToGive = (int) Math.floor(cummulativeExperience); 
                data.block.getWorld().spawn(expLocation, ExperienceOrb.class)
                    .setExperience(experienceToGive);
                cummulativePlayerExperience.put(player, cummulativeExperience - experienceToGive);
            }
            
        }

        ToolHelper.damageTool(player, data.tool, 1);
    }
} 