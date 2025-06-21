package me.vout.spigot.arcania.enchant.bow;

import me.vout.core.arcania.enums.ArcaniaEnchantType;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;

public class BlinkEnchant extends ArcaniaEnchant {

    public static  final BlinkEnchant INSTANCE = new BlinkEnchant();

    private BlinkEnchant() {
        super("Blink",
                ArcaniaEnchantType.BLINK.getKey(),
                "Shoot an ender pearl for further range",
                EnchantRarityEnum.LEGENDARY,
                1,
                0.4,
                3,
                me.vout.core.arcania.util.ItemHelper::isRangedWeapon);
    }
    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }
}
