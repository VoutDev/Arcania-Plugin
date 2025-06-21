package me.vout.paper.arcania.listener;

import me.vout.core.arcania.gui.GuiHelperBase;
import me.vout.core.arcania.gui.GuiHolder;
import me.vout.core.arcania.gui.enchants.EnchantsMenuHandler;
import me.vout.core.arcania.gui.enchants.EnchantsMenuHolder;
import me.vout.core.arcania.gui.enchants.TesterMenuHandler;
import me.vout.core.arcania.gui.enchants.TesterMenuHolder;
import me.vout.core.arcania.gui.main.MainMenuHandler;
import me.vout.core.arcania.gui.main.MainMenuHolder;
import me.vout.core.arcania.providers.ArcaniaProvider;
import me.vout.paper.arcania.manager.GuiManager;
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

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;

        InventoryHolder topInventoryHolder = event.getView().getTopInventory().getHolder();

        if (topInventoryHolder instanceof GuiHolder && event.getCurrentItem() != null && GuiHelperBase.isBackButton(event.getCurrentItem())) {
            event.setCancelled(true);
            guiManager.openGui(player, guiManager.guiHistory.get(player.getUniqueId()).peek());
        }
        else if (topInventoryHolder instanceof MainMenuHolder) {
            if (event.getClick() == ClickType.DOUBLE_CLICK) event.setCancelled(true);
            MainMenuHandler.handler(event, guiManager);
        }
        else if (topInventoryHolder instanceof EnchantsMenuHolder) {
            if (event.getClick() == ClickType.DOUBLE_CLICK) event.setCancelled(true);
            EnchantsMenuHandler.handler(event, guiManager);
        }
        else if (topInventoryHolder instanceof TesterMenuHolder) {
            if (event.getClick() == ClickType.DOUBLE_CLICK) event.setCancelled(true);
            TesterMenuHandler.handler(event, guiManager);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        Bukkit.getScheduler().runTaskLater(ArcaniaProvider.getPlugin().getJavaPlugin(), () -> {
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