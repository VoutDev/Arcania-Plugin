package me.vout.paper.arcania.enchant;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

public abstract class ArcaniaEnchant extends Enchantment {
    public static final String NAMESPACE = "arcania";
    private final String key;
    protected final String name;
    protected final String description;
    protected final int maxLevel;
    protected final int startLevel;
    protected final int weight;
    protected final int minModifiedCost;
    protected final int maxModifiedCost;
    protected final int anvilCost;


    public ArcaniaEnchant(String name, String key, String description, int maxLevel, int startLevel, int weight, int minModifiedCost, int maxModifiedCost, int anvilCost) {
        this.name = name;   
        this.key = key;
        this.description = description;
        this.maxLevel = maxLevel;
        this.startLevel = startLevel;
        this.weight = weight;
        this.minModifiedCost = minModifiedCost;
        this.maxModifiedCost = maxModifiedCost;
        this.anvilCost = anvilCost;
    }

    public String getName() {
        return name;
    }

    public @NotNull NamespacedKey getKey() {
        return new NamespacedKey(NAMESPACE, key);
    }

}
