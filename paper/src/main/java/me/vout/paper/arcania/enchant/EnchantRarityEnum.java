package me.vout.paper.arcania.enchant;

import me.vout.core.arcania.enums.IEnchantRarity;

public enum EnchantRarityEnum implements IEnchantRarity {
    COMMON("#868D91", "Common", 1),
    UNCOMMON("#67A168","Uncommon", 2),
    RARE("#4383CC", "Rare", 3),
    LEGENDARY("#FF7200", "Legendary", 4),
    ULTRA("#AE53BF", "Ultra", 5);

    private final String color;
    private final String displayName;
    private final int weight;
    EnchantRarityEnum(String color, String displayName, int weight) { this.color = color; this.displayName = displayName; this.weight = weight; }
    public String getColor() { return color; }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int getNumericValue() {
        return weight;
    }
}

