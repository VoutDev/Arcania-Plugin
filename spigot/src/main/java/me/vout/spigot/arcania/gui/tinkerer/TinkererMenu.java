package me.vout.spigot.arcania.gui.tinkerer;

import me.vout.spigot.arcania.gui.GuiBuilder;
import me.vout.spigot.arcania.gui.GuiTypeEnum;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Deque;

public class TinkererMenu {
    public static Inventory build(Deque<GuiTypeEnum> guiHistory) {
        return new GuiBuilder(GuiTypeEnum.TINKERER.getStaticSize(),GuiTypeEnum.TINKERER.getDisplayName(), new TinkererMenuHolder() )
                .setBorder(new ItemStack(Material.BLUE_STAINED_GLASS))
                .set(TinkererMenuHolder.INPUT_SLOT1, new ItemStack(Material.BARRIER))
                .set(TinkererMenuHolder.INPUT_SLOT2, new ItemStack(Material.BARRIER))
                .set(TinkererMenuHolder.OUTPUT_SLOT, new ItemStack(Material.BARRIER))
                .set(16, new ItemStack(Material.ANVIL))
                .fillEmptySlots(new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
                .set(TinkererMenuHolder.INPUT_SLOT1, new ItemStack(Material.AIR))
                .set(TinkererMenuHolder.INPUT_SLOT2, new ItemStack(Material.AIR))
                .set(TinkererMenuHolder.OUTPUT_SLOT, new ItemStack(Material.AIR))
                .addBackButton(guiHistory)
                .build();

        //return Bukkit.createInventory(new TinkererGuiHolder(GuiTypeEnum.TINKERER), InventoryType.ANVIL);
    }
}
