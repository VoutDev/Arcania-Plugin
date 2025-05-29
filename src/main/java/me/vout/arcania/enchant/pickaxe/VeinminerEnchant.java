package me.vout.arcania.enchant.pickaxe;

import me.vout.arcania.Arcania;
import me.vout.arcania.enchant.ArcaniaEnchant;
import me.vout.arcania.enchant.EnchantRarityEnum;
import me.vout.arcania.util.ItemHelper;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import java.util.*;

public class VeinminerEnchant extends ArcaniaEnchant {
    public static final VeinminerEnchant INSTANCE = new VeinminerEnchant();
    private VeinminerEnchant() {
        super("Vein Miner",
                "Mines connecting veins of initial block",
                EnchantRarityEnum.LEGENDARY,
                3,
                0.2,
                4,
                ItemHelper::isPickaxe);
    }

    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }

    public static boolean isVeinMineBlock(Material mat) {
        return Arcania.getConfigManager().getVeinminerWhitelistedBlocks().stream().anyMatch(pattern -> {
            String regexPattern = pattern.replace("*", ".*");
            return mat.name().matches(regexPattern);
        });
    }

    public static List<Block> getBlocksToBreak(Player player, Block startBlock, Map<ArcaniaEnchant, Integer> enchants, List<Block> blocksToAttemptToBreak) {
        int maxBlocksToVienMine = Arcania.getConfigManager().getVeinminerMaxBlocks() + enchants.get(VeinminerEnchant.INSTANCE) * 5;
        Material targetType = startBlock.getType();
        Set<Block> checked = new HashSet<>();
        Queue<Block> toCheck = new LinkedList<>();
        List<Block> blocksToBreak = new ArrayList<>();

        toCheck.add(startBlock);

        while (!toCheck.isEmpty() && blocksToBreak.size() < maxBlocksToVienMine) {
            Block block = toCheck.poll();
            if (checked.contains(block)) continue;
            checked.add(block);

            if (block.getType() != targetType) continue;
            // make sure we don't add the block if it's already in the list of blocks to break or attempt to break
            if (!blocksToBreak.contains(block) && !blocksToAttemptToBreak.contains(block)) {
                blocksToBreak.add(block);
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

        return blocksToBreak;
    }
}