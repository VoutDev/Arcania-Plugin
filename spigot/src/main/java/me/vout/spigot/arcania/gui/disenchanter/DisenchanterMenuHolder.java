package me.vout.spigot.arcania.gui.disenchanter;

import me.vout.core.arcania.gui.GuiHolder;
import me.vout.core.arcania.gui.GuiTypeEnum;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.nullness.qual.NonNull;

public class DisenchanterMenuHolder  implements GuiHolder {

    //todo Add a second input that can be a book, then have it take top enchant and add to book
    public static  final int INPUT_SLOT = 11;
    public static final int OUTPUT_SLOT = 15;
    @Override
    public GuiTypeEnum getGuiType() {
        return GuiTypeEnum.DISENCHANTER;
    }

    @Override
    @NonNull
    public Inventory getInventory() {
        return null;
    }
}
