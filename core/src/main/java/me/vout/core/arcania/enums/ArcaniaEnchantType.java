package me.vout.core.arcania.enums;

import me.vout.core.arcania.providers.ArcaniaProvider;
import org.bukkit.NamespacedKey;

public enum ArcaniaEnchantType {
    BLINK("blink"),
    HARVESTER("harvester"),
    TILLER("tiller"),
    ENRICHMENT("enrichment"),
    QUARRY("quarry"),
    SMELT("smelt"),
    VEIN_MINER("vein_miner"),
    MAGNET("magnet"),
    PROSPERITY("prosperity"),
    ESSENCE("essence"),
    FROSTBITE("frostbite");
    private final String keyName; // The string part of the NamespacedKey
    private NamespacedKey namespacedKey; // Will be initialized later

    ArcaniaEnchantType(String keyName) {
        this.keyName = keyName;
    }

    public static void initializeKeys() {
        for (ArcaniaEnchantType type : values()) {
            type.namespacedKey = new NamespacedKey(ArcaniaProvider.getPlugin().getJavaPlugin(), type.keyName);
        }
    }

    public String getKeyName() {
        return keyName;
    }
    public NamespacedKey getKey() {
        if (namespacedKey == null) {
            // This indicates a usage error: trying to get key before initialization.
            throw new IllegalStateException("ArcaniaEnchantType keys not initialized! Call initializeKeys() in onEnable.");
        }
        return namespacedKey;
    }
}
