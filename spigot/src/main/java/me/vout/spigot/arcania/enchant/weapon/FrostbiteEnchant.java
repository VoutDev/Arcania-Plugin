package me.vout.spigot.arcania.enchant.weapon;

import me.vout.core.arcania.enums.ArcaniaEnchantType;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;

public class FrostbiteEnchant extends ArcaniaEnchant {
    public static final FrostbiteEnchant INSTANCE = new FrostbiteEnchant();

    private FrostbiteEnchant() {
        super("Frostbite",
                ArcaniaEnchantType.FROSTBITE.getKey(),
                "Slows attacked entity",
                EnchantRarityEnum.UNCOMMON,
                2,
                0.6,
                2,
                me.vout.core.arcania.util.ItemHelper::isCombatWeapon);
    }

    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }
}