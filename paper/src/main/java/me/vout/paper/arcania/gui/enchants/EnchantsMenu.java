package me.vout.paper.arcania.gui.enchants;

import me.vout.paper.arcania.Arcania;
import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import me.vout.paper.arcania.enchant.EnchantExtraEnum;
import me.vout.paper.arcania.enchant.EnchantRarityEnum;
import me.vout.paper.arcania.gui.GuiBuilder;
import me.vout.paper.arcania.gui.GuiHelper;
import me.vout.paper.arcania.gui.GuiTypeEnum;
import me.vout.paper.arcania.gui.PersistentDataEnum;
import me.vout.paper.arcania.util.EnchantHelper;
import me.vout.paper.arcania.util.ItemHelper;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Comparator;
import java.util.Deque;
import java.util.List;

public class EnchantsMenu {
    public static Inventory build(Deque<GuiTypeEnum> guiHistory, EnchantsFilterEnum filter) {
        int size = 54;
        return new GuiBuilder(size,GuiTypeEnum.ENCHANTS.getDisplayName(), new EnchantsMenuHolder())
                .setBorder(new ItemStack(Material.BLUE_STAINED_GLASS))
                .set(48, getCommonItem(Material.LIGHT_GRAY_CONCRETE))
                .set(49, getUncommonItem(Material.LIME_CONCRETE))
                .set(50, getRareItem(Material.LIGHT_BLUE_CONCRETE))
                .set(51, getLegendaryItem(Material.ORANGE_CONCRETE))
                .set(52, getUltraItem(Material.MAGENTA_CONCRETE))
                .fillEmptySlots(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE))
                .addBackButton(guiHistory)
                .build();
    }


    private static ItemStack getCommonItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ItemHelper.colorizeHex(EnchantRarityEnum.COMMON.getColor() + "Common"));
        meta.setLore(List.of(ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor() + "Show only common enchants")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelper.setPersistentData(PersistentDataEnum.FILTER.toString().toLowerCase(), EnchantsFilterEnum.COMMON_FILTER.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getUncommonItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ItemHelper.colorizeHex(EnchantRarityEnum.UNCOMMON.getColor() + "Uncommon"));
        meta.setLore(List.of(ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor() + "Show only uncommon enchants")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelper.setPersistentData(PersistentDataEnum.FILTER.toString().toLowerCase(), EnchantsFilterEnum.UNCOMMON_FILTER.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getRareItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ItemHelper.colorizeHex(EnchantRarityEnum.RARE.getColor() + "Rare"));
        meta.setLore(List.of(ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor() + "Show only rare enchants")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelper.setPersistentData(PersistentDataEnum.FILTER.toString().toLowerCase(), EnchantsFilterEnum.RARE_FILTER.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getLegendaryItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ItemHelper.colorizeHex(EnchantRarityEnum.LEGENDARY.getColor() + "Legendary"));
        meta.setLore(List.of(ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor() + "Show only legendary enchants")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelper.setPersistentData(PersistentDataEnum.FILTER.toString().toLowerCase(), EnchantsFilterEnum.LEGENDARY_FILTER.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getUltraItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ItemHelper.colorizeHex(EnchantRarityEnum.ULTRA.getColor() + "Ultra"));
        meta.setLore(List.of(ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor() + "Show only ultra enchants")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelper.setPersistentData(PersistentDataEnum.FILTER.toString().toLowerCase(), EnchantsFilterEnum.ULTRA_FILTER.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }
}