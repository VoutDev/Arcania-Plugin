package me.vout.core.arcania.gui.enchants;

import me.vout.core.arcania.enums.EnchantsFilterEnum;
import me.vout.core.arcania.gui.GuiHelperBase;
import me.vout.core.arcania.managers.GuiManagerBase;
import me.vout.core.arcania.providers.ArcaniaProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class TesterMenuHandler {
    public static void handler(InventoryClickEvent event, GuiManagerBase guiManager) {
        Inventory clickedInventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        assert clickedInventory != null;
        if (clickedInventory.getHolder() instanceof TesterMenuHolder) {
            event.setCancelled(true);
            ItemStack slotItem = event.getCurrentItem();
            if (slotItem == null || slotItem.getType().isAir()) return;
            EnchantsFilterEnum filter = GuiHelperBase.isEnchantsFilter(slotItem);
            if (filter != null) {
                Inventory enchantsFilterMenu = TesterMenu.build(guiManager.guiHistory.get(player.getUniqueId()), filter, ArcaniaProvider.getPlugin().getEnchantStrategy()::getAllEnchantBooksByLevel);
                player.openInventory(enchantsFilterMenu);
            }
            else {
                if (slotItem.getType() == Material.ENCHANTED_BOOK) {
                    Map<Integer, ItemStack> leftovers = player.getInventory().addItem(slotItem.clone());
                    if (!leftovers.isEmpty()) {
                        player.sendMessage(ChatColor.YELLOW + "Inventory is full");
                    }
                }
                else {
                    ArcaniaProvider.getPlugin().getJavaPlugin().getLogger().info("Not enchanted book");
                }
            }
        } // cancels shift clicking from users inventory
        else if (event.getClick().isShiftClick())
            event.setCancelled(true);
    }
}
