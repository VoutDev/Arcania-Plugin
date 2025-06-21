package me.vout.core.arcania.enchants.bow;

import org.bukkit.Sound;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public class BlinkHelper {
    public static void onProc(Player player, EntityShootBowEvent event, ItemStack offhand) {
        event.setCancelled(true);
        EnderPearl pearl = player.launchProjectile(EnderPearl.class);
        pearl.setVelocity(event.getProjectile().getVelocity());

        offhand.setAmount(offhand.getAmount() - 1);
        player.getInventory().setItemInOffHand(offhand);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_PEARL_THROW, 0.4f, 1.0f);
    }
}
