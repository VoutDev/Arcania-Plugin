package me.vout.paper.arcania.gui.base;

import org.bukkit.inventory.InventoryHolder;

import me.vout.paper.arcania.gui.GuiTypeEnum;

public interface GuiHolder extends InventoryHolder {
    GuiTypeEnum getGuiType();
}
