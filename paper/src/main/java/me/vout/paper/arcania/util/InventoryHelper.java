package me.vout.paper.arcania.util;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryHelper {

    public static void giveOrDrop(Player player, ItemStack... items) { // allows 1 or more items
        HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(items);
        for (ItemStack leftover : leftovers.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), leftover);
        }
    }
}
