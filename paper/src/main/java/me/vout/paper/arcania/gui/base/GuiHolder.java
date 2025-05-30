package me.vout.paper.arcania.gui.base;

import me.vout.paper.arcania.gui.GuiTypeEnum;
import org.bukkit.inventory.InventoryHolder;

public interface GuiHolder extends InventoryHolder {
    GuiTypeEnum getGuiType();
}
