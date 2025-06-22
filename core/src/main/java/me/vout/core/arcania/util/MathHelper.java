package me.vout.core.arcania.util;

import java.util.List;

public class MathHelper {
    public static float getScaledXP(float baseXP, int cutoff, int level, double k, List<Double> multipliers) {
        if (baseXP <= cutoff) {
            return (float) (baseXP * (1 + multipliers.get(level - 1)));
        } else {
            double bonus = baseXP * multipliers.get(level - 1) * (k / (baseXP + k));
            return (float) (baseXP + bonus);
        }
    }
}
