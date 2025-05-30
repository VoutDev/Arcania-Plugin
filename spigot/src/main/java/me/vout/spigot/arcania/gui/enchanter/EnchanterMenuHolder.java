package me.vout.spigot.arcania.gui.enchanter;

import me.vout.spigot.arcania.gui.GuiTypeEnum;
import me.vout.spigot.arcania.gui.base.GuiHolder;
import org.bukkit.inventory.Inventory;

public class EnchanterMenuHolder implements GuiHolder {
    @Override
    public GuiTypeEnum getGuiType() {
        return GuiTypeEnum.ENCHANTER;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
