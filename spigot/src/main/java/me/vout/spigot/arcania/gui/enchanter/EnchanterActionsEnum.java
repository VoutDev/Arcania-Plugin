package me.vout.spigot.arcania.gui.enchanter;

import me.vout.spigot.arcania.enchant.EnchantRarityEnum;

public enum EnchanterActionsEnum {
    ROLL_COMMON("roll_common", EnchantRarityEnum.COMMON),
    ROLL_UNCOMMON("roll_uncommon", EnchantRarityEnum.UNCOMMON),
    ROLL_RARE("roll_rare", EnchantRarityEnum.RARE),
    ROLL_LEGENDARY("roll_legendary", EnchantRarityEnum.LEGENDARY),
    ROLL_ULTRA("roll_ultra", EnchantRarityEnum.ULTRA);

    private final String key;
    private final EnchantRarityEnum rarity;
    EnchanterActionsEnum(String key, EnchantRarityEnum rarity) {
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
