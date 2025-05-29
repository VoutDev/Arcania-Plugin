package me.vout.arcania.gui.tinkerer;

import me.vout.arcania.enchant.ArcaniaEnchant;
import me.vout.arcania.util.EnchantHelper;
import me.vout.arcania.util.ItemHelper;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class TinkererMenuHandler {

    //TODO: Require xp to combine enchants or apply enchants (show a green or red block between input2 and output telling the xp amount required)
    
    public static void handler(InventoryClickEvent event) {
        // This doesn't seem to work at all
        int clickedSlot = event.getSlot();
        Player player = (Player) event.getWhoClicked();
        ItemStack slotItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();
        Inventory tinkererInventory = event.getView().getTopInventory();
        Inventory clickedInventory = event.getClickedInventory();
        assert clickedInventory != null;

        if ((slotItem == null || slotItem.getType().isAir()) &&
                (cursorItem == null || cursorItem.getType().isAir())) return;

        if (clickedInventory.equals(tinkererInventory)) { // player clicked in tinkerer
            event.setCancelled(true);

            if (clickedSlot == TinkererMenuHolder.INPUT_SLOT1 || clickedSlot == TinkererMenuHolder.INPUT_SLOT2) {
                if (event.isShiftClick() && slotItem != null) {
                    HashMap<Integer, ItemStack> items = player.getInventory().addItem(slotItem.clone());
                    if (items.isEmpty())
                        clickedInventory.setItem(clickedSlot, null);
                }
                else {
                    player.setItemOnCursor(slotItem == null ? null : slotItem.clone());
                    clickedInventory.setItem(clickedSlot, cursorItem == null ? null : cursorItem.clone());
                }
            } // give player output and remove input and output items
            else if (clickedSlot == TinkererMenuHolder.OUTPUT_SLOT &&
                    slotItem != null) {
                if (event.isShiftClick()) {
                    HashMap<Integer, ItemStack> items = player.getInventory().addItem(slotItem.clone());
                    if (items.isEmpty()) {
                        clickedInventory.setItem(clickedSlot, null);
                        tinkererInventory.setItem(TinkererMenuHolder.INPUT_SLOT1, null);
                        tinkererInventory.setItem(TinkererMenuHolder.INPUT_SLOT2, null);
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.3f, 1.0f);
                    }
                }
                else {
                    player.setItemOnCursor(slotItem.clone());
                    tinkererInventory.setItem(clickedSlot, null);
                    tinkererInventory.setItem(TinkererMenuHolder.INPUT_SLOT1, null);
                    tinkererInventory.setItem(TinkererMenuHolder.INPUT_SLOT2, null);
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.3f, 1.0f);
                }
            }

            // Clicked in either input or output with an item, or an item was clicked on
        } // player shift clicked in their inventory
        else if (slotItem != null &&
                !slotItem.getType().isAir() &&
                !clickedInventory.equals(tinkererInventory) &&
                event.isShiftClick()) {
            //shift clicked in player inventory and the clicked item was not empty
            event.setCancelled(true);
            ItemStack inputItem1 = tinkererInventory.getItem(TinkererMenuHolder.INPUT_SLOT1);
            ItemStack inputItem2 = tinkererInventory.getItem(TinkererMenuHolder.INPUT_SLOT2);
            if (inputItem1 != null &&
                    !inputItem1.getType().isAir() &&
                    inputItem2 != null &&
                    !inputItem2.getType().isAir()) return; // both inputs have items

            if (inputItem1 == null || inputItem1.getType().isAir()) { // at least slot 1 is empty
                tinkererInventory.setItem(TinkererMenuHolder.INPUT_SLOT1, slotItem.clone());
                clickedInventory.setItem(clickedSlot, null);
            }
            else { // only slot 2 empty
                tinkererInventory.setItem(TinkererMenuHolder.INPUT_SLOT2, slotItem.clone());
                clickedInventory.setItem(clickedSlot, null);
            }
        }
        ItemStack input1 = tinkererInventory.getItem(TinkererMenuHolder.INPUT_SLOT1);
        ItemStack input2 = tinkererInventory.getItem(TinkererMenuHolder.INPUT_SLOT2);
        if (input1 != null && input2 != null) {
            ItemStack output = buildOutputItem(input1, input2);
            tinkererInventory.setItem(TinkererMenuHolder.OUTPUT_SLOT, output);
        }
        else
            tinkererInventory.setItem(TinkererMenuHolder.OUTPUT_SLOT, null);
    }

    private static ItemStack buildOutputItem(ItemStack input1, ItemStack input2) {
        if ((ItemHelper.isArmor(input1.getType()) || ItemHelper.isToolExtended(input1.getType())) &&
        input2.getType() == Material.ENCHANTED_BOOK) {
            if (ItemHelper.isArcaniaEnchant(input2)) {
                Map<ArcaniaEnchant, Integer> enchantItemMap = EnchantHelper.getItemEnchants(input1);
                Map<ArcaniaEnchant, Integer> enchantBookMap = EnchantHelper.getItemEnchants(input2);

                Map<ArcaniaEnchant, Integer> enchantsToAdd = new HashMap<>();

                ItemStack outputItem = input1.clone();
                ItemMeta outputMeta = outputItem.getItemMeta();
                boolean canApplyAny = false;

                for (ArcaniaEnchant enchant: enchantBookMap.keySet()) {
                    if (!enchant.canApplyTo(input1.getType()) || enchantItemMap.keySet().stream().anyMatch(e -> !e.canApplyWith(enchant))) continue;
                    canApplyAny = true;
                    enchantsToAdd.put(enchant, enchantBookMap.get(enchant));
                }

                if (canApplyAny) {
                    // Merges duplicate enchants to use highest level
                    for (Map.Entry<ArcaniaEnchant, Integer> entry: enchantItemMap.entrySet()) {
                        enchantsToAdd.merge(entry.getKey(), entry.getValue(), (oldLevel, newLevel) -> {
                            if (oldLevel.equals(newLevel)) {
                                // Both levels are the same, so increment (up to max)
                                int max = entry.getKey().getMaxLevel();
                                return Math.min(oldLevel + 1, max);
                            } else {
                                // Levels are different, use the higher one
                                return Math.max(oldLevel, newLevel);
                            }
                        });
                    }

                    // sorts enchants by rarity, by name and by level
                    List<Map.Entry<ArcaniaEnchant, Integer>> sortedEnchants = new ArrayList<>(enchantsToAdd.entrySet());
                    sortedEnchants.sort(Comparator
                            .comparing((Map.Entry<ArcaniaEnchant, Integer> e) -> e.getKey().getRarity().getCost())
                            .thenComparing(e -> e.getKey().getName(), String.CASE_INSENSITIVE_ORDER));

                    List<String> lore = new ArrayList<>();
                    for (Map.Entry<ArcaniaEnchant, Integer> entry : sortedEnchants) {
                        ArcaniaEnchant enchant = entry.getKey();
                        int level = entry.getValue();
                        lore.add(ItemHelper.colorizeHex(String.format("%s%s %s",
                                enchant.getRarity().getColor(),
                                enchant.getName(),
                                ItemHelper.intToRoman(level)
                        )));
                    }
                    if (outputMeta != null) outputMeta.setLore(lore);
                    outputItem.setItemMeta(outputMeta);
                    return outputItem;
                }
                return null; //input1.clone(); maybe enchant is a clone?
            }
        } // 2 valid non enchanted book items
        else if (input1.getType().equals(input2.getType()) &&
                (ItemHelper.isArmor(input1.getType()) || ItemHelper.isToolExtended(input1.getType())) &&
                (ItemHelper.isArmor(input2.getType()) || ItemHelper.isToolExtended(input2.getType()))) {
            Map<ArcaniaEnchant, Integer> enchantItemMap = EnchantHelper.getItemEnchants(input1);
            Map<ArcaniaEnchant, Integer> enchantItem2Map = EnchantHelper.getItemEnchants(input2);
            if (enchantItemMap.isEmpty() && enchantItem2Map.isEmpty())  return null;

            Map<ArcaniaEnchant, Integer> enchantsToAdd = new HashMap<>();
            ItemStack outputItem = input1.clone();
            ItemMeta outputMeta = outputItem.getItemMeta();

            for (ArcaniaEnchant enchant: enchantItemMap.keySet()) {
                enchantsToAdd.put(enchant, enchantItemMap.get(enchant));
            }

            for (Map.Entry<ArcaniaEnchant, Integer> entry: enchantItem2Map.entrySet()) {
                enchantsToAdd.merge(entry.getKey(), entry.getValue(), (oldLevel, newLevel) -> {
                    if (oldLevel.equals(newLevel)) {
                        // Both levels are the same, so increment (up to max)
                        int max = entry.getKey().getMaxLevel();
                        return Math.min(oldLevel + 1, max);
                    } else {
                        // Levels are different, use the higher one
                        return Math.max(oldLevel, newLevel);
                    }
                });
            }

            if (enchantsToAdd.isEmpty()) return null;

            List<Map.Entry<ArcaniaEnchant, Integer>> sortedEnchants = new ArrayList<>(enchantsToAdd.entrySet());
            sortedEnchants.sort(Comparator
                    .comparing((Map.Entry<ArcaniaEnchant, Integer> e) -> e.getKey().getRarity().getCost())
                    .thenComparing(e -> e.getKey().getName(), String.CASE_INSENSITIVE_ORDER));

            List<String> lore = new ArrayList<>();
            for (Map.Entry<ArcaniaEnchant, Integer> entry : sortedEnchants) {
                ArcaniaEnchant enchant = entry.getKey();
                int level = entry.getValue();
                lore.add(ItemHelper.colorizeHex(String.format("%s%s %s",
                        enchant.getRarity().getColor(),
                        enchant.getName(),
                        ItemHelper.intToRoman(level)
                )));
            }
            if (outputMeta != null) outputMeta.setLore(lore);
            outputItem.setItemMeta(outputMeta);
            return outputItem;
        }
        else if (input1.getType() == Material.ENCHANTED_BOOK &&
                input2.getType() == Material.ENCHANTED_BOOK) {
            if (ItemHelper.isArcaniaEnchant(input1) && ItemHelper.isArcaniaEnchant(input2)) {
                Map<ArcaniaEnchant, Integer> enchantBook1Map = EnchantHelper.getItemEnchants(input1);
                Map<ArcaniaEnchant, Integer> enchantBook2Map = EnchantHelper.getItemEnchants(input2);
                Map<ArcaniaEnchant, Integer> enchantsToAdd = new HashMap<>();

                ItemStack outputItem = input1.clone();
                ItemMeta outputMeta = outputItem.getItemMeta();
                boolean canApplyAny = false;

                for (ArcaniaEnchant enchant: enchantBook2Map.keySet()) {
                    if (enchantBook1Map.keySet().stream().anyMatch(e -> !e.canApplyWith(enchant))) continue;

                    canApplyAny = true;
                    enchantsToAdd.put(enchant, enchantBook2Map.get(enchant));
                }

                if (canApplyAny) {
                    // Merges duplicate enchants to use highest level
                    for (Map.Entry<ArcaniaEnchant, Integer> entry: enchantBook1Map.entrySet()) {
                        enchantsToAdd.merge(entry.getKey(), entry.getValue(), (oldLevel, newLevel) -> {
                            if (oldLevel.equals(newLevel)) {
                                // Both levels are the same, so increment (up to max)
                                int max = entry.getKey().getMaxLevel();
                                return Math.min(oldLevel + 1, max);
                            } else {
                                // Levels are different, use the higher one
                                return Math.max(oldLevel, newLevel);
                            }
                        });
                    }

                    // sorts enchants by rarity, by name and by level
                    List<Map.Entry<ArcaniaEnchant, Integer>> sortedEnchants = new ArrayList<>(enchantsToAdd.entrySet());
                    sortedEnchants.sort(Comparator
                            .comparing((Map.Entry<ArcaniaEnchant, Integer> e) -> e.getKey().getRarity().getCost())
                            .thenComparing(e -> e.getKey().getName(), String.CASE_INSENSITIVE_ORDER));

                    List<String> lore = new ArrayList<>();
                    for (Map.Entry<ArcaniaEnchant, Integer> entry : sortedEnchants) {
                        ArcaniaEnchant enchant = entry.getKey();
                        int level = entry.getValue();
                        lore.add(ItemHelper.colorizeHex(String.format("%s%s %s",
                                enchant.getRarity().getColor(),
                                enchant.getName(),
                                ItemHelper.intToRoman(level)
                        )));
                    }
                    if (outputMeta != null) outputMeta.setLore(lore);
                    outputItem.setItemMeta(outputMeta);
                    return outputItem;
                }
                return null;
            }
        }
        return null;
    }
}