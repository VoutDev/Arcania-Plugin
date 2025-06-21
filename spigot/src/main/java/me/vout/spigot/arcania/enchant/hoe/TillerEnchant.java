package me.vout.spigot.arcania.enchant.hoe;

import me.vout.core.arcania.enums.ArcaniaEnchantType;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;

public class TillerEnchant extends ArcaniaEnchant {

    public static final TillerEnchant INSTANCE = new TillerEnchant();
    private TillerEnchant() {
        super("Tiller",
                ArcaniaEnchantType.TILLER.getKey(),
                "Tills a 3x3 around initial block",
                EnchantRarityEnum.COMMON,
                1,
                0.8,
                1,
                me.vout.core.arcania.util.ItemHelper::isHoe);
    }

    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }
}