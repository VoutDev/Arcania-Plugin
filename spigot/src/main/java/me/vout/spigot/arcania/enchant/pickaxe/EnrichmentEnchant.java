package me.vout.spigot.arcania.enchant.pickaxe;

import me.vout.core.arcania.enums.ArcaniaEnchantType;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;

public class EnrichmentEnchant extends ArcaniaEnchant {
    public static final EnrichmentEnchant INSTANCE = new EnrichmentEnchant();

    private EnrichmentEnchant() {
        super("Enrichment",
                ArcaniaEnchantType.ENRICHMENT.getKey(),
                "Increase xp dropped by ores. Effects are applied after smelting.",
                EnchantRarityEnum.RARE,
                3,
                0.7,
                3,
                me.vout.core.arcania.util.ItemHelper::isPickaxe);
    }
    
    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }
}