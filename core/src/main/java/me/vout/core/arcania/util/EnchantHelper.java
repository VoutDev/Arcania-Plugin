package me.vout.core.arcania.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EnchantHelper {

    public static ItemStack getEnchantBook(Enchantment enchant, int level) {  //will pass enchant, which will have a rarity attatched
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        book.addEnchantment(enchant, level);
        return book;
    }

    public static ItemStack toMaxLevelBook(Enchantment enchant) {
        return getEnchantBook(enchant, enchant.getMaxLevel());
    }

    public static List<ItemStack> toAllLevelBooks(Enchantment enchant) {
        // Use IntStream to generate numbers from 1 to enchant.getMaxLevel()
        return IntStream.rangeClosed(1, enchant.getMaxLevel())
                .mapToObj(level -> EnchantHelper.getEnchantBook(enchant, level)) // For each level, create an ItemStack
                .collect(Collectors.toList()); // Collect into a List
    }
}