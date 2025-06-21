package me.vout.spigot.arcania.util;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.inventory.ItemStack;





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
}
