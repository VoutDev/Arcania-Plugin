package me.vout.arcania.util;

import me.vout.arcania.Arcania;
import me.vout.arcania.gui.PersistentDataEnum;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemHelper {
    public static boolean isTool(Material mat) {
        String name = mat.name();
        return name.endsWith("_PICKAXE") ||
                name.endsWith("_AXE") ||
                name.endsWith("_SHOVEL") ||
                name.endsWith("_HOE") ||
                name.endsWith("SHEARS");
    }

    public static boolean isBlockBreakTool(Material mat) {
        String name = mat.name();
        return name.endsWith("_PICKAXE") ||
                name.endsWith("_SHOVEL") ||
                name.endsWith("_AXE") ||
                name.endsWith("SHEARS");
    }

    public static boolean isToolExtended(Material mat) {
        return isTool(mat) || isSword(mat) || isRangedWeapon(mat) || isTrident(mat) || isHoe(mat);
    }

    public static boolean isDigger(Material mat) {
        return mat.name().endsWith("_PICKAXE") ||
                mat.name().endsWith("_SHOVEL");
    }

    public static boolean isPickaxe(Material mat) {
        return mat.name().endsWith("_PICKAXE");
    }
    public static boolean isHoe(Material mat) {
        return mat.name().endsWith("_HOE");
    }

    public static boolean isSword(Material mat) {
        return mat.name().endsWith("_SWORD");
    }

    public static boolean isTrident(Material mat) {
        return mat.name().endsWith("TRIDENT");
    }

    public static boolean isWeapon(Material mat) {
        String name = mat.name();
        return name.endsWith("_SWORD") ||
                name.endsWith("_AXE") ||
                name.endsWith("BOW") ||
                name.endsWith("CROSSBOW");
    }

    public static boolean isRangedWeapon(Material mat) {
        String name = mat.name();
        return name.endsWith("BOW") ||
                name.endsWith("CROSSBOW");
    }

    public static boolean isMeleeWeapon(Material mat) {
        String name = mat.name();
        return name.endsWith("_SWORD") ||
                name.endsWith("_AXE");
    }

    public static boolean isArmor(Material mat) {
        String name = mat.name();
        return name.endsWith("_HELMET") ||
                name.endsWith("_CHESTPLATE") ||
                name.endsWith("_LEGGINGS") ||
                name.endsWith("_BOOTS");
    }

    public static String colorizeHex(String message) {
        Pattern hexPattern = Pattern.compile("#([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder buffer = new StringBuilder();
        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder replacement = new StringBuilder("§x");
            for (char c : hex.toCharArray()) {
                replacement.append('§').append(c);
            }
            matcher.appendReplacement(buffer, replacement.toString());
        }
        matcher.appendTail(buffer);
        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }

    public static boolean isArcaniaEnchant(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(Arcania.getInstance(), PersistentDataEnum.ENCHANT.toString());
        if (meta == null) return false;
        return meta.getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }

    public static int romanToInt(String roman) {
        return switch (roman) {
            case "II" -> 2;
            case "III" -> 3;
            case "IV" -> 4;
            case "V" -> 5;
            case "VI" -> 6;
            case "VII" -> 7;
            case "VIII" -> 8;
            case "IX" -> 9;
            case "X" -> 10;
            default -> 1;
        };
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

    public static FurnaceRecipe getFurnaceRecipeForItemStack(ItemStack item) {
        Optional<FurnaceRecipe> furnaceRecipe = Bukkit.getRecipesFor(item)
            .stream()
            .filter(possibleRecipe -> {
                if (!(possibleRecipe instanceof FurnaceRecipe)) {
                    return false;
                }
                if (((FurnaceRecipe)possibleRecipe).getInput().getType() != item.getType()) {
                    return false;
                }
                return true;
            })
            .map(unmappedFurnaceRecipe -> (FurnaceRecipe) unmappedFurnaceRecipe)
            .findFirst();
        
        if (furnaceRecipe.isPresent()) {
            return furnaceRecipe.get();
        }

        return null;
    }
}
