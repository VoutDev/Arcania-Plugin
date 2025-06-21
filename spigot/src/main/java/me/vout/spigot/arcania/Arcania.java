package me.vout.spigot.arcania;

import me.vout.core.arcania.ArcaniaPlugin;
import me.vout.core.arcania.enums.ArcaniaEnchantType;
import me.vout.core.arcania.enums.EnchantsFilterEnum;
import me.vout.core.arcania.enums.IEnchantRarity;
import me.vout.core.arcania.listener.ArcaniaEnchantListener;
import me.vout.core.arcania.listener.WorldListener;
import me.vout.core.arcania.managers.ConfigManager;
import me.vout.core.arcania.providers.ArcaniaProvider;
import me.vout.core.arcania.service.BlockTaggingService;
import me.vout.core.arcania.strategies.IEnchantmentStrategy;
import me.vout.core.arcania.strategies.IValidToolStrategy;
import me.vout.core.arcania.util.ItemHelper;
import me.vout.spigot.arcania.command.*;
import me.vout.spigot.arcania.command.tab.ArcaniaTabCompleter;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;
import me.vout.spigot.arcania.listener.GuiListener;
import me.vout.spigot.arcania.manager.ArcaniaEnchantManager;
import me.vout.spigot.arcania.manager.GuiManager;
import me.vout.spigot.arcania.strategies.EnchantmentStrategy;
import me.vout.spigot.arcania.strategies.ValidToolStrategy;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Arcania extends JavaPlugin implements ArcaniaPlugin {
    private static GuiManager guiManager;
    private static ConfigManager configManager;
    private static ArcaniaEnchantManager enchantManager;
    private  static ValidToolStrategy validToolStrategy;
    private static EnchantmentStrategy enchantmentStrategy;
    private static BlockTaggingService blockTaggingService;

    @Override
    public void onEnable() {
        getLogger().info("Plugin started!");
        ArcaniaProvider.initialize(this);
        blockTaggingService = new BlockTaggingService();
        blockTaggingService.initialize();
        configManager = new ConfigManager();
        saveDefaultConfig();
        reloadManagers();
        registerCommands();
        ItemHelper.initFurnaceRecipes();
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled!");
        if (blockTaggingService != null) {
            getLogger().info("Shutting down block tagging service");
            blockTaggingService.shutdown();
        }
    }

    public void registerCommands() {
        Objects.requireNonNull(getCommand("arcania")).setExecutor(new ArcaniaCommand(guiManager, this));
        Objects.requireNonNull(getCommand("arcania")).setTabCompleter(new ArcaniaTabCompleter());
        Objects.requireNonNull(getCommand("tinkerer")).setExecutor(new TinkererCommand(guiManager));
        Objects.requireNonNull(getCommand("disenchanter")).setExecutor(new DisenchanterCommand(guiManager));
        Objects.requireNonNull(getCommand("enchanter")).setExecutor(new EnchanterCommand(guiManager));
        Objects.requireNonNull(getCommand("enchants")).setExecutor(new EnchantsCommand(guiManager));
        Objects.requireNonNull(getCommand("tester")).setExecutor(new TesterCommand(guiManager));
    }

    public void reloadManagers() {
        // Unregister all listeners for this plugin
        HandlerList.unregisterAll(this);

        // Reload config and re-initialize managers
        configManager.reload();
        ArcaniaEnchantType.initializeKeys();
        enchantManager = new ArcaniaEnchantManager();
        validToolStrategy = new ValidToolStrategy();
        enchantmentStrategy = new EnchantmentStrategy();
        guiManager = new GuiManager();

        // Register listeners with new manager instances
        getServer().getPluginManager().registerEvents(
                new ArcaniaEnchantListener(), this
        );
        getServer().getPluginManager().registerEvents(
                new GuiListener(guiManager), this
        );
        getServer().getPluginManager().registerEvents(
                new WorldListener(), this
        );
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public IEnchantmentStrategy getEnchantStrategy() {
        return enchantmentStrategy;
    }

    @Override
    public IValidToolStrategy getValidToolStrategy() {
        return validToolStrategy;
    }

    @Override
    public BlockTaggingService getBlockTaggingService() {
        return blockTaggingService;
    }

    public static ArcaniaEnchantManager getEnchantManager() {
        return enchantManager;
    }

    @Override
    public ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public IEnchantRarity getRarityForFilter(EnchantsFilterEnum filter) {
        return switch (filter) {
            case COMMON_FILTER -> EnchantRarityEnum.COMMON;
            case UNCOMMON_FILTER -> EnchantRarityEnum.UNCOMMON;
            case RARE_FILTER -> EnchantRarityEnum.RARE;
            case LEGENDARY_FILTER -> EnchantRarityEnum.LEGENDARY;
            case ULTRA_FILTER -> EnchantRarityEnum.ULTRA;
            case ALL -> null; // Or handle as needed
        };
    }
}