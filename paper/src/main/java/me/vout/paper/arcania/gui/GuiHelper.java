package me.vout.paper.arcania.gui;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.vout.paper.arcania.Arcania;

public class GuiHelper {
    public static boolean isBackButton(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(Arcania.getInstance(),PersistentDataEnum.BUTTON_TYPE.toString());
        if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            String value = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            return "back".equals(value);
        }
        return false;
    }

    public static GuiTypeEnum isGuiRedirect(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(Arcania.getInstance(),PersistentDataEnum.GUI_REDIRECT.toString());
        if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            String value = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            for (GuiTypeEnum t: GuiTypeEnum.values()) {
                if (t.toString().equalsIgnoreCase(value))
                    return t;
            }
        }
        return null;
    }


    public static void setPersistentData(String keyName, String value, ItemMeta meta) {
        NamespacedKey key = new NamespacedKey(Arcania.getInstance(), keyName);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
    }
}
