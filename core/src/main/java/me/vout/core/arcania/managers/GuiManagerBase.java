package me.vout.core.arcania.managers;

import me.vout.core.arcania.gui.GuiHolder;
import me.vout.core.arcania.gui.GuiTypeEnum;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public abstract class GuiManagerBase {
    public final Map<UUID, Deque<GuiTypeEnum>> guiHistory = new HashMap<>();

    public abstract Inventory getMenuInventory(GuiTypeEnum guiType, UUID uuid);
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

    public GuiTypeEnum getCurrentGuiType(Player player) {
        if (player.getOpenInventory().getTopInventory().getHolder() instanceof GuiHolder guiHolder)
            return guiHolder.getGuiType();
        return null;
    }
}
