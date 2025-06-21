package me.vout.core.arcania.gui.main;

import me.vout.core.arcania.gui.GuiHolder;
import me.vout.core.arcania.gui.GuiTypeEnum;
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
