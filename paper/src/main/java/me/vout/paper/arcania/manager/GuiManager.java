package me.vout.paper.arcania.manager;

import me.vout.paper.arcania.gui.*;
import me.vout.paper.arcania.gui.base.GuiHolder;
import me.vout.paper.arcania.gui.enchants.EnchantsFilterEnum;
import me.vout.paper.arcania.gui.enchants.EnchantsMenu;
import me.vout.paper.arcania.gui.main.MainMenu;
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
            case ENCHANTS -> EnchantsMenu.build(guiHistory.get(uuid), EnchantsFilterEnum.ALL);
        };
    }

    public GuiTypeEnum getCurrentGuiType(Player player) {
        if (player.getOpenInventory().getTopInventory().getHolder() instanceof GuiHolder guiHolder)
            return guiHolder.getGuiType();
        return null;
    }
}