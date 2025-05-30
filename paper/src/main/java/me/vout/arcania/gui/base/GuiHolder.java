package me.vout.arcania.gui.base;

import me.vout.arcania.gui.GuiTypeEnum;
import org.bukkit.inventory.InventoryHolder;

public interface GuiHolder extends InventoryHolder {
    GuiTypeEnum getGuiType();
}
