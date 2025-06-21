package me.vout.spigot.arcania.enchant.pickaxe;

import me.vout.core.arcania.enums.ArcaniaEnchantType;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;

public class VeinminerEnchant extends ArcaniaEnchant {
    public static final VeinminerEnchant INSTANCE = new VeinminerEnchant();
    private VeinminerEnchant() {
        super("Vein Miner",
                ArcaniaEnchantType.VEIN_MINER.getKey(),
                "Mines connecting veins of initial block",
                EnchantRarityEnum.LEGENDARY,
                3,
                0.2,
                4,
                me.vout.core.arcania.util.ItemHelper::isPickaxe);
    }

    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }
}