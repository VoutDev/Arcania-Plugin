package me.vout.arcania.enchant.tool;

import me.vout.arcania.enchant.ArcaniaEnchant;
import me.vout.arcania.enchant.EnchantRarityEnum;
import me.vout.arcania.util.InventoryHelper;
import me.vout.arcania.util.ItemHelper;

import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

public class SmeltEnchant extends ArcaniaEnchant {
    public static  final SmeltEnchant INSTANCE = new SmeltEnchant();
    private SmeltEnchant() {
        super("Smelt",
                "Instantly smelt the block into it's smelting result",
                EnchantRarityEnum.ULTRA,
                1,
                0.1,
                5,
                ItemHelper::isPickaxe);
    }

    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }

    public static void onProc(Player player, EntityDeathEvent event, int xp) {
        // this is a hack to get the xp to give to the player
        // because the xp is passed by reference and we need to modify it
        // and we can't use a local variable because it will be lost when the method returns
        final int[] xpToGive = {xp};
        event.setDroppedExp(0);
        event.getDrops().forEach(itemStack -> {
            // check if the item has a smelting result
            FurnaceRecipe furnaceRecipe = ItemHelper.getFurnaceRecipeForItemStack(itemStack);
            // if the smelting result is null just give the player the drop
            if (furnaceRecipe == null) {
                InventoryHelper.giveOrDrop(player, itemStack);
            } else {
                // give the player the smelting result
                ItemStack smeltedItemStack = furnaceRecipe.getResult();
                // set the amount of the item to whatever the initial drop was
                smeltedItemStack.setAmount(itemStack.getAmount());
                // add the experience to the total xp to give
                xpToGive[0] += furnaceRecipe.getExperience();
                // give the player the smelting result
                InventoryHelper.giveOrDrop(player, smeltedItemStack);
            }
        });
        // clear the drops
        event.getDrops().clear();
        // give the player the xp
        player.getWorld().spawn(player.getLocation(), ExperienceOrb.class).setExperience(xpToGive[0]);
    }
}
