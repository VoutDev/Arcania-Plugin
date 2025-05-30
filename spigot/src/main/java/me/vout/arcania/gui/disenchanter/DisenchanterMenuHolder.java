package me.vout.arcania.gui.disenchanter;

import me.vout.arcania.gui.GuiTypeEnum;
import me.vout.arcania.gui.base.GuiHolder;
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
