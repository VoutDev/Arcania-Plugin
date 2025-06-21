package me.vout.spigot.arcania.gui.disenchanter;

import de.tr7zw.nbtapi.NBT;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;
import me.vout.spigot.arcania.util.EnchantHelper;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DisenchanterMenuHandler {

    //TODO: allow for adding a book the first enchant can be moved over to and remove from the initial item, rather than clearing all
    
    public static void handler(InventoryClickEvent event) {
        int clickedSlot = event.getSlot();
        Player player = (Player) event.getWhoClicked();
        ItemStack slotItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();
        Inventory disenchanterInventory = event.getView().getTopInventory();
        Inventory clickedInventory = event.getClickedInventory();
        assert clickedInventory != null;

        if ((slotItem == null || slotItem.getType().isAir()) &&
                (cursorItem == null || cursorItem.getType().isAir())) return;
        if (clickedInventory.equals(disenchanterInventory)) { // player clicked in disenchanter
            event.setCancelled(true);

            if (clickedSlot == DisenchanterMenuHolder.INPUT_SLOT) {
                if (event.isShiftClick() && slotItem != null) {
                    HashMap<Integer, ItemStack> items = player.getInventory().addItem(slotItem.clone());
                    if (items.isEmpty()) {
                        clickedInventory.setItem(clickedSlot, null);
                        clickedInventory.setItem(DisenchanterMenuHolder.OUTPUT_SLOT, null);
                    }
                }
                else {
                    player.setItemOnCursor(slotItem == null ? null : slotItem.clone());
                    clickedInventory.setItem(clickedSlot, cursorItem == null ? null : cursorItem.clone());
                    ItemStack outputItem = getOutputItem(clickedInventory.getItem(DisenchanterMenuHolder.INPUT_SLOT));
                    clickedInventory.setItem(DisenchanterMenuHolder.OUTPUT_SLOT, outputItem);
                }
            } // give player output and remove input and output items
            else if (clickedSlot == DisenchanterMenuHolder.OUTPUT_SLOT &&
                    slotItem != null) {
                Map<NamespacedKey, Integer> enchants = EnchantHelper.getItemEnchants(slotItem);
                int xp = getXpForEnchants(enchants);
                if (event.isShiftClick()) {
                    HashMap<Integer, ItemStack> items = player.getInventory().addItem(slotItem.clone());
                    if (items.isEmpty()) {
                        clickedInventory.setItem(clickedSlot, null);
                        disenchanterInventory.setItem(DisenchanterMenuHolder.INPUT_SLOT, null);
                        player.getWorld().spawn(player.getLocation(), ExperienceOrb.class).setExperience(xp);
                        player.playSound(player.getLocation(), Sound.BLOCK_GRINDSTONE_USE, 0.5f, 1.0f);
                    }
                }
                else {
                    player.setItemOnCursor(slotItem.clone());
                    disenchanterInventory.setItem(clickedSlot, null);
                    disenchanterInventory.setItem(DisenchanterMenuHolder.INPUT_SLOT, null);
                    player.getWorld().spawn(player.getLocation(), ExperienceOrb.class).setExperience(xp);
                    player.playSound(player.getLocation(), Sound.BLOCK_GRINDSTONE_USE, 0.5f, 1.0f);
                }
            }

            // Clicked in either input or output with an item, or an item was clicked on
        } // player shift clicked in their inventory
        else if (slotItem != null &&
                !slotItem.getType().isAir() &&
                !clickedInventory.equals(disenchanterInventory) &&
                event.isShiftClick()) {
            //shift clicked in player inventory and the clicked item was not empty
            event.setCancelled(true);
            ItemStack inputItem = disenchanterInventory.getItem(DisenchanterMenuHolder.INPUT_SLOT);
            if (inputItem != null && !inputItem.getType().isAir()) return; // slot has item
            disenchanterInventory.setItem(DisenchanterMenuHolder.INPUT_SLOT, slotItem.clone());
            clickedInventory.setItem(clickedSlot, null);
            ItemStack outputItem = getOutputItem(disenchanterInventory.getItem(DisenchanterMenuHolder.INPUT_SLOT));
            disenchanterInventory.setItem(DisenchanterMenuHolder.OUTPUT_SLOT, outputItem);
        }
    }

    private static ItemStack getOutputItem(ItemStack inputItem) {
        if (inputItem == null || inputItem.getType().isAir()) return null;

        Map<NamespacedKey, Integer> enchants = EnchantHelper.getItemEnchants(inputItem);
        if (enchants.isEmpty()) return null;
        if (inputItem.getType() == Material.ENCHANTED_BOOK) // otherwise returns special enchanted book
            return new ItemStack(Material.BOOK);
        ItemStack outputItem = inputItem.clone();
        NBT.modify(outputItem, nbt -> {
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
        ItemMeta outputMeta = outputItem.getItemMeta();
        Objects.requireNonNull(outputMeta).setLore(null);
        outputItem.setItemMeta(outputMeta);
        return outputItem;
    }

    private static int getXpForEnchants(Map<NamespacedKey, Integer> enchants) {
        if (enchants.isEmpty()) return 0;
        int total = 0;
        for (Map.Entry<NamespacedKey, Integer> entry : enchants.entrySet()) {
            ArcaniaEnchant enchant = EnchantHelper.namespaceToEnchant(entry.getKey());
            int level = entry.getValue();

            boolean isTreasure = enchant.getRarity() == EnchantRarityEnum.LEGENDARY
                    || enchant.getRarity() == EnchantRarityEnum.ULTRA;

            int minCost = isTreasure
                    ? 1 + (level * 4)
                    : 1 + (level * 2);

            total += minCost;
        }
        // Vanilla: XP = ceil(total / 2), minimum 1
        int xp = (int) Math.ceil(total / 2.0);
        return Math.max(1, xp);
    }
}
