package me.vout.arcania.enchant.pickaxe;

import me.vout.arcania.enchant.ArcaniaEnchant;
import me.vout.arcania.enchant.EnchantRarityEnum;
import me.vout.arcania.util.ItemHelper;

public class EnrichmentEnchant extends ArcaniaEnchant {
    public static final EnrichmentEnchant INSTANCE = new EnrichmentEnchant();

    private EnrichmentEnchant() {
        super("Enrichment",
                "Increase xp dropped by ores. Effects are applied after smelting.",
                EnchantRarityEnum.RARE,
                3,
                0.7,
                3,
                ItemHelper::isPickaxe);
    }
    
    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }
}