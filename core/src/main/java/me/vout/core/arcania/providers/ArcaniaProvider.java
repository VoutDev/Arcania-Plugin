package me.vout.core.arcania.providers;

import me.vout.core.arcania.ArcaniaPlugin;

public class ArcaniaProvider {
    private static ArcaniaPlugin instance;

    // Called once from your main plugin's onEnable()
    public static void initialize(ArcaniaPlugin plugin) {
        instance = plugin;
    }

    // The static getter that all helpers will use
    public static ArcaniaPlugin getPlugin() {
        if (instance == null) {
            throw new IllegalStateException("ArcaniaProvider has not been initialized!");
        }
        return instance;
    }
}
