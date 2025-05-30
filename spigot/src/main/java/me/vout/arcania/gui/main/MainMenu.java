package me.vout.arcania.gui.main;

import me.vout.arcania.gui.GuiBuilder;
import me.vout.arcania.gui.GuiHelper;
import me.vout.arcania.gui.GuiTypeEnum;
import me.vout.arcania.gui.PersistentDataEnum;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MainMenu {
    public static Inventory build() {
        return new GuiBuilder(GuiTypeEnum.MAIN.getStaticSize(), GuiTypeEnum.MAIN.getDisplayName(), new MainMenuHolder())
                .setBorder(new ItemStack(Material.BLUE_STAINED_GLASS))
                .set(11, getShowEnchantsItem(Material.BOOKSHELF))
                .set(13, getEnchantsItem(Material.ENCHANTED_BOOK))
                .set(15, getTinkererItem(Material.ANVIL))
                .set(16, getDisenchanterItem(Material.GRINDSTONE))
                .fillEmptySlots(new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
                .build();
    }

    private static ItemStack getShowEnchantsItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§bShow Enchants");
        meta.setLore(List.of("§7Click to show all enchants"));

        meta.addEnchant(Enchantment.UNBREAKING, 1, true); // Glowing effect
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelper.setPersistentData(PersistentDataEnum.GUI_REDIRECT.toString(), GuiTypeEnum.ENCHANTS.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getEnchantsItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§bGet Enchant");
        meta.setLore(List.of("§7Click to roll for enchants"));
        meta.addEnchant(Enchantment.UNBREAKING, 1, true); // Glowing effect
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelper.setPersistentData(PersistentDataEnum.GUI_REDIRECT.toString(), GuiTypeEnum.ENCHANTER.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getTinkererItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§bTinkerer");
        meta.setLore(List.of("§7Click to modify enchants"));
        meta.addEnchant(Enchantment.UNBREAKING, 1, true); // Glowing effect
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        GuiHelper.setPersistentData(PersistentDataEnum.GUI_REDIRECT.toString(), GuiTypeEnum.TINKERER.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack getDisenchanterItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§bDisenchanter");
        meta.setLore(List.of("§7Click to disenchant"));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.addEnchant(Enchantment.UNBREAKING, 1, true); // Glowing effect
        GuiHelper.setPersistentData(PersistentDataEnum.GUI_REDIRECT.toString(), GuiTypeEnum.DISENCHANTER.toString(), meta);
        item.setItemMeta(meta);
        return item;
    }
}