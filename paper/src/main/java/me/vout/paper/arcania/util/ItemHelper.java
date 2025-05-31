package me.vout.paper.arcania.util;

import org.bukkit.Material;

public class ItemHelper {

    // TODO: remove this and use tags instead
    public static boolean isTool(Material mat) {
        String name = mat.name();
        return name.endsWith("_PICKAXE") ||
                name.endsWith("_AXE") ||
                name.endsWith("_SHOVEL") ||
                name.endsWith("_HOE") ||
                name.endsWith("SHEARS");
    }
    // TODO: remove this and use tags instead
    public static boolean isBlockBreakTool(Material mat) {
        String name = mat.name();
        return name.endsWith("_PICKAXE") ||
                name.endsWith("_SHOVEL") ||
                name.endsWith("_AXE") ||
                name.endsWith("SHEARS");
    }
    // TODO: remove this and use tags instead
    public static boolean isToolExtended(Material mat) {
        return isTool(mat) || isSword(mat) || isRangedWeapon(mat) || isTrident(mat) || isHoe(mat);
    }
    // TODO: remove this and use tags instead
    public static boolean isDigger(Material mat) {
        return mat.name().endsWith("_PICKAXE") ||
                mat.name().endsWith("_SHOVEL");
    }
    // TODO: remove this and use tags instead
    public static boolean isPickaxe(Material mat) {
        return mat.name().endsWith("_PICKAXE");
    }
    // TODO: remove this and use tags instead
    public static boolean isHoe(Material mat) {
        return mat.name().endsWith("_HOE");
    }
    // TODO: remove this and use tags instead
    public static boolean isSword(Material mat) {
        return mat.name().endsWith("_SWORD");
    }
    // TODO: remove this and use tags instead
    public static boolean isTrident(Material mat) {
        return mat.name().endsWith("TRIDENT");
    }
    // TODO: remove this and use tags instead
    public static boolean isWeapon(Material mat) {
        String name = mat.name();
        return name.endsWith("_SWORD") ||
                name.endsWith("_AXE") ||
                name.endsWith("BOW") ||
                name.endsWith("CROSSBOW");
    }
    // TODO: remove this and use tags instead
    public static boolean isRangedWeapon(Material mat) {
        String name = mat.name();
        return name.endsWith("BOW") ||
                name.endsWith("CROSSBOW");
    }

    // TODO: remove this and use tags instead
    public static boolean isMeleeWeapon(Material mat) {
        String name = mat.name();
        return name.endsWith("_SWORD") ||
                name.endsWith("_AXE");
    }

    // TODO: remove this and use tags instead
    public static boolean isArmor(Material mat) {
        String name = mat.name();
        return name.endsWith("_HELMET") ||
                name.endsWith("_CHESTPLATE") ||
                name.endsWith("_LEGGINGS") ||
                name.endsWith("_BOOTS");
    }


}
