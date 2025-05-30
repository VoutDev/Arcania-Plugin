package me.vout.arcania.gui.enchants;

import me.vout.arcania.enchant.EnchantRarityEnum;

public enum EnchantsFilterEnum {
    COMMON_FILTER("common_filter",EnchantRarityEnum.COMMON),
    UNCOMMON_FILTER("uncommon_filter", EnchantRarityEnum.UNCOMMON),
    RARE_FILTER("rare_filter", EnchantRarityEnum.RARE),
    LEGENDARY_FILTER("legendary_filter", EnchantRarityEnum.LEGENDARY),
    ULTRA_FILTER("ultra_filter", EnchantRarityEnum.ULTRA),
    ALL("all", null);

    private final String key;
    private final EnchantRarityEnum rarity;
    EnchantsFilterEnum(String key, EnchantRarityEnum rarity) {
        this.key = key;
        this.rarity = rarity;
    }

    @Override
    public String toString() {
        return key;
    }

    public EnchantRarityEnum getRarity() {
        return rarity;
    }
}
