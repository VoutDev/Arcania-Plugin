package me.vout.spigot.arcania.gui.tinkerer;

import de.tr7zw.nbtapi.NBT;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.util.EnchantHelper;
import me.vout.spigot.arcania.util.ItemHelper;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
        if ((me.vout.core.arcania.util.ItemHelper.isWearableArmor(input1.getType()) || me.vout.core.arcania.util.ItemHelper.isMainHandTool(input1.getType())) &&
        input2.getType() == Material.ENCHANTED_BOOK) {
            if (ItemHelper.isArcaniaEnchant(input2)) {
                Map<NamespacedKey, Integer> enchantItemMap = EnchantHelper.getItemEnchants(input1);
                Map<NamespacedKey, Integer> enchantBookMap = EnchantHelper.getItemEnchants(input2);

                Map<ArcaniaEnchant, Integer> enchantsToAdd = new HashMap<>();

                ItemStack outputItem = input1.clone();
                boolean canApplyAny = false;

                for (NamespacedKey key: enchantBookMap.keySet()) {
                    ArcaniaEnchant enchant = EnchantHelper.namespaceToEnchant(key);
                    if (!enchant.canApplyTo(input1.getType()) ||
                            enchantItemMap.keySet().stream().map(EnchantHelper::namespaceToEnchant).anyMatch(e -> !e.canApplyWith(enchant))) continue;
                    canApplyAny = true;
                    enchantsToAdd.put(enchant, enchantBookMap.get(key));
                }

                if (canApplyAny) {
                    // Merges duplicate enchants to use highest level
                    for (Map.Entry<NamespacedKey, Integer> entry: enchantItemMap.entrySet()) {
                        ArcaniaEnchant enchant = EnchantHelper.namespaceToEnchant(entry.getKey());
                        enchantsToAdd.merge(enchant, entry.getValue(), (oldLevel, newLevel) -> {
                            if (oldLevel.equals(newLevel)) {
                                // Both levels are the same, so increment (up to max)
                                int max = enchant.getMaxLevel();
                                return Math.min(oldLevel + 1, max);
                            } else {
                                // Levels are different, use the higher one
                                return Math.max(oldLevel, newLevel);
                            }
                        });
                    }
                    return setOutputData(outputItem, enchantsToAdd);
                }
                return null; //input1.clone(); maybe enchant is a clone?
            }
        } // 2 valid non enchanted book items
        else if (input1.getType().equals(input2.getType()) &&
                (me.vout.core.arcania.util.ItemHelper.isWearableArmor(input1.getType()) || me.vout.core.arcania.util.ItemHelper.isMainHandTool(input1.getType())) &&
                (me.vout.core.arcania.util.ItemHelper.isWearableArmor(input2.getType()) || me.vout.core.arcania.util.ItemHelper.isMainHandTool(input2.getType()))) {
            Map<NamespacedKey, Integer> enchantItemMap = EnchantHelper.getItemEnchants(input1);
            Map<NamespacedKey, Integer> enchantItem2Map = EnchantHelper.getItemEnchants(input2);
            if (enchantItemMap.isEmpty() && enchantItem2Map.isEmpty())  return null;

            Map<ArcaniaEnchant, Integer> enchantsToAdd = new HashMap<>();
            ItemStack outputItem = input1.clone();

            for (NamespacedKey key: enchantItemMap.keySet()) {
                ArcaniaEnchant enchant = EnchantHelper.namespaceToEnchant(key);
                enchantsToAdd.put(enchant, enchantItemMap.get(key));
            }

            for (Map.Entry<NamespacedKey, Integer> entry: enchantItem2Map.entrySet()) {
                ArcaniaEnchant enchant = EnchantHelper.namespaceToEnchant(entry.getKey());
                enchantsToAdd.merge(enchant, entry.getValue(), (oldLevel, newLevel) -> {
                    if (oldLevel.equals(newLevel)) {
                        // Both levels are the same, so increment (up to max)
                        int max = enchant.getMaxLevel();
                        return Math.min(oldLevel + 1, max);
                    } else {
                        // Levels are different, use the higher one
                        return Math.max(oldLevel, newLevel);
                    }
                });
            }

            if (enchantsToAdd.isEmpty()) return null;
            return setOutputData(outputItem, enchantsToAdd);
        }
        else if (input1.getType() == Material.ENCHANTED_BOOK &&
                input2.getType() == Material.ENCHANTED_BOOK) {
            if (ItemHelper.isArcaniaEnchant(input1) && ItemHelper.isArcaniaEnchant(input2)) {
                Map<NamespacedKey, Integer> enchantBook1Map = EnchantHelper.getItemEnchants(input1);
                Map<NamespacedKey, Integer> enchantBook2Map = EnchantHelper.getItemEnchants(input2);
                Map<ArcaniaEnchant, Integer> enchantsToAdd = new HashMap<>();

                ItemStack outputItem = input1.clone();
                boolean canApplyAny = false;

                for (NamespacedKey key: enchantBook2Map.keySet()) {
                    ArcaniaEnchant enchant = EnchantHelper.namespaceToEnchant(key);
                    if (enchantBook1Map.keySet().stream().map(EnchantHelper::namespaceToEnchant).anyMatch(e -> !e.canApplyWith(enchant))) continue;

                    canApplyAny = true;
                    enchantsToAdd.put(enchant, enchantBook2Map.get(key));
                }

                if (canApplyAny) {
                    // Merges duplicate enchants to use highest level
                    for (Map.Entry<NamespacedKey, Integer> entry: enchantBook1Map.entrySet()) {
                        ArcaniaEnchant enchant = EnchantHelper.namespaceToEnchant(entry.getKey());
                        enchantsToAdd.merge(enchant, entry.getValue(), (oldLevel, newLevel) -> {
                            if (oldLevel.equals(newLevel)) {
                                // Both levels are the same, so increment (up to max)
                                int max = enchant.getMaxLevel();
                                return Math.min(oldLevel + 1, max);
                            } else {
                                // Levels are different, use the higher one
                                return Math.max(oldLevel, newLevel);
                            }
                        });
                    }
                    return setOutputData(outputItem, enchantsToAdd);
                }
            }
        }
        return null;
    }

    private static ItemStack setOutputData(ItemStack outputItem, Map<ArcaniaEnchant, Integer> enchantsToAdd) {
        // sorts enchants by rarity, by name and by level
        List<Map.Entry<ArcaniaEnchant, Integer>> sortedEnchants = new ArrayList<>(enchantsToAdd.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue()))
                .toList());
        sortedEnchants.sort(Comparator
                .comparing((Map.Entry<ArcaniaEnchant, Integer> e) -> e.getKey().getRarity().getNumericValue())
                .thenComparing(e -> e.getKey().getName(), String.CASE_INSENSITIVE_ORDER));

        NBT.modify(outputItem, nbt -> {
            //Get or create the nbt base path we want
            var nbtCompound = nbt.getOrCreateCompound("arcania").getOrCreateCompound("enchants");
            //wipe the existing enchant area clean

            Set<String> keys = nbtCompound.getKeys();
            for (String key : keys) {
                nbtCompound.removeKey(key);
            }

            // Store the new enchant data, going through all existing enchants and storing
            for (Map.Entry<ArcaniaEnchant, Integer> entry : sortedEnchants) {
                ArcaniaEnchant enchant = entry.getKey();
                int level = entry.getValue();
                nbtCompound.setString(enchant.KEY.getKey(), String.valueOf(level));
            }
        });

        // grabs the meta data including new nbt data
        ItemMeta outputMeta = outputItem.getItemMeta();

        // Now we add the display data
        List<String> lore = new ArrayList<>();
        for (Map.Entry<ArcaniaEnchant, Integer> entry : sortedEnchants) {
            ArcaniaEnchant enchant = entry.getKey();
            int level = entry.getValue();
            lore.add(me.vout.core.arcania.util.ItemHelper.colorizeHex(String.format("%s%s %s",
                    enchant.getRarity().getColor(),
                    enchant.getName(),
                    ItemHelper.intToRoman(level)
            )));
        }

        if (outputMeta != null) {
            outputMeta.setLore(lore);
            outputItem.setItemMeta(outputMeta);
        }
        return outputItem;
    }
}