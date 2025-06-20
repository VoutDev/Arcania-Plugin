package me.vout.paper.arcania.gui.main;

import me.vout.core.arcania.gui.GuiBuilder;
import me.vout.core.arcania.gui.GuiTypeEnum;
import me.vout.core.arcania.gui.main.MainMenuHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
                .fillEmptySlots(new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
                .build();
    }

    private static ItemStack getShowEnchantsItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.customName(Component.text("Show Enchants", NamedTextColor.AQUA));
        meta.lore(List.of(Component.text("Click to show all enchants", NamedTextColor.GRAY)));

        meta.addEnchant(Enchantment.UNBREAKING, 1, true); // Glowing effect
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }
}