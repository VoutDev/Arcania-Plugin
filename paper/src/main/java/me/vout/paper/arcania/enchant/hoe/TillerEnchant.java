package me.vout.paper.arcania.enchant.hoe;

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
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.ItemTypeKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import me.vout.paper.arcania.util.ToolHelper;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

public class TillerEnchant extends ArcaniaEnchant {

    public static final TillerEnchant INSTANCE = new TillerEnchant();
    private TillerEnchant() {
        super("Tiller",
                "tiller",
                "Tills a 3x3 around initial block",
                1,
                1,
                5,
                10,
                15,
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
        return "enchantment." + NAMESPACE + "." + key;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack arg0) {
        return true;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment arg0) {
        return false;
    }

    @Override
    public @NotNull Component description() {
        return Component.text(description);
    }

    @Override
    public @NotNull Component displayName(int arg0) {
        return Component.text(name);
    }

    @Override
    public @NotNull Set<EquipmentSlotGroup> getActiveSlotGroups() {
        return Set.of(EquipmentSlotGroup.MAINHAND);
    }

    @Override
    public int getAnvilCost() {
        return anvilCost;
    }

    @Override
    public float getDamageIncrease(int arg0, @NotNull EntityCategory arg1) {
        return 0;
    }

    @Override
    public float getDamageIncrease(int arg0, @NotNull EntityType arg1) {
        return 0;
    }
    
    @Override
    public @NotNull RegistryKeySet<Enchantment> getExclusiveWith() {
        return RegistrySet.keySet(EnchantmentKeys.create(Key.key(NAMESPACE, key)).registryKey());
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getMaxModifiedCost(int arg0) {
        return maxModifiedCost;
    }

    @Override
    public int getMinModifiedCost(int arg0) {
        return minModifiedCost;
    }

    @Override
    public @Nullable RegistryKeySet<ItemType> getPrimaryItems() {
        return RegistrySet.keySet(RegistryKey.ITEM, ItemTypeKeys.ENCHANTED_BOOK, ItemTypeKeys.WOODEN_HOE, ItemTypeKeys.STONE_HOE, ItemTypeKeys.IRON_HOE, ItemTypeKeys.GOLDEN_HOE, ItemTypeKeys.DIAMOND_HOE, ItemTypeKeys.NETHERITE_HOE);
    }

    @Override
    public @NotNull EnchantmentRarity getRarity() {
        return EnchantmentRarity.RARE;
    }

    @Override
    public int getStartLevel() {
        return startLevel;
    }

    @Override
    public @NotNull RegistryKeySet<ItemType> getSupportedItems() {
        return RegistrySet.keySet(RegistryKey.ITEM, ItemTypeKeys.ENCHANTED_BOOK, ItemTypeKeys.WOODEN_HOE, ItemTypeKeys.STONE_HOE, ItemTypeKeys.IRON_HOE, ItemTypeKeys.GOLDEN_HOE, ItemTypeKeys.DIAMOND_HOE, ItemTypeKeys.NETHERITE_HOE);
    }

    @Override
    public int getWeight() {
        return weight;
    }
    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return true;
    }
    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }
    @Override
    public @NotNull String translationKey() {
        return "enchantment." + NAMESPACE + "." + key;
    }

    @Override
    public @NotNull EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }
}