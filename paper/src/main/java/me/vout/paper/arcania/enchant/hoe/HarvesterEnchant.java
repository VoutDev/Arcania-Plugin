package me.vout.paper.arcania.enchant.hoe;

import me.vout.paper.arcania.Arcania;
import me.vout.paper.arcania.ArcaniaBootstrap;
import me.vout.paper.arcania.enchant.ArcaniaEnchant;
// import me.vout.paper.arcania.enchant.EnchantRarityEnum;
import me.vout.paper.arcania.enchant.tool.MagnetEnchant;
import me.vout.paper.arcania.util.InventoryHelper;
import me.vout.paper.arcania.util.ItemHelper;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class HarvesterEnchant extends ArcaniaEnchant {
    public  static  final HarvesterEnchant INSTANCE = new HarvesterEnchant();
    private HarvesterEnchant() {
        super("Harvester",
                "harvester",
                "Right click to collect and replant crop",
                1,
                1,
                1,
                1,
                1,
                1
        );
    }

    public static void onProc(Player player, Block crop, ItemStack hoe) {
        if (crop.getBlockData() instanceof Ageable ageable) {
            if (ageable.getAge() != ageable.getMaximumAge()) return;
            Collection<ItemStack> drops = crop.getDrops(hoe);

            Map<Enchantment, Integer> enchants = hoe.getEnchantments();
            Enchantment magnetEnchant = Arcania.getEnchantRegistry().get(MagnetEnchant.INSTANCE.getKey());
            boolean hasMagnet = enchants.containsKey(magnetEnchant);
            if (hasMagnet)
                InventoryHelper.giveOrDrop(player, drops.toArray(new ItemStack[0]));

            else {
                World world = crop.getWorld();
                for (ItemStack drop : drops) {
                    world.dropItemNaturally(crop.getLocation().add(0.5, 0.5, 0.5), drop);
                }
            }
            ageable.setAge(0);
            crop.setBlockData((BlockData) ageable);
        }
    }

    public static boolean isCrop(Material mat) {
        return mat == Material.WHEAT
                || mat == Material.CARROTS
                || mat == Material.POTATOES
                || mat == Material.BEETROOTS
                || mat == Material.NETHER_WART;
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
