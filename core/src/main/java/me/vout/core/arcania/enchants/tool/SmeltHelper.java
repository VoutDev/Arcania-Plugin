package me.vout.core.arcania.enchants.tool;

import me.vout.core.arcania.providers.ArcaniaProvider;
import org.bukkit.Material;

public class SmeltHelper {
    public static boolean isBlacklistedSmeltBlock(Material mat) {
        return ArcaniaProvider.getPlugin().getConfigManager().getSmeltBlackListedBlocks().stream().anyMatch(pattern -> {
            String regexPattern = pattern.replace("*", ".*");
            return mat.name().matches(regexPattern);
        });
    }
}
