package me.vout.paper.arcania.enchant.hoe;

import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import me.vout.paper.arcania.util.ToolHelper;
import net.kyori.adventure.text.Component;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.papermc.paper.enchantments.EnchantmentRarity;
import io.papermc.paper.registry.set.RegistryKeySet;

public class TillerEnchant extends ArcaniaEnchant {

    public static final TillerEnchant INSTANCE = new TillerEnchant();
    private TillerEnchant() {
        super("Tiller",
                "tiller",
                "Tills a 3x3 around initial block",
                1,
                1,
                1,
                1,
                1,
                1
        );
    }

    public static void onProc(Player player, ItemStack tool, Block clicked, PlayerInteractEvent event) {
        // 3x3 area around the clicked block
        outer:
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                Block target = clicked.getRelative(dx, 0, dz);
                if (isTillable(target.getType()) && canTill(target)) {
                    tillBlock(target);
                    if (!ToolHelper.damageTool(player, tool, 1)) break outer;
                }
            }
        }
    }

    public static boolean isTillable(Material mat) {
        return mat == Material.DIRT ||
                mat == Material.ROOTED_DIRT ||
                mat ==Material.GRASS_BLOCK ||
                mat == Material.COARSE_DIRT ||
                mat == Material.DIRT_PATH;
    }

    private static boolean canTill(Block block) {
        return block.getRelative(BlockFace.UP).getType().isAir();
    }

    private static void tillBlock(Block block) {
        Material newType = (block.getType() == Material.COARSE_DIRT) ? Material.DIRT : Material.FARMLAND;
        block.setType(newType, true);
        // Optionally play sound/particle
        //block.getWorld().playSound(block.getLocation(), Sound.ITEM_HOE_TILL, 1.0f, 1.0f);
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