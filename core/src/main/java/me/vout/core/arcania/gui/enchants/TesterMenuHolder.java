package me.vout.core.arcania.gui.enchants;

import me.vout.core.arcania.gui.GuiHolder;
import me.vout.core.arcania.gui.GuiTypeEnum;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.nullness.qual.NonNull;

public class TesterMenuHolder implements GuiHolder {
    @Override
    public GuiTypeEnum getGuiType() {
        return GuiTypeEnum.TESTER;
    }

    @Override
    @NonNull
    public Inventory getInventory() {
        return null;
    }
}