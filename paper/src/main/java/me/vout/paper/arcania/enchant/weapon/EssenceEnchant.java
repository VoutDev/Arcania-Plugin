package me.vout.paper.arcania.enchant.weapon;

import me.vout.paper.arcania.Arcania;
import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import me.vout.paper.arcania.enchant.tool.MagnetEnchant;
import me.vout.paper.arcania.manager.ConfigManager;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
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

import java.util.List;
import java.util.Map;
import java.util.Set;

public class EssenceEnchant extends ArcaniaEnchant {
    public static final EssenceEnchant INSTANCE = new EssenceEnchant();
    private EssenceEnchant() {
        super("Essence",
                "essence",
                "Increases xp dropped by mobs",
                3,
                1,
                1,
                1,
                1,
                1
        );
    }

    public static void onProc(Player player, EntityDeathEvent event, Map<Enchantment, Integer> enchants) {
        int essenceLevel = 0;
        Enchantment essenceEnchant = Arcania.getEnchantRegistry().get(EssenceEnchant.INSTANCE.getKey());
        if (enchants.containsKey(essenceEnchant))
            essenceLevel = enchants.get(essenceEnchant);

        float xp = event.getDroppedExp();
        if (essenceLevel > 0)
            xp = getScaledXP(xp, essenceLevel);

        Enchantment magnetEnchant = Arcania.getEnchantRegistry().get(MagnetEnchant.INSTANCE.getKey());

        if (enchants.containsKey(magnetEnchant)) {
           MagnetEnchant.onProc(player, event, xp);
        } else {
            event.setDroppedExp((int)xp);
        }
    }

    public static float getScaledXP(float baseXP, int level) {
        ConfigManager configManager = Arcania.getConfigManager();
        double k = configManager.getEssenceK();
        List<Double> multipliers = configManager.getEssenceXpMultiplier();

        if (baseXP <= 5) {
            return (float) (baseXP * (1 + multipliers.get(level - 1)));
        } else {
            double bonus = baseXP * multipliers.get(level - 1) * (k / (baseXP + k));
            return (float) (baseXP + bonus);
        }
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
        return RegistrySet.keySet(RegistryKey.ITEM, ItemTypeKeys.ENCHANTED_BOOK, ItemTypeKeys.STONE_SWORD, ItemTypeKeys.IRON_SWORD, ItemTypeKeys.GOLDEN_SWORD, ItemTypeKeys.DIAMOND_SWORD, ItemTypeKeys.NETHERITE_SWORD, ItemTypeKeys.BOW, ItemTypeKeys.CROSSBOW);
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
        return RegistrySet.keySet(RegistryKey.ITEM, ItemTypeKeys.ENCHANTED_BOOK, ItemTypeKeys.STONE_SWORD, ItemTypeKeys.IRON_SWORD, ItemTypeKeys.GOLDEN_SWORD, ItemTypeKeys.DIAMOND_SWORD, ItemTypeKeys.NETHERITE_SWORD, ItemTypeKeys.BOW, ItemTypeKeys.CROSSBOW);
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
        return EnchantmentTarget.WEAPON;
    }
}
