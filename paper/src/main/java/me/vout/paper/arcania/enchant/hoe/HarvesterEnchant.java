package me.vout.paper.arcania.enchant.hoe;

import me.vout.paper.arcania.Arcania;
import me.vout.paper.arcania.ArcaniaBootstrap;
import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import me.vout.paper.arcania.enchant.EnchantRarityEnum;
import me.vout.paper.arcania.enchant.tool.MagnetEnchant;
import me.vout.paper.arcania.util.InventoryHelper;
import me.vout.paper.arcania.util.ItemHelper;
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
import io.papermc.paper.registry.set.RegistryKeySet;

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
