package me.vout.paper.arcania.enchant.registry;


import org.bukkit.enchantments.Enchantment;

import io.papermc.paper.registry.data.EnchantmentRegistryEntry.Builder;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry.EnchantmentCost;
import io.papermc.paper.registry.event.WritableRegistry;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import net.kyori.adventure.text.Component;

public class RegisterArcaniaEnchant {
    public static void register(WritableRegistry<Enchantment, Builder> registry, ArcaniaEnchant enchantment) {
        registry.register(EnchantmentKeys.create(enchantment.getKey()), b -> 
         b.description(Component.text(enchantment.getName()))
            .supportedItems(enchantment.getSupportedItems())
            .primaryItems(enchantment.getPrimaryItems())
            .exclusiveWith(enchantment.getExclusiveWith())
            .anvilCost(enchantment.getAnvilCost())
            .maxLevel(enchantment.getMaxLevel())
            .weight(enchantment.getWeight())
            .minimumCost(EnchantmentCost.of(enchantment.getStartLevel(), enchantment.getMinModifiedCost(enchantment.getStartLevel())))
            .maximumCost(EnchantmentCost.of(enchantment.getStartLevel(), enchantment.getMaxModifiedCost(enchantment.getStartLevel())))
            .activeSlots(enchantment.getActiveSlotGroups())
        );
    }
}
