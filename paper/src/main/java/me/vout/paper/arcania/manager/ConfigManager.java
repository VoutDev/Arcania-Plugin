package me.vout.paper.arcania.manager;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import me.vout.paper.arcania.Arcania;

public class ConfigManager {
    private boolean fixEnchantCheckEnabled;
    private int veinminerMaxBlocks;
    private List<String> veinminerWhitelistedBlocks;
    private List<Double> essenceXpMultiplier;
    private double essenceK;
    private List<Double> enrichmentXpMultiplier;
    private double enrichmentK;

    public ConfigManager() {
        this.loadConfig();
    }

    public void loadConfig() {
        FileConfiguration config = Arcania.getInstance().getConfig();
        
        fixEnchantCheckEnabled = config.getBoolean("utility.fix-enchant-check", false);
        
        veinminerMaxBlocks = config.getInt("enchant.veinminer.max-blocks", 10);
        veinminerWhitelistedBlocks = config.getStringList("enchant.veinminer.whitelisted-blocks");
        
        essenceXpMultiplier = config.getDoubleList("enchant.essence.xp-multiplier");
        essenceK = config.getDouble("enchant.essence.k", 10.0);
        
        enrichmentXpMultiplier = config.getDoubleList("enchant.enrichment.xp-multiplier");
        enrichmentK = config.getDouble("enchant.enrichment.k", 10.0);
    }

    public void reload() {
        Arcania.getInstance().getLogger().info("Reloading configs");
        Arcania.getInstance().reloadConfig();
        this.loadConfig();
        
    }

    // Getters
    public boolean isFixEnchantCheckEnabled() { return fixEnchantCheckEnabled; }
    public int getVeinminerMaxBlocks() { return veinminerMaxBlocks; }
    public List<String> getVeinminerWhitelistedBlocks() { return veinminerWhitelistedBlocks; }
    public List<Double> getEssenceXpMultiplier() { return essenceXpMultiplier; }
    public double getEssenceK() { return essenceK; }
    public List<Double> getEnrichmentXpMultiplier() { return enrichmentXpMultiplier; }
    public double getEnrichmentK() { return enrichmentK; }
}
