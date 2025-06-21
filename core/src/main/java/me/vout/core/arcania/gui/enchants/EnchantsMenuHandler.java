package me.vout.core.arcania.gui.enchants;

import me.vout.core.arcania.enums.EnchantsFilterEnum;
import me.vout.core.arcania.gui.GuiHelperBase;
import me.vout.core.arcania.managers.GuiManagerBase;
import me.vout.core.arcania.providers.ArcaniaProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EnchantsMenuHandler {
    public static void handler(InventoryClickEvent event, GuiManagerBase guiManager) {
        Inventory clickedInventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        assert clickedInventory != null;
        if (clickedInventory.getHolder() instanceof EnchantsMenuHolder) {
            event.setCancelled(true);
            ItemStack slotItem = event.getCurrentItem();
            if (slotItem == null || slotItem.getType().isAir()) return;
            EnchantsFilterEnum filter = GuiHelperBase.isEnchantsFilter(slotItem);
            if (filter != null) {
                Inventory enchantsFilterMenu = EnchantsMenu.build(guiManager.guiHistory.get(player.getUniqueId()), filter, ArcaniaProvider.getPlugin().getEnchantStrategy()::getEnchants);
                player.openInventory(enchantsFilterMenu);
            }
        } // cancels shift clicking from users inventory
        else if (event.getClick().isShiftClick())
            event.setCancelled(true);
    }
}
