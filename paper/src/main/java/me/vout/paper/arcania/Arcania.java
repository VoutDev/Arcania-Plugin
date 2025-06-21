package me.vout.paper.arcania;


import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
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
import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import me.vout.paper.arcania.enchant.EnchantRarityEnum;
import me.vout.paper.arcania.listener.GuiListener;
import me.vout.paper.arcania.manager.GuiManager;
import me.vout.paper.arcania.strategies.EnchantmentStategy;
import me.vout.paper.arcania.strategies.ValidToolStrategy;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Arcania extends JavaPlugin implements ArcaniaPlugin {
    private static GuiManager guiManager;
    private static ConfigManager configManager;
    private static Server server;
    private  static ValidToolStrategy validToolStrategy;
    private static EnchantmentStategy enchantmentStategy;
    private static Map<NamespacedKey, Enchantment> enchantmentMap;
    private static BlockTaggingService blockTaggingService;

    @Override
    public void onEnable() {
        getLogger().info("Plugin started!");
        ArcaniaProvider.initialize(this);
        ArcaniaEnchantType.initializeKeys();
        server = this.getServer();
        blockTaggingService = new BlockTaggingService();
        blockTaggingService.initialize();
        configManager = new ConfigManager();
        saveDefaultConfig();
        reloadManagers();
        loadEnchantRegistry();
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

    public void reloadManagers() {
        // Unregister all listeners for this plugin
        HandlerList.unregisterAll(this);
        configManager.reload();
        validToolStrategy = new ValidToolStrategy();
        enchantmentStategy = new EnchantmentStategy();
        guiManager = new GuiManager();

        // Register listeners with new manager instances
        server.getPluginManager().registerEvents(
                new ArcaniaEnchantListener(), this
        );
        server.getPluginManager().registerEvents(
                new GuiListener(getGuiManager()), this
        );
        server.getPluginManager().registerEvents(
                new WorldListener(), this
        );
        for (org.bukkit.World world : Bukkit.getWorlds()) { // Use org.bukkit.World to avoid ambiguity
            blockTaggingService.loadWorldData(world.getUID());
            getLogger().info("Manually loaded block tags for world: " + world.getName() + " (" + world.getUID() + ")");
        }
    }
    
    public void loadEnchantRegistry() {
        Arcania.enchantmentMap = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).stream()
                .filter(enchantment -> enchantment.getKey().getNamespace()
                        .equalsIgnoreCase(ArcaniaEnchant.NAMESPACE))
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                Enchantment::getKey,
                                Function.identity()
                        ),
                        Collections::unmodifiableMap
                        ));
    }

    public static Map<NamespacedKey, Enchantment> getEnchantMap() {
        return enchantmentMap;
    }

    @Override
    public ConfigManager getConfigManager() {
        return configManager;
    }

    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public IEnchantmentStrategy getEnchantStrategy() {
        return enchantmentStategy;
    }

    @Override
    public IValidToolStrategy getValidToolStrategy() {
        return validToolStrategy;
    }

    @Override
    public BlockTaggingService getBlockTaggingService() {
        return blockTaggingService;
    }

    public static GuiManager getGuiManager() {
        return guiManager;
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