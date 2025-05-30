package me.vout.spigot.arcania.gui.main;

import me.vout.spigot.arcania.gui.GuiTypeEnum;
import me.vout.spigot.arcania.gui.base.GuiHolder;
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
