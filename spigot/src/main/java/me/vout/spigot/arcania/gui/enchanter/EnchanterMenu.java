package me.vout.spigot.arcania.gui.enchanter;

import me.vout.core.arcania.enums.EnchantExtraEnum;
import me.vout.core.arcania.gui.GuiBuilder;
import me.vout.core.arcania.gui.GuiTypeEnum;
import me.vout.core.arcania.gui.PersistentDataEnum;
import me.vout.core.arcania.util.ItemHelper;
import me.vout.spigot.arcania.Arcania;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;
import me.vout.spigot.arcania.gui.GuiHelper;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Deque;
import java.util.List;

public class EnchanterMenu {

    public static Inventory build(Deque<GuiTypeEnum> guiHistory) {
        return new GuiBuilder(GuiTypeEnum.ENCHANTER.getStaticSize(), GuiTypeEnum.ENCHANTER.getDisplayName(), new EnchanterMenuHolder())
                .setBorder(new ItemStack(Material.BLUE_STAINED_GLASS))
                .set(20, getCommonItem(Material.LIGHT_GRAY_CONCRETE))
                .set(22, getRareItem(Material.LIGHT_BLUE_CONCRETE))
                .set(24, getUltraItem(Material.MAGENTA_CONCRETE))
                .set(30, getUncommonItem(Material.LIME_CONCRETE))
                .set(32, getLegendaryItem(Material.ORANGE_CONCRETE))
                .fillEmptySlots(new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
                .addBackButton(guiHistory)
                .build();
    }

    private static ItemStack emptyRarityPlaceholder(EnchantRarityEnum rarity) {
        ItemStack item = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ItemHelper.colorizeHex(rarity.getColor() + rarity.getDisplayName()));
        meta.setLore(List.of(ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor() + "No enchants at this rarity.")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }
    private static ItemStack getCommonItem(Material material) {
        if (Arcania.getEnchantManager().getEnchants().stream().noneMatch(e -> e.getRarity() == EnchantRarityEnum.COMMON))
            return emptyRarityPlaceholder(EnchantRarityEnum.COMMON);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ItemHelper.colorizeHex(EnchantRarityEnum.COMMON.getColor() + EnchantRarityEnum.COMMON.getDisplayName()));
        meta.setLore(List.of(ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor() + String.format("Roll %s Enchants", EnchantRarityEnum.COMMON.getDisplayName()))
                ,ItemHelper.colorizeHex(EnchantExtraEnum.LEVEL_COST.getColor() + String.format("Cost %d levels",EnchantRarityEnum.COMMON.getNumericValue()))));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelper.setPersistentData(PersistentDataEnum.GUI_ACTION.toString().toLowerCase(), EnchanterActionsEnum.ROLL_COMMON.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getUncommonItem(Material material) {
        if (Arcania.getEnchantManager().getEnchants().stream().noneMatch(e -> e.getRarity() == EnchantRarityEnum.UNCOMMON))
            return emptyRarityPlaceholder(EnchantRarityEnum.UNCOMMON);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ItemHelper.colorizeHex(EnchantRarityEnum.UNCOMMON.getColor() + EnchantRarityEnum.UNCOMMON.getDisplayName()));
        meta.setLore(List.of(ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor() + String.format("Roll %s Enchants", EnchantRarityEnum.UNCOMMON.getDisplayName()))
                ,ItemHelper.colorizeHex(EnchantExtraEnum.LEVEL_COST.getColor() + String.format("Cost %d levels",EnchantRarityEnum.UNCOMMON.getNumericValue()))));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelper.setPersistentData(PersistentDataEnum.GUI_ACTION.toString().toLowerCase(),EnchanterActionsEnum.ROLL_UNCOMMON.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getRareItem(Material material) {
        if (Arcania.getEnchantManager().getEnchants().stream().noneMatch(e -> e.getRarity() == EnchantRarityEnum.RARE))
            return emptyRarityPlaceholder(EnchantRarityEnum.RARE);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ItemHelper.colorizeHex(EnchantRarityEnum.RARE.getColor() + EnchantRarityEnum.RARE.getDisplayName()));
        meta.setLore(List.of(ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor() + String.format("Roll %s Enchants", EnchantRarityEnum.RARE.getDisplayName())),
                ItemHelper.colorizeHex(EnchantExtraEnum.LEVEL_COST.getColor() + String.format("Cost %d levels",EnchantRarityEnum.RARE.getNumericValue()))));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelper.setPersistentData(PersistentDataEnum.GUI_ACTION.toString().toLowerCase(),EnchanterActionsEnum.ROLL_RARE.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getLegendaryItem(Material material) {
        if (Arcania.getEnchantManager().getEnchants().stream().noneMatch(e -> e.getRarity() == EnchantRarityEnum.LEGENDARY))
            return emptyRarityPlaceholder(EnchantRarityEnum.LEGENDARY);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ItemHelper.colorizeHex(EnchantRarityEnum.LEGENDARY.getColor() + EnchantRarityEnum.LEGENDARY.getDisplayName()));
        meta.setLore(List.of(ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor() + String.format("Roll %s Enchants", EnchantRarityEnum.LEGENDARY.getDisplayName())),
                ItemHelper.colorizeHex(EnchantExtraEnum.LEVEL_COST.getColor() + String.format("Cost %d levels",EnchantRarityEnum.LEGENDARY.getNumericValue()))));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelper.setPersistentData(PersistentDataEnum.GUI_ACTION.toString().toLowerCase(),EnchanterActionsEnum.ROLL_LEGENDARY.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getUltraItem(Material material) {
        if (Arcania.getEnchantManager().getEnchants().stream().noneMatch(e -> e.getRarity() == EnchantRarityEnum.ULTRA))
            return emptyRarityPlaceholder(EnchantRarityEnum.ULTRA);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ItemHelper.colorizeHex(EnchantRarityEnum.ULTRA.getColor() + EnchantRarityEnum.ULTRA.getDisplayName()));
        meta.setLore(List.of(ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor() + String.format("Roll %s Enchants", EnchantRarityEnum.ULTRA.getDisplayName())),
                ItemHelper.colorizeHex(EnchantExtraEnum.LEVEL_COST.getColor() + String.format("Cost %d levels",EnchantRarityEnum.ULTRA.getNumericValue()))));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelper.setPersistentData(PersistentDataEnum.GUI_ACTION.toString().toLowerCase(),EnchanterActionsEnum.ROLL_ULTRA.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }
}
