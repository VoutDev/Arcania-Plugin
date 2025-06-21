package me.vout.spigot.arcania.enchant.pickaxe;

import me.vout.core.arcania.enums.ArcaniaEnchantType;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;

public class QuarryEnchant extends ArcaniaEnchant {

    public static final QuarryEnchant INSTANCE = new QuarryEnchant();
    private QuarryEnchant() {
        super("Quarry",
                ArcaniaEnchantType.QUARRY.getKey(),
                "Mines a 3x3 around broken block",
                EnchantRarityEnum.RARE,
                1,
                0.5,
                2,
                me.vout.core.arcania.util.ItemHelper::isDigger);
    }

    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }
}
