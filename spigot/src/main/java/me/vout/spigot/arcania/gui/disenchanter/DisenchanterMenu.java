package me.vout.spigot.arcania.gui.disenchanter;

import me.vout.spigot.arcania.gui.GuiBuilder;
import me.vout.spigot.arcania.gui.GuiTypeEnum;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Deque;

public class DisenchanterMenu {
    public static Inventory build(Deque<GuiTypeEnum> guiHistory) {
        return new GuiBuilder(GuiTypeEnum.DISENCHANTER.getStaticSize(),GuiTypeEnum.DISENCHANTER.getDisplayName(), new DisenchanterMenuHolder())
                .setBorder(new ItemStack(Material.BLUE_STAINED_GLASS))
                .set(DisenchanterMenuHolder.INPUT_SLOT, new ItemStack(Material.BARRIER))
                .set(DisenchanterMenuHolder.OUTPUT_SLOT, new ItemStack(Material.BARRIER))
                .set(16, new ItemStack(Material.GRINDSTONE))
                .fillEmptySlots(new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
                .set(DisenchanterMenuHolder.INPUT_SLOT, new ItemStack(Material.AIR))
                .set(DisenchanterMenuHolder.OUTPUT_SLOT, new ItemStack(Material.AIR))
                .addBackButton(guiHistory)
                .build();
    }
}