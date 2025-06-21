package me.vout.core.arcania.strategies;

import me.vout.core.arcania.enums.EnchantsFilterEnum;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface IEnchantmentStrategy {
    Map<NamespacedKey, Integer> getActiveEnchantments(ItemStack item);
    int getCustomEnchantLevel(Map<NamespacedKey, Integer> activeEnchants, NamespacedKey enchantKey);
    ItemStack[] getEnchants(EnchantsFilterEnum filter);
    ItemStack[] getAllEnchantBooksByLevel(EnchantsFilterEnum filter);
}
