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
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemHelper {
    private static final Set<String> ARMOR_SUFFIXES = Set.of("_HELMET", "_CHESTPLATE", "_LEGGINGS", "_BOOTS");
    public static Map<Material, FurnaceRecipe> furnaceRecipes = new HashMap<>();

    public static void initFurnaceRecipes() {
        Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
        ArcaniaProvider.getPlugin().getJavaPlugin().getLogger().log(Level.INFO, "initializing furnace recipes");
        while (recipeIterator.hasNext()) {
            Recipe recipe = recipeIterator.next();
            if (recipe instanceof FurnaceRecipe furnaceRecipe) {
                furnaceRecipes.put(furnaceRecipe.getInput().getType(), furnaceRecipe);
            }
        }
        ArcaniaProvider.getPlugin().getJavaPlugin().getLogger().log(Level.INFO, "number of furnace recipes: " + furnaceRecipes.size());
    }


    // --- Internal Helper for Generic Suffix Checking ---
    private static boolean matchesAnySuffix(String materialName, Set<String> suffixes) {
        for (String suffix : suffixes) {
            if (materialName.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    //Basic Item Checks

    public static boolean isPickaxe(Material mat) {
        return mat.name().endsWith("_PICKAXE");
    }

    public static boolean isShovel(Material mat) {
        return mat.name().endsWith("_SHOVEL");
    }

    public static boolean isAxe(Material mat) {
        return mat.name().endsWith("_AXE");
    }

    public static boolean isHoe(Material mat) {
        return mat.name().endsWith("_HOE");
    }

    public static boolean isSword(Material mat) {
        return mat.name().endsWith("_SWORD");
    }

    public static boolean isMace(Material mat) {
        return mat.name().endsWith("MACE");
    }

    public static boolean isTrident(Material mat) {
        return mat.name().endsWith("TRIDENT");
    }

    public static boolean isBow(Material mat) {
        return mat.name().endsWith("BOW");
    }

    public static boolean isCrossbow(Material mat) {
        return mat.name().endsWith("CROSSBOW");
    }

    public static boolean isShears(Material mat) {
        return mat.name().endsWith("SHEARS");
    }

    //Functional groupings

    public static boolean isMiningTool(Material mat) {
        return isPickaxe(mat) || isShovel(mat);
    }

    public static boolean isHarvestingTool(Material mat) { // Tools for farming, logging, breaking (non-combat)
        return isAxe(mat) || isHoe(mat) || isShears(mat) || isMiningTool(mat);
    }

    public static boolean isMeleeWeapon(Material mat) {
        return isSword(mat) || isAxe(mat) || isMace(mat);
    }

    public static boolean isRangedWeapon(Material mat) {
        return isBow(mat) || isCrossbow(mat);
    }

    public static boolean isCombatWeapon(Material mat) {
        return isMeleeWeapon(mat) || isRangedWeapon(mat) || isTrident(mat); // Trident is both melee and ranged
    }

    public static boolean isWearableArmor(Material mat) {
        return matchesAnySuffix(mat.name(), ARMOR_SUFFIXES);
    }

    public static boolean isMainHandTool(Material mat) {
        return isHarvestingTool(mat) || isCombatWeapon(mat);
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
