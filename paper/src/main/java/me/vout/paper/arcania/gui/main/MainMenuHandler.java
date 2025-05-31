package me.vout.paper.arcania.gui.main;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.vout.paper.arcania.gui.GuiHelper;
import me.vout.paper.arcania.gui.GuiTypeEnum;
import me.vout.paper.arcania.manager.GuiManager;

public class MainMenuHandler {

    public static void handler(InventoryClickEvent event, GuiManager guiManager) {
        Inventory clickedInventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        assert clickedInventory != null;
        if (clickedInventory.getHolder() instanceof MainMenuHolder) {
            event.setCancelled(true);
            ItemStack slotItem = event.getCurrentItem();
            if (slotItem == null || slotItem.getType().isAir()) return;
            GuiTypeEnum guiType = GuiHelper.isGuiRedirect(slotItem);
            if (guiType != null) {
                guiManager.openGui(player, guiType);
            }
        } // cancels shift clicking from users inventory
        else if (event.getClick().isShiftClick())
            event.setCancelled(true);
    }
}
