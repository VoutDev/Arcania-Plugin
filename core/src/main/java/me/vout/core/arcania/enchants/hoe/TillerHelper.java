package me.vout.core.arcania.enchants.hoe;

import me.vout.core.arcania.util.ToolHelper;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class TillerHelper {
    public static void onProc(Player player, ItemStack tool, Block clicked, PlayerInteractEvent event) {
        // 3x3 area around the clicked block
        outer:
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                Block target = clicked.getRelative(dx, 0, dz);
                if (isTillable(target.getType()) && canTill(target)) {
                    tillBlock(target);
                    if (!ToolHelper.damageTool(player, tool, 1)) break outer;
                }
            }
        }
    }

    public static boolean isTillable(Material mat) {
        return mat == Material.DIRT ||
                mat == Material.ROOTED_DIRT ||
                mat ==Material.GRASS_BLOCK ||
                mat == Material.COARSE_DIRT ||
                mat == Material.DIRT_PATH;
    }

    private static boolean canTill(Block block) {
        return block.getRelative(BlockFace.UP).getType().isAir();
    }

    private static void tillBlock(Block block) {
        Material newType = (block.getType() == Material.COARSE_DIRT) ? Material.DIRT : Material.FARMLAND;
        block.setType(newType, true);
        // Optionally play sound/particle
        //block.getWorld().playSound(block.getLocation(), Sound.ITEM_HOE_TILL, 1.0f, 1.0f);
    }
}
