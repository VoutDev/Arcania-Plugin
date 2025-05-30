package me.vout.spigot.arcania.enchant.tool;

import org.bukkit.enchantments.Enchantment;

import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;
import me.vout.spigot.arcania.util.ItemHelper;

public class SmeltEnchant extends ArcaniaEnchant {
    public static final SmeltEnchant INSTANCE = new SmeltEnchant();
    private SmeltEnchant() {
        super("Smelt",
                "Instantly smelt the block into it's smelting result",
                EnchantRarityEnum.ULTRA,
                1,
                0.1,
                5,
                ItemHelper::isPickaxe);
    }

    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }

    @Override
    public boolean isCompatibleWith(Enchantment enchantment) {
        return enchantment != Enchantment.SILK_TOUCH;
    }
}
