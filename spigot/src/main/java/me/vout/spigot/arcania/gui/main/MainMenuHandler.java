package me.vout.spigot.arcania.gui.main;

import me.vout.spigot.arcania.gui.GuiHelper;
import me.vout.spigot.arcania.gui.GuiTypeEnum;
import me.vout.spigot.arcania.manager.GuiManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
