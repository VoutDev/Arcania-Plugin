package me.vout.arcania.util;

import me.vout.arcania.Arcania;
import me.vout.arcania.enchant.ArcaniaEnchant;
import me.vout.arcania.enchant.EnchantExtraEnum;
import me.vout.arcania.gui.GuiHelper;
import me.vout.arcania.gui.PersistentDataEnum;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EnchantHelper {

    public static ItemStack getEnchantBook(ArcaniaEnchant enchant, int level, boolean includeDescription) {  //will pass enchant, which will have a rarity attatched
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Enchanted Book");
        List<String> lore = new ArrayList<>();
        lore.add(String.format("%s%s %s",ItemHelper.colorizeHex(enchant.getRarity().getColor()), enchant.getName(), ItemHelper.intToRoman(level)));
        if (includeDescription)
            lore.add(String.format("%s%s%s", ItemHelper.colorizeHex(EnchantExtraEnum.DESCRIPTION.getColor()), ChatColor.ITALIC, enchant.getDescription()));
        meta.setLore(lore);
        meta.addEnchant(Enchantment.UNBREAKING, 1, true); // Glowing effect
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelper.setPersistentData(PersistentDataEnum.ENCHANT.toString().toLowerCase(),String.format("%s_%d",enchant.getName(), level), meta);
        book.setItemMeta(meta);
        return book;
    }

    public static Map<ArcaniaEnchant, Integer> getItemEnchants(ItemStack item) {
        Map<ArcaniaEnchant, Integer> result = new HashMap<>();
        if (item == null || !item.hasItemMeta()) return result;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return result;

        for (String line : meta.getLore()) {
            // Remove color codes if present
            String cleanLine = ChatColor.stripColor(line).trim();
            // Split by last space to get name and level
            int lastSpace = cleanLine.lastIndexOf(' ');
            if (lastSpace == -1) continue;
            String name = cleanLine.substring(0, lastSpace);
            String roman = cleanLine.substring(lastSpace + 1);
            int level = ItemHelper.romanToInt(roman);

            ArcaniaEnchant enchant = Arcania.getEnchantManager().enchantMap.get(name);
            if (enchant != null) {
                result.put(enchant, level);
            }
        }
        return result;
    }

    public static ItemStack toMaxLevelBook(ArcaniaEnchant enchant) {
        return getEnchantBook(enchant, enchant.getMaxLevel(), true);
    }

    public static boolean needsUpdate(ItemStack item) {
        Map<ArcaniaEnchant, Integer> enchants = getItemEnchants(item);
        if (enchants.isEmpty()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        // Build the expected, sorted lore
        List<Map.Entry<ArcaniaEnchant, Integer>> sortedEnchants = new ArrayList<>(enchants.entrySet());
        sortedEnchants.sort(Comparator
                .comparing((Map.Entry<ArcaniaEnchant, Integer> e) -> e.getKey().getRarity().getCost())
                .thenComparing(e -> e.getKey().getName(), String.CASE_INSENSITIVE_ORDER));

        List<String> expectedLore = new ArrayList<>();
        for (Map.Entry<ArcaniaEnchant, Integer> entry : sortedEnchants) {
            ArcaniaEnchant e = entry.getKey();
            int level = entry.getValue();
            expectedLore.add(ItemHelper.colorizeHex(String.format("%s%s %s",
                    e.getRarity().getColor(),
                    e.getName(),
                    ItemHelper.intToRoman(level)
            )));
        }

        List<String> currentLore = meta.getLore();
        if (!Objects.equals(currentLore, expectedLore)) {
            meta.setLore(expectedLore);
            item.setItemMeta(meta);
            return true;
        }
        return false;
    }
}