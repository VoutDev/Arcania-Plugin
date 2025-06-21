package me.vout.spigot.arcania.manager;

import me.vout.core.arcania.enums.EnchantsFilterEnum;
import me.vout.core.arcania.gui.GuiTypeEnum;
import me.vout.core.arcania.gui.enchants.EnchantsMenu;
import me.vout.core.arcania.gui.enchants.TesterMenu;
import me.vout.core.arcania.managers.GuiManagerBase;
import me.vout.core.arcania.providers.ArcaniaProvider;
import me.vout.spigot.arcania.gui.disenchanter.DisenchanterMenu;
import me.vout.spigot.arcania.gui.enchanter.EnchanterMenu;
import me.vout.spigot.arcania.gui.main.MainMenu;
import me.vout.spigot.arcania.gui.tinkerer.TinkererMenu;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class GuiManager extends GuiManagerBase {
    public Inventory getMenuInventory(GuiTypeEnum guiType, UUID uuid) {
        return switch (guiType) {
            case MAIN -> MainMenu.build();
            case TINKERER -> TinkererMenu.build(guiHistory.get(uuid));
            case DISENCHANTER -> DisenchanterMenu.build(guiHistory.get(uuid));
            case ENCHANTER -> EnchanterMenu.build(guiHistory.get(uuid));
            case ENCHANTS -> EnchantsMenu.build(guiHistory.get(uuid), me.vout.core.arcania.enums.EnchantsFilterEnum.ALL, ArcaniaProvider.getPlugin().getEnchantStrategy()::getEnchants);
            case TESTER -> TesterMenu.build(guiHistory.get(uuid), EnchantsFilterEnum.ALL, ArcaniaProvider.getPlugin().getEnchantStrategy()::getAllEnchantBooksByLevel);
        };
    }
}