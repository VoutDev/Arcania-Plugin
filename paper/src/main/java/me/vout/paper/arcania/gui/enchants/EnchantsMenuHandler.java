package me.vout.paper.arcania.gui.enchants;

import me.vout.paper.arcania.gui.GuiHelper;
import me.vout.paper.arcania.manager.GuiManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EnchantsMenuHandler {
    public static void handler(InventoryClickEvent event, GuiManager guiManager) {
        Inventory clickedInventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        assert clickedInventory != null;
        if (clickedInventory.getHolder() instanceof EnchantsMenuHolder) {
            event.setCancelled(true);
            ItemStack slotItem = event.getCurrentItem();
            if (slotItem == null || slotItem.getType().isAir()) return;
            EnchantsFilterEnum filter = GuiHelper.isEnchantsFilter(slotItem);
            if (filter != null) {
                Inventory enchantsFilterMenu = EnchantsMenu.build(guiManager.guiHistory.get(player.getUniqueId()), filter);
                player.openInventory(enchantsFilterMenu);
            }
        } // cancels shift clicking from users inventory
        else if (event.getClick().isShiftClick())
            event.setCancelled(true);
    }
}
