package me.vout.paper.arcania.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;

import me.vout.paper.arcania.Arcania;

public class FurnaceRecipeHelper {
    public static Map<RecipeChoice, FurnaceRecipe> furnaceRecipes = new HashMap<>();

    public static void initFurnaceRecipes() {
        Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
        Arcania.getInstance().getLogger().log(Level.INFO, "initializing furnace recipes");
        while (recipeIterator.hasNext()) {
            Recipe recipe = recipeIterator.next();
            if (recipe instanceof FurnaceRecipe furnaceRecipe) {
                // Arcania.getInstance().getLogger().log(Level.INFO, "initFurnaceRecipes: " + furnaceRecipe.getInput().getType().name());
                furnaceRecipes.put(furnaceRecipe.getInputChoice(), furnaceRecipe);
            }
        }
        Arcania.getInstance().getLogger().log(Level.INFO, "number of furnace recipes: " + furnaceRecipes.size());
    }

    public static FurnaceRecipe getFurnaceRecipeForItemStack(ItemStack item) {
        FurnaceRecipe furnaceRecipe = furnaceRecipes.values().stream().filter(recipe -> recipe.getInputChoice().test(item)).findFirst().orElse(null);
        return furnaceRecipe;
    }
}
