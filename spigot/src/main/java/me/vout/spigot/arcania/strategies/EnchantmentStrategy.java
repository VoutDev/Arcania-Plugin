package me.vout.spigot.arcania.strategies;

import me.vout.core.arcania.enums.EnchantsFilterEnum;
import me.vout.core.arcania.enums.IEnchantRarity;
import me.vout.core.arcania.providers.ArcaniaProvider;
import me.vout.core.arcania.strategies.IEnchantmentStrategy;
import me.vout.spigot.arcania.Arcania;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.util.EnchantHelper;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EnchantmentStrategy implements IEnchantmentStrategy {
    @Override
    public Map<NamespacedKey, Integer> getActiveEnchantments(ItemStack item) {
        // Spigot's EnchantHelper handles this
        return EnchantHelper.getItemEnchants(item).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public int getCustomEnchantLevel(Map<NamespacedKey, Integer> activeEnchants, NamespacedKey enchantKey) {
        return activeEnchants.getOrDefault(enchantKey, 0);
    }

    @Override
    public ItemStack[] getEnchants(EnchantsFilterEnum filter) {
        if (filter == EnchantsFilterEnum.ALL)
            return Arcania.getEnchantManager().getEnchants().stream()
                    .sorted(
                            Comparator.comparing(ArcaniaEnchant::getRarity)
                                    .thenComparing(ArcaniaEnchant::getName, String.CASE_INSENSITIVE_ORDER)
                    )
                    .map(EnchantHelper::toMaxLevelBook)
                    .toArray(ItemStack[]::new);

        else {
            IEnchantRarity rarity = ArcaniaProvider.getPlugin().getRarityForFilter(filter);
            return Arcania.getEnchantManager().getEnchants().stream()
                    .filter(e -> e.getRarity() == rarity)
                    .sorted(Comparator.comparing(ArcaniaEnchant::getName, String.CASE_INSENSITIVE_ORDER))
                    .map(EnchantHelper::toMaxLevelBook)
                    .toArray(ItemStack[]::new);
        }
    }

    private List<ItemStack> toAllLevelBooks(ArcaniaEnchant enchant) {
        // Use IntStream to generate numbers from 1 to enchant.getMaxLevel()
        return IntStream.rangeClosed(1, enchant.getMaxLevel())
                .mapToObj(level -> EnchantHelper.getEnchantBook(enchant, level, true)) // For each level, create an ItemStack
                .collect(Collectors.toList()); // Collect into a List
    }
    @Override
    public ItemStack[] getAllEnchantBooksByLevel(EnchantsFilterEnum filter) {
        if (filter == EnchantsFilterEnum.ALL)
            return Arcania.getEnchantManager().getEnchants().stream()
                    .sorted(Comparator.comparing(ArcaniaEnchant::getName, String.CASE_INSENSITIVE_ORDER))
                    .flatMap(enchant -> toAllLevelBooks(enchant).stream())
                    .toArray(ItemStack[]::new);
        else {
            IEnchantRarity rarity = ArcaniaProvider.getPlugin().getRarityForFilter(filter);
            return Arcania.getEnchantManager().getEnchants().stream()
                    .filter(e -> e.getRarity() == rarity)
                    .sorted(
                            Comparator.comparing(ArcaniaEnchant::getName, String.CASE_INSENSITIVE_ORDER)
                    )
                    .flatMap(enchant -> toAllLevelBooks(enchant).stream())
                    .toArray(ItemStack[]::new);
        }
    }
}