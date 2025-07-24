package me.vout.core.arcania.listener;

import me.vout.core.arcania.providers.ArcaniaProvider;
import me.vout.core.arcania.service.BlockTaggingService;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockUpdateListener implements Listener {
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) { // For TNT explosion
        if (event.isCancelled()) return;

        // event.blockList() contains the mutable list of blocks that WILL be removed.
        // Iterate over a copy to avoid ConcurrentModificationException if other plugins modify it.
        List<Block> blocksAffected = new ArrayList<>(event.blockList());
        BlockTaggingService blockTaggingService = ArcaniaProvider.getPlugin().getBlockTaggingService();
        for (Block block : blocksAffected) {
            // Only remove tag if it's currently marked as player-placed
            if (blockTaggingService.isPlayerPlaced(block)) {
                blockTaggingService.removePlayerPlacedTag(block);
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) { // For Creeper/Ghast/Fireball explosion
        if (event.isCancelled()) return;

        BlockTaggingService blockTaggingService = ArcaniaProvider.getPlugin().getBlockTaggingService();
        // event.blockList() contains the mutable list of blocks that WILL be removed.
        List<Block> blocksAffected = new ArrayList<>(event.blockList());

        for (Block block : blocksAffected) {
            if (blockTaggingService.isPlayerPlaced(block)) {
                blockTaggingService.removePlayerPlacedTag(block);
            }
        }
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (event.isCancelled()) return;

        BlockTaggingService blockTaggingService = ArcaniaProvider.getPlugin().getBlockTaggingService();
        BlockFace direction = event.getDirection();

        // Store locations to remove and locations to add
        // Use Map<Location, Location> to map old -> new for each moved block
        // Or just a List of Pairs (old_location, new_location)
       /* List<Map.Entry<Location, Location>> movesToTrack = new ArrayList<>();

        // Phase 1: Identify all affected blocks and their intended moves
        for (Block pushedBlock : event.getBlocks()) {
            if (blockTaggingService.isPlayerPlaced(pushedBlock)) {
                Location oldLoc = pushedBlock.getLocation();
                Location newLoc = pushedBlock.getRelative(direction).getLocation();
                movesToTrack.add(new AbstractMap.SimpleEntry<>(oldLoc, newLoc));
            }
        }

        // Phase 2: Apply the changes
        for (Map.Entry<Location, Location> move : movesToTrack) {
            Location oldLoc = move.getKey();
            Location newLoc = move.getValue();

            // 1. Remove tag from original location
            // Get the block at the old location
            Block oldBlock = oldLoc.getBlock();
            if (blockTaggingService.isPlayerPlaced(oldBlock)) { // Double-check it's still tagged
                blockTaggingService.removePlayerPlacedTag(oldBlock);
            }

            // 2. Tag at the new location
            // Get the block at the new location (which is the pushed block)
            Block newBlock = newLoc.getBlock(); // This Block object will be the *pushed* block's destination
            blockTaggingService.tagBlockAsPlayerPlaced(newBlock);
        }*/

        for (Block pushedBlock : event.getBlocks()) { // Blocks being pushed
            if (blockTaggingService.isPlayerPlaced(pushedBlock)) {

                blockTaggingService.removePlayerPlacedTag(pushedBlock); // Remove from old location
                Block newLocation = pushedBlock.getRelative(direction);
                ArcaniaProvider.getPlugin().getJavaPlugin().getLogger().info(String.format("Initial tag location removed %d,%d,%d. New location tagged: %d,%d,%d", pushedBlock.getX(), pushedBlock.getY(), pushedBlock.getZ(), newLocation.getX(), newLocation.getY(), newLocation.getZ()));
                blockTaggingService.tagBlockAsPlayerPlaced(newLocation); // Tag at new location
            }
        }

        //reversed version
        for (Block pushedBlock : event.getBlocks().reversed()) {
            if (blockTaggingService.isPlayerPlaced(pushedBlock)) {
                blockTaggingService.removePlayerPlacedTag(pushedBlock); // Remove from old location
                Block newLocation = pushedBlock.getRelative(direction);
                ArcaniaProvider.getPlugin().getJavaPlugin().getLogger().info(String.format("Initial tag location removed %d,%d,%d. New location tagged: %d,%d,%d", pushedBlock.getX(), pushedBlock.getY(), pushedBlock.getZ(), newLocation.getX(), newLocation.getY(), newLocation.getZ()));
                blockTaggingService.tagBlockAsPlayerPlaced(newLocation); // Tag at new location
            }
        }

    }

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (event.isCancelled()) return;

        BlockTaggingService blockTaggingService = ArcaniaProvider.getPlugin().getBlockTaggingService();
        BlockFace direction = event.getDirection(); // Direction it's pulling

        for (Block pulledBlock : event.getBlocks()) { // Blocks being pulled (if sticky piston)
            if (blockTaggingService.isPlayerPlaced(pulledBlock)) {
                blockTaggingService.removePlayerPlacedTag(pulledBlock); // Remove from old location
                Block newLocation = pulledBlock.getRelative(direction.getOppositeFace());
                blockTaggingService.tagBlockAsPlayerPlaced(newLocation); // Tag at new location
            }
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) { // Enderman, Ravager, etc.
        if (event.isCancelled()) return;

        BlockTaggingService blockTaggingService = ArcaniaProvider.getPlugin().getBlockTaggingService();
        Block changedBlock = event.getBlock();

        // If the block is being removed (e.g., Enderman picking it up)
        if (event.getTo() == Material.AIR ||
                event.getTo() == Material.CAVE_AIR ||
                event.getTo() == Material.VOID_AIR) {
            if (blockTaggingService.isPlayerPlaced(changedBlock)) {
                blockTaggingService.removePlayerPlacedTag(changedBlock);
            }
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) { // Wood burning away
        if (event.isCancelled()) return;

        BlockTaggingService blockTaggingService = ArcaniaProvider.getPlugin().getBlockTaggingService();
        if (blockTaggingService.isPlayerPlaced(event.getBlock())) {
            blockTaggingService.removePlayerPlacedTag(event.getBlock());
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) { // Ice melting, leaves decaying, snow layers melting, concrete powder
        if (event.isCancelled()) return;
        //todo doesnt seem to work for ice melting
        BlockTaggingService blockTaggingService = ArcaniaProvider.getPlugin().getBlockTaggingService();
        // Check if the block is fading to air or another state where the tag should be removed
        if (event.getNewState().getType() == Material.AIR || event.getNewState().getType() == Material.CAVE_AIR || event.getNewState().getType() == Material.VOID_AIR) {
            if (blockTaggingService.isPlayerPlaced(event.getBlock())) {
                blockTaggingService.removePlayerPlacedTag(event.getBlock());
            }
        }
    }
}
