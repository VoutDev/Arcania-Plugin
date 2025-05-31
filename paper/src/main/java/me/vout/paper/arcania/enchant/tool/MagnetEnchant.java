package me.vout.paper.arcania.enchant.tool;

import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import me.vout.paper.arcania.util.InventoryHelper;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

import java.util.Set;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
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

public class MagnetEnchant extends ArcaniaEnchant {
    public static  final MagnetEnchant INSTANCE = new MagnetEnchant();
    private MagnetEnchant() {
        super("Magnet",
            "magnet",
            "Attempts to put item drops directly in inventory",
            1,
            1,
            5,
            1,
            1,
            1
        );
    }

    public static void onProc(Player player, EntityDeathEvent event, float xp) {
        event.setDroppedExp(0);

        InventoryHelper.giveOrDrop(player, event.getDrops().toArray(new ItemStack[0]));
        event.getDrops().clear();
        player.getWorld().spawn(player.getLocation(), ExperienceOrb.class).setExperience((int)xp);
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
        return RegistrySet.keySet(RegistryKey.ITEM, 
            ItemTypeKeys.ENCHANTED_BOOK, 
            
            ItemTypeKeys.WOODEN_PICKAXE,
            ItemTypeKeys.STONE_PICKAXE, 
            ItemTypeKeys.IRON_PICKAXE, 
            ItemTypeKeys.GOLDEN_PICKAXE, 
            ItemTypeKeys.DIAMOND_PICKAXE, 
            ItemTypeKeys.NETHERITE_PICKAXE,

            ItemTypeKeys.WOODEN_AXE,
            ItemTypeKeys.STONE_AXE,
            ItemTypeKeys.IRON_AXE,
            ItemTypeKeys.GOLDEN_AXE,
            ItemTypeKeys.DIAMOND_AXE,
            ItemTypeKeys.NETHERITE_AXE,

            ItemTypeKeys.WOODEN_SHOVEL,
            ItemTypeKeys.STONE_SHOVEL,
            ItemTypeKeys.IRON_SHOVEL,
            ItemTypeKeys.GOLDEN_SHOVEL,
            ItemTypeKeys.DIAMOND_SHOVEL,
            ItemTypeKeys.NETHERITE_SHOVEL,

            ItemTypeKeys.WOODEN_HOE,
            ItemTypeKeys.STONE_HOE,
            ItemTypeKeys.IRON_HOE,
            ItemTypeKeys.GOLDEN_HOE,
            ItemTypeKeys.DIAMOND_HOE,
            ItemTypeKeys.NETHERITE_HOE,

            ItemTypeKeys.WOODEN_SWORD,
            ItemTypeKeys.STONE_SWORD,
            ItemTypeKeys.IRON_SWORD,
            ItemTypeKeys.GOLDEN_SWORD,
            ItemTypeKeys.DIAMOND_SWORD,
            ItemTypeKeys.NETHERITE_SWORD
        );    
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
        return RegistrySet.keySet(RegistryKey.ITEM, 
            ItemTypeKeys.ENCHANTED_BOOK, 

            ItemTypeKeys.WOODEN_PICKAXE,
            ItemTypeKeys.STONE_PICKAXE, 
            ItemTypeKeys.IRON_PICKAXE, 
            ItemTypeKeys.GOLDEN_PICKAXE, 
            ItemTypeKeys.DIAMOND_PICKAXE, 
            ItemTypeKeys.NETHERITE_PICKAXE,

            ItemTypeKeys.WOODEN_AXE,
            ItemTypeKeys.STONE_AXE,
            ItemTypeKeys.IRON_AXE,
            ItemTypeKeys.GOLDEN_AXE,
            ItemTypeKeys.DIAMOND_AXE,
            ItemTypeKeys.NETHERITE_AXE,

            ItemTypeKeys.WOODEN_SHOVEL,
            ItemTypeKeys.STONE_SHOVEL,
            ItemTypeKeys.IRON_SHOVEL,
            ItemTypeKeys.GOLDEN_SHOVEL,
            ItemTypeKeys.DIAMOND_SHOVEL,
            ItemTypeKeys.NETHERITE_SHOVEL,

            ItemTypeKeys.WOODEN_HOE,
            ItemTypeKeys.STONE_HOE,
            ItemTypeKeys.IRON_HOE,
            ItemTypeKeys.GOLDEN_HOE,
            ItemTypeKeys.DIAMOND_HOE,
            ItemTypeKeys.NETHERITE_HOE,

            ItemTypeKeys.WOODEN_SWORD,
            ItemTypeKeys.STONE_SWORD,
            ItemTypeKeys.IRON_SWORD,
            ItemTypeKeys.GOLDEN_SWORD,
            ItemTypeKeys.DIAMOND_SWORD,
            ItemTypeKeys.NETHERITE_SWORD
        );
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
