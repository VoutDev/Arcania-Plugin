package me.vout.spigot.arcania.util;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class ItemHelper {
    public static boolean isArcaniaEnchant(ItemStack item) {
        if (item.getType().isAir()) return false;
        return NBT.get(item, nbt -> {
            if (nbt != null) {
                ReadableNBT enchantsCompound = nbt.resolveCompound("arcania.enchants");
                return enchantsCompound != null;
            }
            return false;
        });
    }

    public static String intToRoman(int num) {
        return switch (num) {
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> "I";
        };
    }

    public static void clearDataForItem(ItemStack item) {
        if (item == null || item.getType().isAir()) return;
        NBT.modify(item, nbt -> {
            // Clears existing Arcania section and enchants
            var arcaniaCompound = nbt.getCompound("arcania");
            // Only proceed if the 'arcania' compound exists
            if (arcaniaCompound != null) {
                // Remove the 'enchants' compound directly from 'arcania' compound
                arcaniaCompound.removeKey("enchants");

                // Now, check if the 'arcania' compound itself is empty after this removal
                if (arcaniaCompound.getKeys().isEmpty()) {
                    // If 'arcania' is empty, remove it from the item's main NBT
                    nbt.removeKey("arcania");
                }
            }
        });
        ItemMeta outputMeta = item.getItemMeta();
        Objects.requireNonNull(outputMeta).setLore(null);
        item.setItemMeta(outputMeta);
    }
}
