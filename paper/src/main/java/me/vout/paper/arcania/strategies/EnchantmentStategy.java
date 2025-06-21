package me.vout.paper.arcania.strategies;

import me.vout.core.arcania.enums.EnchantsFilterEnum;
import me.vout.core.arcania.enums.IEnchantRarity;
import me.vout.core.arcania.providers.ArcaniaProvider;
import me.vout.core.arcania.strategies.IEnchantmentStrategy;
import me.vout.paper.arcania.Arcania;
import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import me.vout.paper.arcania.util.EnchantHelper;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EnchantmentStategy implements IEnchantmentStrategy {
    @Override
    public Map<NamespacedKey, Integer> getActiveEnchantments(ItemStack item) {
        return item.getEnchantments().entrySet().stream()
                .filter(enchantment -> enchantment.getKey().getKey().getNamespace().equals(ArcaniaEnchant.NAMESPACE))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getKey(),
                        Map.Entry::getValue
                ));
    }

    @Override
    public int getCustomEnchantLevel(Map<NamespacedKey, Integer> activeEnchants, NamespacedKey enchantKey) {
        return activeEnchants.getOrDefault(enchantKey, -1);
    }

    @Override
    public ItemStack[] getEnchants(EnchantsFilterEnum filter) {
        if (filter == me.vout.core.arcania.enums.EnchantsFilterEnum.ALL)
            return Arcania.getEnchantMap().values().stream()
                    .sorted(
                            Comparator.comparing(entry -> entry.getKey().getKey(), String.CASE_INSENSITIVE_ORDER)
                    )
                    .map(EnchantHelper::toMaxLevelBook)
                    .toArray(ItemStack[]::new);

        else { //todo this is used like 20 times, extrapolate it into core
            IEnchantRarity rarity = ArcaniaProvider.getPlugin().getRarityForFilter(filter);
            return Arcania.getEnchantMap().values().stream()
                    .filter(e -> e.getWeight() == rarity.getNumericValue())
                    .sorted(
                            Comparator.comparing(enchantment -> enchantment.getKey().getKey(), String.CASE_INSENSITIVE_ORDER)
                    )
                    .map(EnchantHelper::toMaxLevelBook)
                    .toArray(ItemStack[]::new);
        }
    }

    private static List<ItemStack> toAllLevelBooks(Enchantment enchant) {
        // Use IntStream to generate numbers from 1 to enchant.getMaxLevel()
        return IntStream.rangeClosed(1, enchant.getMaxLevel())
                .mapToObj(level -> EnchantHelper.getEnchantBook(enchant, level)) // For each level, create an ItemStack
                .collect(Collectors.toList()); // Collect into a List
    }

    @Override
    public ItemStack[] getAllEnchantBooksByLevel(EnchantsFilterEnum filter) {
        if (filter == me.vout.core.arcania.enums.EnchantsFilterEnum.ALL)
            return Arcania.getEnchantMap().values().stream()
                    .sorted(
                            Comparator.comparing(enchantment -> enchantment.getKey().getKey(),String.CASE_INSENSITIVE_ORDER)
                    )
                    .flatMap(enchantment -> toAllLevelBooks(enchantment).stream())
                    .toArray(ItemStack[]::new);
        else {
            IEnchantRarity rarity = ArcaniaProvider.getPlugin().getRarityForFilter(filter);
            return Arcania.getEnchantMap().values().stream()
                    .filter(e -> e.getWeight() == rarity.getNumericValue())
                    .sorted(
                            Comparator.comparing(enchantment -> enchantment.getKey().getKey(), String.CASE_INSENSITIVE_ORDER)
                    )
                    .flatMap(enchant -> toAllLevelBooks(enchant).stream())
                    .toArray(ItemStack[]::new);
        }
    }
}
