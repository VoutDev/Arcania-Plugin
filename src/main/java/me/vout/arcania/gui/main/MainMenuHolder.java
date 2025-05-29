package me.vout.arcania.gui.main;

import me.vout.arcania.gui.GuiTypeEnum;
import me.vout.arcania.gui.base.GuiHolder;
import org.bukkit.inventory.Inventory;

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
