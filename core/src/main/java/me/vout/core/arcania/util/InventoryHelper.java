package me.vout.core.arcania.util;

import me.vout.core.arcania.enums.ArcaniaEnchantType;
import org.bukkit.NamespacedKey;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class InventoryHelper {

    public static void giveOrDrop(Player player, ItemStack... items) { // allows 1 or more items
        HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(items);
        for (ItemStack leftover : leftovers.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), leftover);
        }
    }

    public static boolean containerBlockBreak(Block eventBlock, Container container, Player player, boolean hasMagnet) {
        BlockState state = eventBlock.getState();
        if (!hasMagnet) return true;
        if (state instanceof ShulkerBox) return false; // EnderChest does not extend container interface

        if (state instanceof Chest chest) // getBlockInventory returns left or right half only, not entire double chest if applicable
            processContainerInventory(player, chest.getBlockInventory());
        else
            processContainerInventory(player, container.getInventory());
        return false;
    }

    public static boolean processContainerInventory(Player player, Inventory inventory) {
        InventoryHelper.giveOrDrop(player, Arrays.stream(inventory.getContents())
                .filter(itemStack -> itemStack != null && !itemStack.getType().isAir())
                .toArray(ItemStack[]::new));
        return false;
    }

    public static boolean isContainerBreak(Block eventBlock, Container container, Player player, Map<NamespacedKey, Integer> activeEnchants) {
        boolean hasMagnet = activeEnchants.containsKey(ArcaniaEnchantType.MAGNET.getKey());
        return me.vout.core.arcania.util.InventoryHelper.containerBlockBreak(eventBlock, container, player, hasMagnet);
    }
}
