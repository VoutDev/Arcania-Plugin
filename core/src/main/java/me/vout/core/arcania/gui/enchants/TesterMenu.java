package me.vout.core.arcania.gui.enchants;

import me.vout.core.arcania.enums.EnchantsFilterEnum;
import me.vout.core.arcania.gui.GuiBuilder;
import me.vout.core.arcania.gui.GuiTypeEnum;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Deque;
import java.util.function.Function;

public class TesterMenu {
    public static Inventory build(Deque<GuiTypeEnum> guiHistory, EnchantsFilterEnum filter, Function<EnchantsFilterEnum, ItemStack[]> enchantsSupplier) {
        int size = 54;
        return new GuiBuilder(size,GuiTypeEnum.TESTER.getDisplayName(), new TesterMenuHolder())
                .setBorder(new ItemStack(Material.BLUE_STAINED_GLASS))
                .set(48, me.vout.core.arcania.gui.enchants.EnchantsMenu.getCommonItem(Material.LIGHT_GRAY_CONCRETE))
                .set(49, me.vout.core.arcania.gui.enchants.EnchantsMenu.getUncommonItem(Material.LIME_CONCRETE))
                .set(50, me.vout.core.arcania.gui.enchants.EnchantsMenu.getRareItem(Material.LIGHT_BLUE_CONCRETE))
                .set(51, me.vout.core.arcania.gui.enchants.EnchantsMenu.getLegendaryItem(Material.ORANGE_CONCRETE))
                .set(52, me.vout.core.arcania.gui.enchants.EnchantsMenu.getUltraItem(Material.MAGENTA_CONCRETE))
                .fillEmptySlotsWithItems(enchantsSupplier.apply(filter))
                .fillEmptySlots(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE))
                .addBackButton(guiHistory)
                .build();
    }
}
