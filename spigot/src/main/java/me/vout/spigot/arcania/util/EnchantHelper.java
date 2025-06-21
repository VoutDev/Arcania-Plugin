package me.vout.spigot.arcania.util;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import me.vout.core.arcania.enums.EnchantExtraEnum;
import me.vout.core.arcania.providers.ArcaniaProvider;
import me.vout.spigot.arcania.Arcania;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantHelper {

    public static ItemStack getEnchantBook(ArcaniaEnchant enchant, int level, boolean includeDescription) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);

        // Add NBT data
        NBT.modify(book, nbt -> {
            // Get or create the arcania:enchants compound
            var nbtCompound = nbt.getOrCreateCompound("arcania").getOrCreateCompound("enchants");

            //todo Handle this enchant.getName().replace to handle better, this is required because vein miner is not valid
            // Store the enchant name and level in the 'enchants' compound
            nbtCompound.setString(enchant.KEY.getKey(), String.valueOf(level));
        });

        // Update display name and lore
        ItemMeta meta = book.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "Enchanted Book");

            List<String> lore = new ArrayList<>();
            lore.add(String.format("%s%s %s",
                    me.vout.core.arcania.util.ItemHelper.colorizeHex(enchant.getRarity().getColor()),
                    enchant.getName(),
                    ItemHelper.intToRoman(level)));

            if (includeDescription) {
                lore.add(String.format("%s%s",
                        me.vout.core.arcania.util.ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor()),
                        enchant.getDescription()));
            }
            meta.setLore(lore);
            meta.addEnchant(Enchantment.UNBREAKING, 1, true); // Glowing effect
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
            book.setItemMeta(meta);
        }
        return book;
    }

    public static Map<NamespacedKey, Integer> getItemEnchants(ItemStack item) {
        Map<NamespacedKey, Integer> result = new HashMap<>();
        if (item.getType().isAir()) return result;
        NBT.get(item, nbt -> {
            if (nbt != null) {
                ReadableNBT enchantsCompound = nbt.resolveCompound("arcania.enchants");
                if (enchantsCompound != null) {
                    // Iterate through the keys in the 'enchants' compound
                    for (String enchantName : enchantsCompound.getKeys()) {
                        ArcaniaEnchant enchant = namespaceToEnchant(new NamespacedKey(ArcaniaProvider.getPlugin().getJavaPlugin(), enchantName.toLowerCase()));
                        result.put(enchant.KEY, Integer.parseInt(enchantsCompound.getString(enchantName)));
                    }
                }
            }
        });
        return result;
    }

    public static ArcaniaEnchant namespaceToEnchant(NamespacedKey key) {
       return Arcania.getEnchantManager().enchantMap.get(key);
    }

    public static ItemStack toMaxLevelBook(ArcaniaEnchant enchant) {
        return getEnchantBook(enchant, enchant.getMaxLevel(), true);
    }
}