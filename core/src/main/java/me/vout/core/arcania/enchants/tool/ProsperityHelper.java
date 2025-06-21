package me.vout.core.arcania.enchants.tool;

import java.util.Random;

public class ProsperityHelper {
    public static boolean shouldApplyEffect(int level) {
        Random random = new Random();
        float chance = switch (level) {
            case 1 -> 0.1f; // 10% chance
            case 2 -> 0.2f; // 20% chance
            case 3 -> 0.35f; // 35% chance
            default -> 0f;
        };
        return random.nextFloat() < chance;
    }
}
