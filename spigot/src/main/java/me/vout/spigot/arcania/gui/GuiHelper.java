package me.vout.spigot.arcania.gui;

import me.vout.core.arcania.gui.GuiHelperBase;
import me.vout.core.arcania.gui.PersistentDataEnum;
import me.vout.core.arcania.providers.ArcaniaProvider;
import me.vout.spigot.arcania.gui.enchanter.EnchanterActionsEnum;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GuiHelper extends GuiHelperBase {
    public static EnchanterActionsEnum isEnchanterAction(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        NamespacedKey key = new NamespacedKey(ArcaniaProvider.getPlugin().getJavaPlugin(), PersistentDataEnum.GUI_ACTION.toString());
        if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            String value = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            for (EnchanterActionsEnum t: EnchanterActionsEnum.values()) {
                if (t.toString().equalsIgnoreCase(value))
                    return t;
            }
        }
        return null;
    }
}