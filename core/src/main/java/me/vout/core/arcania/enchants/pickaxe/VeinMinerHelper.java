package me.vout.core.arcania.enchants.pickaxe;

import me.vout.core.arcania.enums.ArcaniaEnchantType;
import me.vout.core.arcania.providers.ArcaniaProvider;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class VeinMinerHelper {
    public static boolean isVeinMineBlock(Material mat) {
        return ArcaniaProvider.getPlugin().getConfigManager().getVeinminerWhitelistedBlocks().stream().anyMatch(pattern -> {
            String regexPattern = pattern.replace("*", ".*");
            return mat.name().matches(regexPattern);
        });
    }

    public static void getBlocksToBreak(Player player, Block startBlock, ItemStack tool, Map<NamespacedKey, Integer> activeEnchants, List<Block> allBlocksToBreak) {
        //Block startBlock = event.getBlock();
        if (!ArcaniaProvider.getPlugin().getValidToolStrategy().isValidTool(tool, startBlock)) {
            return;
        }
        int maxBlocksToVienMine = ArcaniaProvider.getPlugin().getConfigManager().getVeinminerMaxBlocks()
                + ArcaniaProvider.getPlugin().getEnchantStrategy().getCustomEnchantLevel(activeEnchants, ArcaniaEnchantType.VEIN_MINER.getKey()) * 5;
        Material targetType = startBlock.getType();
        Set<Block> checked = new HashSet<>();
        Queue<Block> toCheck = new LinkedList<>();

        toCheck.add(startBlock);
        // add 1 to account for the initial block
        while (!toCheck.isEmpty() && (allBlocksToBreak.size()+1) < maxBlocksToVienMine) {
            Block block = toCheck.poll();
            if (checked.contains(block)) continue;
            checked.add(block);

            if (block.getType() != targetType) continue;
            // make sure we don't add the block if it's already in the list of blocks to break or attempt to break
            if (!allBlocksToBreak.contains(block) && !block.equals(startBlock)) {
                allBlocksToBreak.add(block);
            }

            // Check all 26 neighbors in a 3x3x3 cube
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) continue; // Skip self
                        Block neighbor = block.getRelative(dx, dy, dz);
                        if (!checked.contains(neighbor) && neighbor.getType() == targetType) {
                            toCheck.add(neighbor);
                        }
                    }
                }
            }
        }
    }
}
