package me.vout.paper.arcania.enchant;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import io.papermc.paper.registry.data.EnchantmentRegistryEntry.Builder;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry.EnchantmentCost;
import io.papermc.paper.registry.event.WritableRegistry;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import net.kyori.adventure.text.Component;

public abstract class ArcaniaEnchant extends Enchantment {
    public static final String NAMESPACE = "arcania";
    protected final String key;
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return new NamespacedKey(NAMESPACE, key);
    }

    public void register(WritableRegistry<Enchantment, Builder> registry) {
        registry.register(EnchantmentKeys.create(getKey()), b -> 
         b.description(Component.text(getName()))
            .supportedItems(getSupportedItems())
            .primaryItems(getPrimaryItems())
            .exclusiveWith(getExclusiveWith())
            .anvilCost(getAnvilCost())
            .maxLevel(getMaxLevel())
            .weight(getWeight())
            .minimumCost(EnchantmentCost.of(getStartLevel(), getMinModifiedCost(getStartLevel())))
            .maximumCost(EnchantmentCost.of(getStartLevel(), getMaxModifiedCost(getStartLevel())))
            .activeSlots(getActiveSlotGroups())
        );
    };

}
