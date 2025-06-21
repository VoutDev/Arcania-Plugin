package me.vout.spigot.arcania.enchant.weapon;

import me.vout.core.arcania.enums.ArcaniaEnchantType;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;

public class EssenceEnchant extends ArcaniaEnchant {
    public static final EssenceEnchant INSTANCE = new EssenceEnchant();
    private EssenceEnchant() {
        super("Essence",
                ArcaniaEnchantType.ESSENCE.getKey(),
                "Increases xp dropped by mobs",
                EnchantRarityEnum.LEGENDARY,
                3,
                0.3,
                4,
                me.vout.core.arcania.util.ItemHelper::isMeleeWeapon);
    }

    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }
}
