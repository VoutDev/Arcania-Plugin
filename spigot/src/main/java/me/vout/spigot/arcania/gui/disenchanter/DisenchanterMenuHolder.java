package me.vout.spigot.arcania.gui.disenchanter;

import me.vout.spigot.arcania.gui.GuiTypeEnum;
import me.vout.spigot.arcania.gui.base.GuiHolder;
import org.bukkit.inventory.Inventory;

public class DisenchanterMenuHolder  implements GuiHolder {

    public static  final int INPUT_SLOT = 11;
    public static final int OUTPUT_SLOT = 15;
    @Override
    public GuiTypeEnum getGuiType() {
        return GuiTypeEnum.DISENCHANTER;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
