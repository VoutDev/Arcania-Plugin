package me.vout.paper.arcania.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class EnchantHelper {

    public static ItemStack getEnchantBook(Enchantment enchant, int level) {  //will pass enchant, which will have a rarity attatched
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        book.addEnchantment(enchant, level);
        return book;
    }

    public static ItemStack toMaxLevelBook(Enchantment enchant) {
        return getEnchantBook(enchant, enchant.getMaxLevel());
    }
}