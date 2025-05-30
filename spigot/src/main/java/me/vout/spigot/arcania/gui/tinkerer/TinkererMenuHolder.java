package me.vout.spigot.arcania.gui.tinkerer;

import me.vout.spigot.arcania.gui.GuiTypeEnum;
import me.vout.spigot.arcania.gui.base.GuiHolder;
import org.bukkit.inventory.Inventory;

public class TinkererMenuHolder implements GuiHolder {
    public static int INPUT_SLOT1 = 11;
    public static int INPUT_SLOT2 = 13;
    public static int OUTPUT_SLOT = 15;
    @Override
    public GuiTypeEnum getGuiType() {
        return GuiTypeEnum.TINKERER;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
