package me.vout.core.arcania.enchants.pickaxe;

import me.vout.core.arcania.providers.ArcaniaProvider;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class QuarryHelper {
    public static void getBlocksToBreak(Player player, ItemStack item, BlockBreakEvent event, List<Block> blocksToAttemptToBreak) {
        // get the block to break
        Block block = event.getBlock();
        // check if the block is preferred tool
        if (!ArcaniaProvider.getPlugin().getValidToolStrategy().isValidTool(item, block)) return;

        BlockFace face = getBlockFace(player);
        if (face == null)
            face = player.getFacing();

        int[][] offsets = getBlocksOffset(face);

        for (int[] offset : offsets) {
            // get the relative block
            Block relative = block.getRelative(offset[0], offset[1], offset[2]);
            // skip the block if it's the same as the block being broken
            if (block.equals(relative)) continue;
            // skip the block if it's already in the list of blocks to break
            if (blocksToAttemptToBreak.contains(relative)) continue;

            // get the hardness of the block
            float hardness = relative.getType().getHardness();
            boolean isAir = relative.getType().isAir();
            if (hardness < 0 || isAir) continue;

            if (!ArcaniaProvider.getPlugin().getValidToolStrategy().isValidTool(item, relative)) continue;
            if (blocksToAttemptToBreak.contains(relative)) continue;

            blocksToAttemptToBreak.add(relative);
        }
    }

    /**
     * Determines which block face the player is mining based on their target blocks.
     * This is used to orient the 3x3 mining pattern correctly.
     *
     * @param player The player mining the block
     * @return The BlockFace being mined, or null if cannot be determined
     */
    private static BlockFace getBlockFace(Player player) {
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 5);
        if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()) return null;
        Block targetBlock = lastTwoTargetBlocks.get(1);
        Block adjacentBlock = lastTwoTargetBlocks.get(0);
        return targetBlock.getFace(adjacentBlock);
    }

    private static int[][] getBlocksOffset(BlockFace face) {
        if (face == BlockFace.UP || face == BlockFace.DOWN) {
            // Horizontal plane (X, Z)
            return new int[][] {
                    {-1, 0, -1}, {0, 0, -1}, {1, 0, -1},
                    {-1, 0,  0}, {0, 0,  0}, {1, 0,  0},
                    {-1, 0,  1}, {0, 0,  1}, {1, 0,  1}
            };
        } else if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
            // Vertical plane (X, Y)
            return new int[][] {
                    {-1, -1, 0}, {0, -1, 0}, {1, -1, 0},
                    {-1,  0, 0}, {0,  0, 0}, {1,  0, 0},
                    {-1,  1, 0}, {0,  1, 0}, {1,  1, 0}
            };
        } else {
            // EAST or WEST: Vertical plane (Y, Z)
            return new int[][] {
                    {0, -1, -1}, {0, -1, 0}, {0, -1, 1},
                    {0,  0, -1}, {0,  0, 0}, {0,  0, 1},
                    {0,  1, -1}, {0,  1, 0}, {0,  1, 1}
            };
        }
    }

}
