package me.vout.arcania.enchant.weapon;

import me.vout.arcania.enchant.ArcaniaEnchant;
import me.vout.arcania.enchant.EnchantRarityEnum;
import me.vout.arcania.enchant.tool.MagnetEnchant;
import me.vout.arcania.util.ItemHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

public class EssenceEnchant extends ArcaniaEnchant {
    public static final EssenceEnchant INSTANCE = new EssenceEnchant();
    private EssenceEnchant() {
        super("Essence",
                "Increases xp dropped by mobs",
                EnchantRarityEnum.LEGENDARY,
                3,
                0.3,
                4,
                ItemHelper::isMeleeWeapon);
    }

    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }

    public static void onProc(Player player, EntityDeathEvent event, Map<ArcaniaEnchant, Integer> enchants) {
        int essenceLevel = 0;
        if (enchants.containsKey(EssenceEnchant.INSTANCE))
            essenceLevel = enchants.get(EssenceEnchant.INSTANCE);

        float xp = event.getDroppedExp();
        if (essenceLevel > 0)
            xp = getScaledXP(xp, essenceLevel);

        if (enchants.containsKey(MagnetEnchant.INSTANCE)) {
           MagnetEnchant.onProc(player, event, xp);
        } else {
            event.setDroppedExp((int)xp);
        }
    }

    public static float getScaledXP(float baseXP, int level) {
        double[] multipliers = {0.75, 1.0, 1.5};
        double k = 10.0;

        if (baseXP <= 5) {
            return (int) Math.round(baseXP * (1 + multipliers[level - 1]));
        } else {
            double bonus = baseXP * multipliers[level - 1] * (k / (baseXP + k));
            return baseXP + (int) Math.round(bonus);
        }
    }
}
