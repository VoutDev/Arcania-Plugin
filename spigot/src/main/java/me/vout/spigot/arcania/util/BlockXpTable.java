package me.vout.spigot.arcania.util;

import org.bukkit.Material;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntSupplier;

public class BlockXpTable {
    private static final Map<Material, IntSupplier> XP_MAP = Map.ofEntries(
            Map.entry(Material.SPAWNER, () -> ThreadLocalRandom.current().nextInt(15, 44)),
            Map.entry(Material.COAL_ORE, () -> ThreadLocalRandom.current().nextInt(0, 3)),
            Map.entry(Material.DEEPSLATE_COAL_ORE, () -> ThreadLocalRandom.current().nextInt(0, 3)),
            Map.entry(Material.DIAMOND_ORE, () -> ThreadLocalRandom.current().nextInt(3, 8)),
            Map.entry(Material.DEEPSLATE_DIAMOND_ORE, () -> ThreadLocalRandom.current().nextInt(3, 8)),
            Map.entry(Material.EMERALD_ORE, () -> ThreadLocalRandom.current().nextInt(3, 8)),
            Map.entry(Material.DEEPSLATE_EMERALD_ORE, () -> ThreadLocalRandom.current().nextInt(3, 8)),
            Map.entry(Material.LAPIS_ORE, () -> ThreadLocalRandom.current().nextInt(2, 6)),
            Map.entry(Material.DEEPSLATE_LAPIS_ORE, () -> ThreadLocalRandom.current().nextInt(2, 6)),
            Map.entry(Material.NETHER_QUARTZ_ORE, () -> ThreadLocalRandom.current().nextInt(2, 6)),
            Map.entry(Material.NETHER_GOLD_ORE, () -> ThreadLocalRandom.current().nextInt(0, 2)),
            Map.entry(Material.REDSTONE_ORE, () -> ThreadLocalRandom.current().nextInt(1, 6)),
            Map.entry(Material.DEEPSLATE_REDSTONE_ORE, () -> ThreadLocalRandom.current().nextInt(1, 6)),
            Map.entry(Material.SCULK, () -> 1),
            Map.entry(Material.SCULK_CATALYST, () -> 5),
            Map.entry(Material.SCULK_SHRIEKER, () -> 5),
            Map.entry(Material.SCULK_SENSOR, () -> 5)
    );

    public static int getXp(Material material) {
        IntSupplier supplier = XP_MAP.get(material);
        return supplier != null ? supplier.getAsInt() : 0;
    }
}
