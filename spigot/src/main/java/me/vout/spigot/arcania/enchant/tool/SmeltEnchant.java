package me.vout.spigot.arcania.enchant.tool;

import me.vout.core.arcania.enums.ArcaniaEnchantType;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;

public class SmeltEnchant extends ArcaniaEnchant {
    public static final SmeltEnchant INSTANCE = new SmeltEnchant();
    private SmeltEnchant() {
        super("Smelt",
                ArcaniaEnchantType.SMELT.getKey(),
                "Instantly smelt the block into it's smelting result",
                EnchantRarityEnum.ULTRA,
                1,
                0.1,
                5,
                me.vout.core.arcania.util.ItemHelper::isHarvestingTool);
    }

    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }
}
