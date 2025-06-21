package me.vout.core.arcania.gui.enchants;

import me.vout.core.arcania.enums.EnchantExtraEnum;
import me.vout.core.arcania.enums.EnchantsFilterEnum;
import me.vout.core.arcania.enums.IEnchantRarity;
import me.vout.core.arcania.gui.GuiBuilder;
import me.vout.core.arcania.gui.GuiHelperBase;
import me.vout.core.arcania.gui.GuiTypeEnum;
import me.vout.core.arcania.gui.PersistentDataEnum;
import me.vout.core.arcania.providers.ArcaniaProvider;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Deque;
import java.util.List;
import java.util.function.Function;

public class EnchantsMenu {
    public static Inventory build(Deque<GuiTypeEnum> guiHistory, EnchantsFilterEnum filter, Function<EnchantsFilterEnum, ItemStack[]> enchantsSupplier) {
        int size = 54;
        return new GuiBuilder(size,GuiTypeEnum.ENCHANTS.getDisplayName(), new EnchantsMenuHolder())
                .setBorder(new ItemStack(Material.BLUE_STAINED_GLASS))
                .set(48,  me.vout.core.arcania.gui.enchants.EnchantsMenu.getCommonItem(Material.LIGHT_GRAY_CONCRETE))
                .set(49, me.vout.core.arcania.gui.enchants.EnchantsMenu.getUncommonItem(Material.LIME_CONCRETE))
                .set(50, me.vout.core.arcania.gui.enchants.EnchantsMenu.getRareItem(Material.LIGHT_BLUE_CONCRETE))
                .set(51, me.vout.core.arcania.gui.enchants.EnchantsMenu.getLegendaryItem(Material.ORANGE_CONCRETE))
                .set(52, me.vout.core.arcania.gui.enchants.EnchantsMenu.getUltraItem(Material.MAGENTA_CONCRETE))
                .fillEmptySlotsWithItems(enchantsSupplier.apply(filter))
                .fillEmptySlots(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE))
                .addBackButton(guiHistory)
                .build();
    }
    public static ItemStack getCommonItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        IEnchantRarity rarity = ArcaniaProvider.getPlugin().getRarityForFilter(EnchantsFilterEnum.COMMON_FILTER);
        meta.setDisplayName(me.vout.core.arcania.util.ItemHelper.colorizeHex(rarity.getColor() + "Common"));
        meta.setLore(List.of(me.vout.core.arcania.util.ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor() + "Show only common enchants")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelperBase.setPersistentData(PersistentDataEnum.FILTER.toString().toLowerCase(), EnchantsFilterEnum.COMMON_FILTER.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getUncommonItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        IEnchantRarity rarity = ArcaniaProvider.getPlugin().getRarityForFilter(EnchantsFilterEnum.UNCOMMON_FILTER);
        meta.setDisplayName(me.vout.core.arcania.util.ItemHelper.colorizeHex(rarity.getColor() + "Uncommon"));
        meta.setLore(List.of(me.vout.core.arcania.util.ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor() + "Show only uncommon enchants")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelperBase.setPersistentData(PersistentDataEnum.FILTER.toString().toLowerCase(), EnchantsFilterEnum.UNCOMMON_FILTER.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getRareItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        IEnchantRarity rarity = ArcaniaProvider.getPlugin().getRarityForFilter(EnchantsFilterEnum.RARE_FILTER);
        meta.setDisplayName(me.vout.core.arcania.util.ItemHelper.colorizeHex(rarity.getColor() + "Rare"));
        meta.setLore(List.of(me.vout.core.arcania.util.ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor() + "Show only rare enchants")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelperBase.setPersistentData(PersistentDataEnum.FILTER.toString().toLowerCase(), EnchantsFilterEnum.RARE_FILTER.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getLegendaryItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        IEnchantRarity rarity = ArcaniaProvider.getPlugin().getRarityForFilter(EnchantsFilterEnum.LEGENDARY_FILTER);
        meta.setDisplayName(me.vout.core.arcania.util.ItemHelper.colorizeHex(rarity.getColor() + "Legendary"));
        meta.setLore(List.of(me.vout.core.arcania.util.ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor() + "Show only legendary enchants")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelperBase.setPersistentData(PersistentDataEnum.FILTER.toString().toLowerCase(), EnchantsFilterEnum.LEGENDARY_FILTER.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getUltraItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        IEnchantRarity rarity = ArcaniaProvider.getPlugin().getRarityForFilter(EnchantsFilterEnum.ULTRA_FILTER);
        meta.setDisplayName(me.vout.core.arcania.util.ItemHelper.colorizeHex(rarity.getColor() + "Ultra"));
        meta.setLore(List.of(me.vout.core.arcania.util.ItemHelper.colorizeHex(EnchantExtraEnum.TOOL_TIP.getColor() + "Show only ultra enchants")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelperBase.setPersistentData(PersistentDataEnum.FILTER.toString().toLowerCase(), EnchantsFilterEnum.ULTRA_FILTER.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }
}
