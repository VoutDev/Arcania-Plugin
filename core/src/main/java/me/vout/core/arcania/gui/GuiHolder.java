package me.vout.core.arcania.gui;

import org.bukkit.inventory.InventoryHolder;

public interface GuiHolder extends InventoryHolder {
    GuiTypeEnum getGuiType();
}
