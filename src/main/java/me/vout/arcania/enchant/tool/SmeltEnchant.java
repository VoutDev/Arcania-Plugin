package me.vout.arcania.enchant.tool;

import me.vout.arcania.enchant.ArcaniaEnchant;
import me.vout.arcania.enchant.EnchantRarityEnum;
import me.vout.arcania.util.ItemHelper;

public class SmeltEnchant extends ArcaniaEnchant {
    public static final SmeltEnchant INSTANCE = new SmeltEnchant();
    private SmeltEnchant() {
        super("Smelt",
                "Instantly smelt the block into it's smelting result",
                EnchantRarityEnum.ULTRA,
                1,
                0.1,
                5,
                ItemHelper::isPickaxe);
    }

    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }
}
