package me.vout.paper.arcania.gui.enchants;

import me.vout.paper.arcania.gui.GuiTypeEnum;
import me.vout.paper.arcania.gui.base.GuiHolder;
import org.bukkit.inventory.Inventory;

public class EnchantsMenuHolder implements GuiHolder {
    @Override
    public GuiTypeEnum getGuiType() {
        return GuiTypeEnum.ENCHANTS;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
