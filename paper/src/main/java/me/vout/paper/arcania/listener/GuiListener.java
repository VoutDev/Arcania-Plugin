package me.vout.paper.arcania.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

import me.vout.paper.arcania.Arcania;
import me.vout.paper.arcania.gui.GuiHelper;
import me.vout.paper.arcania.gui.base.GuiHolder;
import me.vout.paper.arcania.gui.main.MainMenuHandler;
import me.vout.paper.arcania.gui.main.MainMenuHolder;
import me.vout.paper.arcania.manager.GuiManager;

public class GuiListener implements Listener {

    private final GuiManager guiManager;
    public GuiListener(GuiManager guiManager) {
        this.guiManager = guiManager;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check if this is your custom GUI (using your isCustomGui method)
        // Handle slot clicks, button presses, etc.
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getCurrentItem() == null && event.getCursor() == null) return;

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;

        // if (Arcania.getConfigManager().isFixEnchantCheckEnabled()) {
        //     if (EnchantHelper.needsUpdate(event.getCurrentItem() == null ? event.getCursor() : event.getCurrentItem()))
        //         Arcania.getInstance().getLogger().info(String.format(String.format("Updated item for %s", player.getDisplayName())));
        // }

        InventoryHolder topInventoryHolder = event.getView().getTopInventory().getHolder();

        if (topInventoryHolder instanceof GuiHolder && event.getCurrentItem() != null && GuiHelper.isBackButton(event.getCurrentItem())) {
            event.setCancelled(true);
            guiManager.openGui(player, guiManager.guiHistory.get(player.getUniqueId()).peek());
        }
        else if (topInventoryHolder instanceof MainMenuHolder) {
            if (event.getClick() == ClickType.DOUBLE_CLICK) event.setCancelled(true);
            MainMenuHandler.handler(event, guiManager);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        Bukkit.getScheduler().runTaskLater(Arcania.getInstance(), () -> {
            InventoryView view = player.getOpenInventory();
            Inventory top = view.getTopInventory();
            InventoryHolder newHolder = top.getHolder();

            // If the player is NOT viewing one of your custom GUIs, clear history
            // this checks the next opened inventory, if there is one (occurs when traversing custom GUIs
            if (!(newHolder instanceof GuiHolder)) {
                guiManager.guiHistory.remove(player.getUniqueId());
            }
        }, 1L); // 1 tick delay to allow new inventory to be opened
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        guiManager.guiHistory.remove(event.getPlayer().getUniqueId());
    }
}