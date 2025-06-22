package me.vout.core.arcania.enchants.weapon;

import me.vout.core.arcania.enchants.tool.MagnetHelper;
import me.vout.core.arcania.enums.ArcaniaEnchantType;
import me.vout.core.arcania.managers.ConfigManager;
import me.vout.core.arcania.providers.ArcaniaProvider;
import me.vout.core.arcania.util.MathHelper;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
import java.util.Map;

public class EssenceHelper {
    public static void onProc(Player player, EntityDeathEvent event, Map<NamespacedKey, Integer> enchants) {
        int essenceLevel = enchants.get(ArcaniaEnchantType.ESSENCE.getKey());
        ConfigManager configManager = ArcaniaProvider.getPlugin().getConfigManager();
        int cutoff = configManager.getEssenceCutoff();
        double k = configManager.getEssenceK();
        List<Double> multipliers = configManager.getEssenceXpMultiplier();
        float xp = event.getDroppedExp();
        if (essenceLevel > 0)
            xp = MathHelper.getScaledXP(xp, cutoff, essenceLevel, k, multipliers);

        if (enchants.containsKey(ArcaniaEnchantType.MAGNET.getKey())) {
            MagnetHelper.onProc(player, event, xp);
        } else {
            event.setDroppedExp((int)xp);
        }
    }
}
