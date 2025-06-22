package me.vout.core.arcania.managers;

import me.vout.core.arcania.providers.ArcaniaProvider;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {
    private int veinminerMaxBlocks;
    private List<String> veinminerWhitelistedBlocks;
    private List<String> smeltBlackListedBlocks;
    private List<Double> essenceXpMultiplier;
    private double essenceK;
    private int essenceCutoff;
    private List<Double> enrichmentXpMultiplier;
    private double enrichmentK;
    private int enrichmentCutoff;

    public void reload() {
        ArcaniaProvider.getPlugin().getJavaPlugin().getLogger().info("Reloading configs");
        ArcaniaProvider.getPlugin().getJavaPlugin().reloadConfig();
        this.loadConfig();
    }

    public void loadConfig() {
        FileConfiguration config = ArcaniaProvider.getPlugin().getJavaPlugin().getConfig();

        veinminerMaxBlocks = config.getInt("enchant.veinminer.max-blocks", 10);
        veinminerWhitelistedBlocks = config.getStringList("enchant.veinminer.whitelisted-blocks");

        smeltBlackListedBlocks = config.getStringList("enchant.smelt.blacklisted-blocks");

        essenceXpMultiplier = config.getDoubleList("enchant.essence.xp-multiplier");
        essenceK = config.getDouble("enchant.essence.k", 10.0);
        essenceCutoff = config.getInt("enchant.essence.base-cutoff", 5);

        enrichmentXpMultiplier = config.getDoubleList("enchant.enrichment.xp-multiplier");
        enrichmentK = config.getDouble("enchant.enrichment.k", 10.0);
        enrichmentCutoff = config.getInt("enchant.enrichment.base-cutoff", 5);
    }

    // Getters
    public int getVeinminerMaxBlocks() { return veinminerMaxBlocks; }
    public List<String> getVeinminerWhitelistedBlocks() { return veinminerWhitelistedBlocks; }
    public List<String> getSmeltBlackListedBlocks() {
        return smeltBlackListedBlocks;
    }
    public List<Double> getEssenceXpMultiplier() { return essenceXpMultiplier; }
    public double getEssenceK() { return essenceK; }
    public int getEssenceCutoff() {
        return essenceCutoff;
    }
    public List<Double> getEnrichmentXpMultiplier() { return enrichmentXpMultiplier; }
    public double getEnrichmentK() { return enrichmentK; }
    public int getEnrichmentCutoff() {
        return enrichmentCutoff;
    }
}
