package me.vout.spigot.arcania.gui.base;

import me.vout.spigot.arcania.gui.GuiTypeEnum;
import org.bukkit.inventory.InventoryHolder;

public interface GuiHolder extends InventoryHolder {
    GuiTypeEnum getGuiType();
}
