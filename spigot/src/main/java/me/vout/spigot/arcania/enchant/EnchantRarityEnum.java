package me.vout.spigot.arcania.enchant;

public enum EnchantRarityEnum {
    COMMON("#868D91", "Common", 5),
    UNCOMMON("#67A168","Uncommon", 10),
    RARE("#4383CC", "Rare", 15),
    LEGENDARY("#FF7200", "Legendary", 20),
    ULTRA("#AE53BF", "Ultra", 30);

    private final String color;
    private final String displayName;
    private final int cost;
    EnchantRarityEnum(String color, String displayName, int cost) { this.color = color; this.displayName = displayName; this.cost = cost; }
    public String getColor() { return color; }
    public int getCost() { return cost; }

    public String getDisplayName() {
        return displayName;
    }
}

