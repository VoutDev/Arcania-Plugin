package me.vout.spigot.arcania.enchant.hoe;

import me.vout.core.arcania.enums.ArcaniaEnchantType;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;

public class HarvesterEnchant extends ArcaniaEnchant {
    public  static  final HarvesterEnchant INSTANCE = new HarvesterEnchant();
    private HarvesterEnchant() {
        super("Harvester",
                ArcaniaEnchantType.HARVESTER.getKey(),
                "Right click to collect and replant crop",
                EnchantRarityEnum.COMMON,
                1,
                0.6,
                1,
                me.vout.core.arcania.util.ItemHelper::isHoe);
    }
    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }
}
