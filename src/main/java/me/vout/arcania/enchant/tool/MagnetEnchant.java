package me.vout.arcania.enchant.tool;

import me.vout.arcania.enchant.ArcaniaEnchant;
import me.vout.arcania.enchant.EnchantRarityEnum;
import me.vout.arcania.util.InventoryHelper;
import me.vout.arcania.util.ItemHelper;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class MagnetEnchant extends ArcaniaEnchant {
    public static  final MagnetEnchant INSTANCE = new MagnetEnchant();
    private MagnetEnchant() {
        super("Magnet",
                "Attempts to put item drops directly in inventory",
                EnchantRarityEnum.ULTRA,
                1,
                0.1,
                5,
                ItemHelper::isToolExtended);
    }

    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }

    public static void onProc(Player player, EntityDeathEvent event, int xp) {
        event.setDroppedExp(0);

        InventoryHelper.giveOrDrop(player, event.getDrops().toArray(new ItemStack[0]));
        event.getDrops().clear();
        player.getWorld().spawn(player.getLocation(), ExperienceOrb.class).setExperience(xp);
    }
}
