package me.vout.spigot.arcania.enchant.tool;

import me.vout.core.arcania.enums.ArcaniaEnchantType;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;

public class MagnetEnchant extends ArcaniaEnchant {
    public static  final MagnetEnchant INSTANCE = new MagnetEnchant();
    private MagnetEnchant() {
        super("Magnet",
                ArcaniaEnchantType.MAGNET.getKey(),
                "Attempts to put item drops directly in inventory",
                EnchantRarityEnum.ULTRA,
                1,
                0.1,
                5,
                me.vout.core.arcania.util.ItemHelper::isMainHandTool);
    }

    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }
}
