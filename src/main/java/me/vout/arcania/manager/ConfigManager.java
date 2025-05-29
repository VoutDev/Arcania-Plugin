package me.vout.arcania.manager;

import me.vout.arcania.Arcania;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private boolean fixEnchantCheckEnabled;
    private int veinminerMaxBlocks;
    private List<String> veinminerWhitelistedBlocks;

    public void reload() {
        Arcania.getInstance().getLogger().info("Reloading configs");
        Arcania.getInstance().reloadConfig();
        FileConfiguration config = Arcania.getInstance().getConfig();
        fixEnchantCheckEnabled = config.getBoolean("utility.fix-enchant-check", false);
        veinminerMaxBlocks = config.getInt("enchant.veinminer.max-blocks", 10);
        veinminerWhitelistedBlocks = config.getStringList("enchant.veinminer.whitelisted-blocks");
    }

    // Getters
    public boolean isFixEnchantCheckEnabled() { return fixEnchantCheckEnabled; }
    public int getVeinminerMaxBlocks() { return veinminerMaxBlocks; }
    public List<String> getVeinminerWhitelistedBlocks() { return veinminerWhitelistedBlocks; }
}
