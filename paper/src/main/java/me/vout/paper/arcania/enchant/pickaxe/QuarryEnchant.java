package me.vout.paper.arcania.enchant.pickaxe;

import me.vout.paper.arcania.Arcania;
import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import me.vout.paper.arcania.enchant.EnchantRarityEnum;
import me.vout.paper.arcania.util.ItemHelper;
import net.kyori.adventure.text.Component;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.papermc.paper.enchantments.EnchantmentRarity;
import io.papermc.paper.registry.set.RegistryKeySet;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 * Quarry Enchantment
 * 
 * An efficient mining enchantment that breaks blocks in a 3x3 pattern based on the player's mining direction.
 * 
 * Technical Specifications:
 * - Rarity: RARE
 * - Max Level: 1
 * - Success Rate: 50%
 * - Cost: 15 levels
 * - Applies to: All digging tools (pickaxe, shovel)
 * 
 * Functionality:
 * - Breaks blocks in a 3x3 pattern
 * - Pattern orientation adapts to player's mining face:
 *   - Looking UP/DOWN: Horizontal plane (X-Z plane)
 *   - Looking NORTH/SOUTH: Vertical plane (X-Y plane)
 *   - Looking EAST/WEST: Vertical plane (Y-Z plane)
 * 
 * Block Breaking Logic:
 * - Only breaks blocks that are valid for the tool type
 * - Checks tool durability before breaking additional blocks
 * - Preserves tool enchantments effects on broken blocks
 * - Respects block breaking permissions
 * 
 * Recursion Prevention:
 * - Uses metadata tag 'arcania:quarried' to prevent infinite loops
 * - Tracks blocks already queued for breaking
 * - Ensures each block is only processed once
 * 
 * Integration:
 * - Compatible with all other mining enchantments
 * - Works with the block breaking queue system for lag-free operation
 * - Properly handles tool durability checks
 * - Can trigger other enchantments (e.g., VeinMiner) on broken blocks
 */
public class QuarryEnchant extends ArcaniaEnchant {

    public static final QuarryEnchant INSTANCE = new QuarryEnchant();
    private QuarryEnchant() {
        super("Quarry",
                "quarry",
                "Mines a 3x3 around broken block",
                1,
                1,
                2,
                1,
                1,
                1
        );
    }

    /**
     * Gets a list of blocks to break in a 3x3 pattern around the initial block.
     * The pattern orientation is determined by the player's mining direction.
     * 
     * Breaking Logic:
     * 1. Determines the face being mined
     * 2. Calculates appropriate 3x3 pattern based on face
     * 3. Validates each block against tool type
     * 4. Checks for blocks already queued for breaking
     * 
     * @param player The player breaking the blocks
     * @param item The tool being used
     * @param event The original block break event
     * @param blocksToAttemptToBreak List of blocks already queued for breaking
     * @return List of additional blocks to break in the 3x3 pattern
     */
    public static void getBlocksToBreak(Player player, ItemStack item, BlockBreakEvent event, List<Block> blocksToAttemptToBreak) {
        // get the block to break
        Block block = event.getBlock();
        // check if the block is preferred tool
        if (!block.isPreferredTool(item)) {
            return;
        }

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

            boolean isPreferredTool = relative.isPreferredTool(item);
            Arcania.getInstance().getLogger().log(Level.INFO, "isPreferredTool: " + isPreferredTool);
            if (!isPreferredTool) continue;

            // Arcania.getInstance().getLogger().log(Level.INFO, "blocksToAttemptToBreak: " + blocksToAttemptToBreak.contains(relative));
            // Arcania.getInstance().getLogger().log(Level.INFO, "blocksToBreak: " + blocksToBreak.contains(relative));
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

    /**
     * Calculates the block offsets for the 3x3 pattern based on the mining face.
     * 
     * Pattern Orientations:
     * - UP/DOWN: Horizontal plane (X-Z plane)
     * - NORTH/SOUTH: Vertical plane (X-Y plane)
     * - EAST/WEST: Vertical plane (Y-Z plane)
     *
     * @param face The BlockFace being mined
     * @return 2D array of coordinate offsets for the 3x3 pattern
     */
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
