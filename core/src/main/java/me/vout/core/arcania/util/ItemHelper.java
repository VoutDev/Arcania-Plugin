package me.vout.core.arcania.util;

import me.vout.core.arcania.enchants.tool.SmeltHelper;
import me.vout.core.arcania.providers.ArcaniaProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemHelper {

    public static Map<Material, FurnaceRecipe> furnaceRecipes = new HashMap<>();

    public static void initFurnaceRecipes() {
        Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
        ArcaniaProvider.getPlugin().getJavaPlugin().getLogger().log(Level.INFO, "initializing furnace recipes");
        while (recipeIterator.hasNext()) {
            Recipe recipe = recipeIterator.next();
            if (recipe instanceof FurnaceRecipe furnaceRecipe) {
                //todo see what these return, and if it makes sense that logs dont get converted into anything
                // Arcania.getInstance().getLogger().log(Level.INFO, "initFurnaceRecipes: " + furnaceRecipe.getInput().getType().name());
                furnaceRecipes.put(furnaceRecipe.getInput().getType(), furnaceRecipe);
            }
        }
        ArcaniaProvider.getPlugin().getJavaPlugin().getLogger().log(Level.INFO, "number of furnace recipes: " + furnaceRecipes.size());
    }
    
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

    public static FurnaceRecipe getFurnaceRecipeForItemStack(ItemStack item) {
        if (SmeltHelper.isBlacklistedSmeltBlock(item.getType())) return null;
        Pattern log = Pattern.compile(".*_LOG");
        if (item.getType().name().matches(log.pattern())) {
            return furnaceRecipes.get(Material.DARK_OAK_LOG);
        }
        return furnaceRecipes.get(item.getType());
    }
}
