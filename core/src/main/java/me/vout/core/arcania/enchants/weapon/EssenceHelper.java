package me.vout.core.arcania.enchants.weapon;

import me.vout.core.arcania.enchants.tool.MagnetHelper;
import me.vout.core.arcania.enums.ArcaniaEnchantType;
import me.vout.core.arcania.managers.ConfigManager;
import me.vout.core.arcania.providers.ArcaniaProvider;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
import java.util.Map;

public class EssenceHelper {
    public static void onProc(Player player, EntityDeathEvent event, Map<NamespacedKey, Integer> enchants) {
        int essenceLevel = 0;
        if (enchants.containsKey(ArcaniaEnchantType.ESSENCE.getKey()))
            essenceLevel = enchants.get(ArcaniaEnchantType.ESSENCE.getKey());

        float xp = event.getDroppedExp();
        if (essenceLevel > 0)
            xp = getScaledXP(xp, essenceLevel);

        if (enchants.containsKey(ArcaniaEnchantType.MAGNET.getKey())) {
            MagnetHelper.onProc(player, event, xp);
        } else {
            event.setDroppedExp((int)xp);
        }
    }

    public static float getScaledXP(float baseXP, int level) {
        ConfigManager configManager = ArcaniaProvider.getPlugin().getConfigManager();
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
