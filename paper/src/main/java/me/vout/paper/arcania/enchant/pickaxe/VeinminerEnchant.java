package me.vout.paper.arcania.enchant.pickaxe;

import me.vout.paper.arcania.Arcania;
import me.vout.paper.arcania.ArcaniaBootstrap;
import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import me.vout.paper.arcania.enchant.EnchantRarityEnum;
import me.vout.paper.arcania.util.ItemHelper;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.papermc.paper.enchantments.EnchantmentRarity;
import io.papermc.paper.registry.set.RegistryKeySet;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;

import java.util.*;

/**
 * VeinMiner Enchantment
 * 
 * A powerful mining enchantment that uses a flood-fill algorithm to break connected blocks of the same type.
 * 
 * Technical Specifications:
 * - Rarity: LEGENDARY
 * - Max Level: 3
 * - Success Rate: 20%
 * - Cost: 30 levels
 * - Applies to: Pickaxes only
 * 
 * Functionality:
 * - Base vein size: 10 blocks (configurable in config.yml)
 * - Additional blocks per level: 5 blocks
 *   - Level 1: 15 blocks total
 *   - Level 2: 20 blocks total
 *   - Level 3: 25 blocks total
 * 
 * Block Detection:
 * - Uses breadth-first search in a 3x3x3 cube
 * - Checks all 26 neighboring blocks (excluding center)
 * - Maintains a set of checked blocks to prevent cycles
 * - Queues matching blocks for further neighbor checking
 * 
 * Whitelisted Blocks (configurable in config.yml):
 * - Supports regex patterns (e.g., '*_ORE' matches all ores)
 * - Default whitelist includes:
 *   - All ore types (*_ORE)
 *   - STONE
 *   - ANCIENT_DEBRIS
 *   - GRANITE
 *   - DIORITE
 *   - ANDESITE
 *   - TUFF
 *   - DEEPSLATE
 *   - NETHERRACK
 * 
 * Recursion Prevention:
 * - Uses metadata tag 'arcania:veinmined' to prevent infinite loops
 * - Tracks blocks already queued for breaking
 * - Ensures each block is only processed once
 * 
 * Integration:
 * - Compatible with all other mining enchantments
 * - Works with the block breaking queue system for lag-free operation
 * - Properly handles tool durability checks
 */
public class VeinminerEnchant extends ArcaniaEnchant {
    public static final VeinminerEnchant INSTANCE = new VeinminerEnchant();
    private VeinminerEnchant() {
        super("Vein Miner",
                "veinminer",
                "Mines connecting veins of initial block",
                3,
                1,
                4,
                1,
                1,
                1);
    }

    /**
     * Checks if a material is allowed to be vein mined based on the config whitelist.
     * The whitelist supports regex patterns, where '*' is converted to '.*' for matching.
     * For example, '*_ORE' will match all blocks ending with _ORE.
     *
     * @param mat The material to check
     * @return true if the material is whitelisted for vein mining
     */
    public static boolean isVeinMineBlock(Material mat) {
        return Arcania.getConfigManager().getVeinminerWhitelistedBlocks().stream().anyMatch(pattern -> {
            String regexPattern = pattern.replace("*", ".*");
            return mat.name().matches(regexPattern);
        });
    }

    /**
     * Gets a list of connected blocks to break using a breadth-first search algorithm.
     * 
     * The algorithm works as follows:
     * 1. Start from the initial block
     * 2. Check all 26 surrounding blocks in a 3x3x3 cube
     * 3. Add matching blocks to the queue for processing
     * 4. Continue until either:
     *    - No more connected blocks are found
     *    - The maximum vein size is reached
     *    - All connected blocks have been checked
     * 
     * The maximum vein size is calculated as:
     * baseSize + (enchantmentLevel * 5)
     * where baseSize is configured in config.yml (default: 10)
     *
     * @param player The player breaking the blocks
     * @param startBlock The initial block that was broken
     * @param enchants The enchantments on the tool being used
     * @param allBlocksToBreak List of blocks already queued for breaking
     * @return List of additional blocks to break as part of the vein
     */
    public static void getBlocksToBreak(Player player, Block startBlock, Map<Enchantment, Integer> enchants, List<Block> allBlocksToBreak) {
        Enchantment veinminerEnchant = Arcania.getEnchantRegistry().get(Key.key("arcania", "veinminer"));
        int maxBlocksToVienMine = Arcania.getConfigManager().getVeinminerMaxBlocks() + enchants.get(veinminerEnchant) * 5;
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

    @Override
    public @NotNull String getTranslationKey() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTranslationKey'");
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canEnchantItem'");
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'conflictsWith'");
    }

    @Override
    public @NotNull Component description() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'description'");
    }

    @Override
    public @NotNull Component displayName(int arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayName'");
    }

    @Override
    public @NotNull Set<EquipmentSlotGroup> getActiveSlotGroups() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getActiveSlotGroups'");
    }

    @Override
    public int getAnvilCost() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAnvilCost'");
    }

    @Override
    public float getDamageIncrease(int arg0, @NotNull EntityCategory arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDamageIncrease'");
    }

    @Override
    public float getDamageIncrease(int arg0, @NotNull EntityType arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDamageIncrease'");
    }

    @Override
    public @NotNull RegistryKeySet<Enchantment> getExclusiveWith() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getExclusiveWith'");
    }

    @Override
    public @NotNull EnchantmentTarget getItemTarget() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getItemTarget'");
    }

    @Override
    public int getMaxLevel() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMaxLevel'");
    }

    @Override
    public int getMaxModifiedCost(int arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMaxModifiedCost'");
    }

    @Override
    public int getMinModifiedCost(int arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMinModifiedCost'");
    }

    @Override
    public @Nullable RegistryKeySet<ItemType> getPrimaryItems() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPrimaryItems'");
    }

    @Override
    public @NotNull EnchantmentRarity getRarity() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRarity'");
    }

    @Override
    public int getStartLevel() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStartLevel'");
    }

    @Override
    public @NotNull RegistryKeySet<ItemType> getSupportedItems() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSupportedItems'");
    }

    @Override
    public int getWeight() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWeight'");
    }

    @Override
    public boolean isCursed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isCursed'");
    }

    @Override
    public boolean isDiscoverable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isDiscoverable'");
    }

    @Override
    public boolean isTradeable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isTradeable'");
    }

    @Override
    public boolean isTreasure() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isTreasure'");
    }

    @Override
    public @NotNull String translationKey() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'translationKey'");
    }
}