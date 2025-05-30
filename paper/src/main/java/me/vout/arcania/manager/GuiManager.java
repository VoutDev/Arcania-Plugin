package me.vout.arcania.manager;

import me.vout.arcania.gui.*;
import me.vout.arcania.gui.base.GuiHolder;
import me.vout.arcania.gui.disenchanter.DisenchanterMenu;
import me.vout.arcania.gui.enchanter.EnchanterMenu;
import me.vout.arcania.gui.enchants.EnchantsFilterEnum;
import me.vout.arcania.gui.enchants.EnchantsMenu;
import me.vout.arcania.gui.main.MainMenu;
import me.vout.arcania.gui.tinkerer.TinkererMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class GuiManager {

    public final Map<UUID, Deque<GuiTypeEnum>> guiHistory = new HashMap<>();

    public void openGui(Player player, GuiTypeEnum type) {
        // Push current GUI type to history
        Deque<GuiTypeEnum> history = guiHistory.computeIfAbsent(
                player.getUniqueId(), k -> new ArrayDeque<>()
        );
        GuiTypeEnum current = getCurrentGuiType(player); // Implement this as needed

        if (current != null) {
            history.push(current);
        }
        // Build and open the new GUI

        Inventory gui = getMenuInventory(type, player.getUniqueId());
        player.openInventory(gui);
    }

    public Inventory getMenuInventory(GuiTypeEnum guiType, UUID uuid) {
        return switch (guiType) {
            case MAIN -> MainMenu.build();
            case TINKERER -> TinkererMenu.build(guiHistory.get(uuid));
            case DISENCHANTER -> DisenchanterMenu.build(guiHistory.get(uuid));
            case ENCHANTER -> EnchanterMenu.build(guiHistory.get(uuid));
            case ENCHANTS -> EnchantsMenu.build(guiHistory.get(uuid), EnchantsFilterEnum.ALL);
        };
    }

    public GuiTypeEnum getCurrentGuiType(Player player) {
        if (player.getOpenInventory().getTopInventory().getHolder() instanceof GuiHolder guiHolder)
            return guiHolder.getGuiType();
        return null;
    }
}