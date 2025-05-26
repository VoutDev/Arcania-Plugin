package me.vout.arcania;

import me.vout.arcania.command.*;
import me.vout.arcania.command.tab.ArcaniaTabCompleter;
import me.vout.arcania.listener.ArcaniaEnchantListener;
import me.vout.arcania.listener.GuiListener;
import me.vout.arcania.manager.ArcaniaEnchantManager;
import me.vout.arcania.manager.ConfigManager;
import me.vout.arcania.manager.GuiManager;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Arcania extends JavaPlugin {
    private static Arcania instance;
    private static GuiManager guiManager;

    private static ConfigManager configManager;
    private static ArcaniaEnchantManager enchantManager;

    @Override
    public void onEnable() {
        getLogger().info("Plugin started!");
        instance = this;
        configManager = new ConfigManager();
        saveDefaultConfig();
        reloadManagers();
        registerCommands();
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled!");
        // Plugin shutdown logic
    }

    public void registerCommands() {
        Objects.requireNonNull(getCommand("arcania")).setExecutor(new ArcaniaCommand(guiManager, this));
        Objects.requireNonNull(getCommand("arcania")).setTabCompleter(new ArcaniaTabCompleter());
        Objects.requireNonNull(getCommand("tinkerer")).setExecutor(new TinkererCommand(guiManager));
        Objects.requireNonNull(getCommand("disenchanter")).setExecutor(new DisenchanterCommand(guiManager));
        Objects.requireNonNull(getCommand("enchanter")).setExecutor(new EnchanterCommand(guiManager));
        Objects.requireNonNull(getCommand("enchants")).setExecutor(new EnchantsCommand(guiManager));
    }

    public void reloadManagers() {
        // Unregister all listeners for this plugin
        HandlerList.unregisterAll(this);

        // Reload config and re-initialize managers
        //reloadConfig();
        configManager.reload();
        enchantManager = new ArcaniaEnchantManager();
        guiManager = new GuiManager();

        // Register listeners with new manager instances
        getServer().getPluginManager().registerEvents(
                new ArcaniaEnchantListener(), this
        );
        getServer().getPluginManager().registerEvents(
                new GuiListener(guiManager), this
        );
    }

    public static ArcaniaEnchantManager getEnchantManager() {
        return enchantManager;
    }
    public static Arcania getInstance() {
        return  instance;
    }
    public static ConfigManager getConfigManager() {
        return configManager;
    }
}