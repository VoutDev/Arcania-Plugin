package me.vout.arcania.gui.enchanter;

import me.vout.arcania.Arcania;
import me.vout.arcania.enchant.ArcaniaEnchant;
import me.vout.arcania.enchant.EnchantExtraEnum;
import me.vout.arcania.enchant.EnchantRarityEnum;
import me.vout.arcania.gui.GuiHelper;
import me.vout.arcania.util.EnchantHelper;
import me.vout.arcania.util.InventoryHelper;
import me.vout.arcania.util.ItemHelper;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class EnchanterMenuHandler {
    public static void handler(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack slotItem = event.getCurrentItem();
        assert clickedInventory != null;
        if (clickedInventory.getHolder() instanceof EnchanterMenuHolder) {
            event.setCancelled(true);
            if (slotItem == null) return;

            EnchanterActionsEnum action = GuiHelper.isEnchanterAction(slotItem);
            if (action != null) {
                int playersLevels = player.getLevel();
                if (playersLevels >= action.getRarity().getCost()) {
                    player.setLevel(playersLevels - action.getRarity().getCost());
                    ArcaniaEnchant enchant = getEnchant(action.getRarity(), false);
                    int level = getLevel(enchant);
                    InventoryHelper.giveOrDrop(player, EnchantHelper.getEnchantBook(enchant, level, true));
                    player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.5f, 1.0f);
                } else {
                    ItemStack errorItem = new ItemStack(Material.RED_WOOL);
                    ItemMeta meta = errorItem.getItemMeta();
                    meta.setDisplayName(ItemHelper.colorizeHex(EnchantExtraEnum.ERROR_MESSAGE.getColor() + "Not enough levels!"));
                    slotItem.setType(Material.RED_WOOL);
                    slotItem.setItemMeta(meta);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5f, 0.8f);
                }
            }
        } // cancels shift clicking from users inventory
        else if (event.getClick().isShiftClick())
            event.setCancelled(true);
    }

    private static ArcaniaEnchant getEnchant(EnchantRarityEnum rarity, boolean includeAll) {
        List<ArcaniaEnchant> enchants;
        List<ArcaniaEnchant> baseList = includeAll ? Arcania.getEnchantManager().getEnchants() : Arcania.getEnchantManager().getEnchants().stream()
                .filter(e -> e.getRarity().equals(rarity)).toList();

        if (includeAll)
            enchants = baseList.stream()
                    .filter(e -> Math.random() < e.getChance())
                    .collect(Collectors.toList());
        else
            enchants = baseList.stream()
                    .filter(e -> e.getRarity().equals(rarity) && (Math.random() < e.getChance()))
                    .collect(Collectors.toList());

        List<ArcaniaEnchant> pool = enchants.isEmpty() ? baseList : enchants;

        int totalWeight = pool.stream().mapToInt(ArcaniaEnchant::getWeight).sum();
        if (totalWeight == 0) {
            return pool.get(new Random().nextInt(pool.size()));
        }

        int r = new Random().nextInt(totalWeight);
        int cumulative = 0;
        for (ArcaniaEnchant enchant : enchants) {
            cumulative += enchant.getWeight();
            if (r < cumulative) {
                return enchant;
            }
        }
        return pool.get(0);
    }

    private static int getLevel(ArcaniaEnchant enchant) {
        if (enchant.getMaxLevel() == 1) return 1;
        return ThreadLocalRandom.current().nextInt(1, enchant.getMaxLevel() + 1);
    }
}