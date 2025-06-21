package me.vout.core.arcania;

import me.vout.core.arcania.enums.EnchantsFilterEnum;
import me.vout.core.arcania.enums.IEnchantRarity;
import me.vout.core.arcania.managers.ConfigManager;
import me.vout.core.arcania.service.BlockTaggingService;
import me.vout.core.arcania.strategies.IEnchantmentStrategy;
import me.vout.core.arcania.strategies.IValidToolStrategy;
import org.bukkit.plugin.java.JavaPlugin;

public interface ArcaniaPlugin {
    void reloadManagers();
    ConfigManager getConfigManager();
    JavaPlugin getJavaPlugin();
    IEnchantmentStrategy getEnchantStrategy();
    IValidToolStrategy getValidToolStrategy();
    BlockTaggingService getBlockTaggingService();
    IEnchantRarity getRarityForFilter(EnchantsFilterEnum filter);
}
