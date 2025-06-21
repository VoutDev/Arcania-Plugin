package me.vout.core.arcania.gui;

import me.vout.core.arcania.enums.EnchantsFilterEnum;
import me.vout.core.arcania.providers.ArcaniaProvider;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GuiHelperBase {
    public static boolean isBackButton(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        NamespacedKey key = new NamespacedKey(ArcaniaProvider.getPlugin().getJavaPlugin(),PersistentDataEnum.BUTTON_TYPE.toString());
        if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            String value = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            return "back".equals(value);
        }
        return false;
    }

    public static me.vout.core.arcania.enums.EnchantsFilterEnum isEnchantsFilter(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        NamespacedKey key = new NamespacedKey(ArcaniaProvider.getPlugin().getJavaPlugin(), PersistentDataEnum.FILTER.toString());
        if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            String value = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            for (me.vout.core.arcania.enums.EnchantsFilterEnum t: EnchantsFilterEnum.values()) {
                if (t.toString().equalsIgnoreCase(value))
                    return t;
            }
        }
        return null;
    }

    public static GuiTypeEnum isGuiRedirect(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        NamespacedKey key = new NamespacedKey(ArcaniaProvider.getPlugin().getJavaPlugin(),PersistentDataEnum.GUI_REDIRECT.toString());
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
        NamespacedKey key = new NamespacedKey(ArcaniaProvider.getPlugin().getJavaPlugin(), keyName);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
    }
}
