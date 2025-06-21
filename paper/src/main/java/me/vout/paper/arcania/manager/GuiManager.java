package me.vout.paper.arcania.manager;

import me.vout.core.arcania.gui.GuiTypeEnum;
import me.vout.core.arcania.gui.enchants.EnchantsMenu;
import me.vout.core.arcania.gui.enchants.TesterMenu;
import me.vout.core.arcania.managers.GuiManagerBase;
import me.vout.core.arcania.providers.ArcaniaProvider;
import me.vout.paper.arcania.gui.main.MainMenu;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class GuiManager extends GuiManagerBase {
    public Inventory getMenuInventory(GuiTypeEnum guiType, UUID uuid) {
        return switch (guiType) {
            case MAIN -> MainMenu.build();
            case ENCHANTS -> EnchantsMenu.build(guiHistory.get(uuid), me.vout.core.arcania.enums.EnchantsFilterEnum.ALL, ArcaniaProvider.getPlugin().getEnchantStrategy()::getEnchants);
            case TESTER -> TesterMenu.build(guiHistory.get(uuid), me.vout.core.arcania.enums.EnchantsFilterEnum.ALL, ArcaniaProvider.getPlugin().getEnchantStrategy()::getAllEnchantBooksByLevel);
            default -> throw new IllegalStateException("Unexpected value: " + guiType);
        };
    }
}