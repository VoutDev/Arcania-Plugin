package me.vout.spigot.arcania.enchant;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.function.Predicate;

public abstract class ArcaniaEnchant {
    private final String name;
    private final double chance;
    private final int weight;
    private final String description;
    private final EnchantRarityEnum rarity;
    public static final String KEY = "arcania_enchants";
    private final Predicate<Material> canApplyPredicate;
    private final int max;

    public ArcaniaEnchant(String name, String description, EnchantRarityEnum rarity, int max, double chance, int weight, Predicate<Material> canApplyPredicate) {
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.max = max;
        this.chance = chance;
        this.weight = weight;
        this.canApplyPredicate = canApplyPredicate;
    }

    public boolean isCompatibleWith(Enchantment enchantment) {
        return true;
    }

    public boolean isCompatibleWith(ArcaniaEnchant arcaniaEnchant) {
        return true;
    }

    public int getMaxLevel() {
        return max;
    }

    public int getWeight() {
        return weight;
    }

    public double getChance() {
        return  chance;
    }

    public EnchantRarityEnum getRarity() {
        return rarity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean canApplyTo(Material mat) {
        return canApplyPredicate.test(mat);
    }

    public abstract boolean canApplyWith(ArcaniaEnchant enchant);
}
