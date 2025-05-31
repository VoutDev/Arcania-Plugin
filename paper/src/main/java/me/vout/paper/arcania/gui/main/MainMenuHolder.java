package me.vout.paper.arcania.gui.main;

import org.bukkit.inventory.Inventory;

import me.vout.paper.arcania.gui.GuiTypeEnum;
import me.vout.paper.arcania.gui.base.GuiHolder;

public class MainMenuHolder implements GuiHolder {

    @Override
    public Inventory getInventory() {
        return null; // Not used
    }

    @Override
    public GuiTypeEnum getGuiType() {
        return GuiTypeEnum.MAIN;
    }
}
