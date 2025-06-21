package me.vout.spigot.arcania.gui.tinkerer;

import me.vout.core.arcania.gui.GuiHolder;
import me.vout.core.arcania.gui.GuiTypeEnum;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.nullness.qual.NonNull;

public class TinkererMenuHolder implements GuiHolder {
    public static int INPUT_SLOT1 = 11;
    public static int INPUT_SLOT2 = 13;
    public static int OUTPUT_SLOT = 15;
    @Override
    public GuiTypeEnum getGuiType() {
        return GuiTypeEnum.TINKERER;
    }

    @Override
    @NonNull
    public Inventory getInventory() {
        return null;
    }
}
