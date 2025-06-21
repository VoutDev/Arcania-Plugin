package me.vout.spigot.arcania.gui.enchanter;

import me.vout.core.arcania.gui.GuiHolder;
import me.vout.core.arcania.gui.GuiTypeEnum;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.nullness.qual.NonNull;

public class EnchanterMenuHolder implements GuiHolder {
    @Override
    public GuiTypeEnum getGuiType() {
        return GuiTypeEnum.ENCHANTER;
    }

    @Override
    @NonNull
    public Inventory getInventory() {
        return null;
    }
}
