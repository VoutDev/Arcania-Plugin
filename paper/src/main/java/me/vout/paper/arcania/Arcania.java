package me.vout.paper.arcania;


import org.bukkit.Server;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;

import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;


import me.vout.paper.arcania.listener.ArcaniaEnchantListener;
import me.vout.paper.arcania.listener.GuiListener;
import me.vout.paper.arcania.manager.ConfigManager;
import me.vout.paper.arcania.util.ItemHelper;

public final class Arcania extends JavaPlugin {
    private static Arcania instance;
    private static Server server;

    private static ConfigManager configManager;

    // Fetch the enchantment registry from the registry access
    private static Registry<Enchantment> enchantmentRegistry;

    @Override
    public void onEnable() {
        getLogger().info("Plugin started!");
        instance = this;
        server = this.getServer();
        configManager = new ConfigManager();
        saveDefaultConfig();
        reloadManagers(server);
        loadEnchantRegistry();
        ItemHelper.initFurnaceRecipes();
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled!");
        // Plugin shutdown logic
    }


    public static Arcania getInstance() {
        return instance;
    }

    public void reloadManagers(Server server) {
        // Unregister all listeners for this plugin
        HandlerList.unregisterAll(Arcania.getInstance());

        // Register listeners with new manager instances
        server.getPluginManager().registerEvents(
                new ArcaniaEnchantListener(), Arcania.getInstance()
        );
        server.getPluginManager().registerEvents(
                new GuiListener(ArcaniaBootstrap.getGuiManager()), Arcania.getInstance()
        );
    }
    
    public void loadEnchantRegistry() {
        Arcania.enchantmentRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    }

    public static Registry<Enchantment> getEnchantRegistry() {
        return enchantmentRegistry;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }


}