package me.vout.spigot.arcania.enchant.tool;

import me.vout.core.arcania.enums.ArcaniaEnchantType;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;

public class ProsperityEnchant extends ArcaniaEnchant {
    public static final ProsperityEnchant INSTANCE = new ProsperityEnchant();

    private ProsperityEnchant() {
        super("Prosperity",
                ArcaniaEnchantType.PROSPERITY.getKey(),
                "Adds chance for double block drops",
                EnchantRarityEnum.RARE,
                3,
                0.4,
                2,
                me.vout.core.arcania.util.ItemHelper::isHarvestingTool);
    }
    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }
}
