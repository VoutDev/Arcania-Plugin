package me.vout.spigot.arcania.listener;

import me.vout.spigot.arcania.Arcania;
import me.vout.spigot.arcania.gui.GuiHelper;
import me.vout.spigot.arcania.gui.base.GuiHolder;
import me.vout.spigot.arcania.gui.disenchanter.DisenchanterMenuHandler;
import me.vout.spigot.arcania.gui.disenchanter.DisenchanterMenuHolder;
import me.vout.spigot.arcania.gui.enchanter.EnchanterMenuHandler;
import me.vout.spigot.arcania.gui.enchanter.EnchanterMenuHolder;
import me.vout.spigot.arcania.gui.enchants.EnchantsMenuHandler;
import me.vout.spigot.arcania.gui.enchants.EnchantsMenuHolder;
import me.vout.spigot.arcania.gui.main.MainMenuHandler;
import me.vout.spigot.arcania.gui.main.MainMenuHolder;
import me.vout.spigot.arcania.gui.tinkerer.TinkererMenuHandler;
import me.vout.spigot.arcania.gui.tinkerer.TinkererMenuHolder;
import me.vout.spigot.arcania.manager.GuiManager;
import me.vout.spigot.arcania.util.EnchantHelper;
import me.vout.spigot.arcania.util.InventoryHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;

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

        if (Arcania.getConfigManager().isFixEnchantCheckEnabled()) {
            if (EnchantHelper.needsUpdate(event.getCurrentItem() == null ? event.getCursor() : event.getCurrentItem()))
                Arcania.getInstance().getLogger().info(String.format(String.format("Updated item for %s", player.getDisplayName())));
        }

        InventoryHolder topInventoryHolder = event.getView().getTopInventory().getHolder();

        if (topInventoryHolder instanceof GuiHolder && event.getCurrentItem() != null && GuiHelper.isBackButton(event.getCurrentItem())) {
            event.setCancelled(true);
            guiManager.openGui(player, guiManager.guiHistory.get(player.getUniqueId()).peek());
        }
        else if (topInventoryHolder instanceof MainMenuHolder) {
            if (event.getClick() == ClickType.DOUBLE_CLICK) event.setCancelled(true);
            MainMenuHandler.handler(event, guiManager);
        }
        else if (topInventoryHolder instanceof EnchanterMenuHolder) {
            if (event.getClick() == ClickType.DOUBLE_CLICK) event.setCancelled(true);
            EnchanterMenuHandler.handler(event);
        }
        else if (topInventoryHolder instanceof DisenchanterMenuHolder) {
            if (event.getClick() == ClickType.DOUBLE_CLICK) event.setCancelled(true);
            DisenchanterMenuHandler.handler(event);
        }
        else if (topInventoryHolder instanceof TinkererMenuHolder) {
            if (event.getClick() == ClickType.DOUBLE_CLICK) event.setCancelled(true);
            TinkererMenuHandler.handler(event);
        }
        else if (topInventoryHolder instanceof EnchantsMenuHolder) {
            if (event.getClick() == ClickType.DOUBLE_CLICK) event.setCancelled(true);
            EnchantsMenuHandler.handler(event, guiManager);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof TinkererMenuHolder) {
            List<ItemStack> inputs = new ArrayList<>();
            List<Integer> slots = List.of(TinkererMenuHolder.INPUT_SLOT1,
                    TinkererMenuHolder.INPUT_SLOT2);
            for (int slot: slots) {
                ItemStack item = event.getInventory().getItem(slot);
                if (item != null && !item.getType().isAir())
                    inputs.add(item);
            }
            InventoryHelper.giveOrDrop(player, inputs.toArray(new ItemStack[0]));
        }

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