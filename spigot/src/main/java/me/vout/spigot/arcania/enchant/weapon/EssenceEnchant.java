package me.vout.spigot.arcania.enchant.weapon;

import me.vout.spigot.arcania.Arcania;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;
import me.vout.spigot.arcania.enchant.tool.MagnetEnchant;
import me.vout.spigot.arcania.manager.ConfigManager;
import me.vout.spigot.arcania.util.ItemHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
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
        ConfigManager configManager = Arcania.getConfigManager();
        double k = configManager.getEssenceK();
        List<Double> multipliers = configManager.getEssenceXpMultiplier();

        if (baseXP <= 5) {
            return (float) (baseXP * (1 + multipliers.get(level - 1)));
        } else {
            double bonus = baseXP * multipliers.get(level - 1) * (k / (baseXP + k));
            return (float) (baseXP + bonus);
        }
    }
}
