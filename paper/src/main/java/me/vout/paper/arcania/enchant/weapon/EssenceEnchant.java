package me.vout.paper.arcania.enchant.weapon;

import me.vout.paper.arcania.Arcania;
import me.vout.paper.arcania.ArcaniaBootstrap;
import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import me.vout.paper.arcania.enchant.EnchantRarityEnum;
import me.vout.paper.arcania.enchant.tool.MagnetEnchant;
import me.vout.paper.arcania.manager.ConfigManager;
import me.vout.paper.arcania.util.ItemHelper;
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
import io.papermc.paper.registry.set.RegistryKeySet;

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
