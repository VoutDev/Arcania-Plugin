package me.vout.core.arcania.enchants.tool;

import me.vout.core.arcania.util.InventoryHelper;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class MagnetHelper {
    public static void onProc(Player player, EntityDeathEvent event, float xp) {
        event.setDroppedExp(0);

        InventoryHelper.giveOrDrop(player, event.getDrops().toArray(new ItemStack[0]));
        event.getDrops().clear();
        player.getWorld().spawn(player.getLocation(), ExperienceOrb.class).setExperience((int)xp);
    }
}
