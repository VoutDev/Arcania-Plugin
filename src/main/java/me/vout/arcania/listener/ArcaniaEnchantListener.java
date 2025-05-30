package me.vout.arcania.listener;

import me.vout.arcania.Arcania;
import me.vout.arcania.enchant.ArcaniaEnchant;
import me.vout.arcania.enchant.hoe.HarvesterEnchant;
import me.vout.arcania.enchant.hoe.TillerEnchant;
import me.vout.arcania.enchant.pickaxe.EnrichmentEnchant;
import me.vout.arcania.enchant.pickaxe.QuarryEnchant;
import me.vout.arcania.enchant.pickaxe.VeinminerEnchant;
import me.vout.arcania.enchant.tool.MagnetEnchant;
import me.vout.arcania.enchant.weapon.EssenceEnchant;
import me.vout.arcania.enchant.weapon.FrostbiteEnchant;
import me.vout.arcania.util.EnchantHelper;
import me.vout.arcania.util.ItemHelper;
import me.vout.arcania.util.ToolHelper;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class ArcaniaEnchantListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir() ||  !ItemHelper.isBlockBreakTool(item.getType())) return;

        Map<ArcaniaEnchant, Integer> activeEnchants = EnchantHelper.getItemEnchants(item);
        if (activeEnchants.isEmpty()) return;

        if (activeEnchants.containsKey(QuarryEnchant.INSTANCE) &&
                event.getBlock().isPreferredTool(item)) {
            QuarryEnchant.onProc(player, item, event, activeEnchants);
        }
        if (!event.isCancelled() &&
                activeEnchants.containsKey(VeinminerEnchant.INSTANCE)) {
            VeinminerEnchant.onProc(player, event, activeEnchants);
        }

        if (!event.isCancelled() &&
        activeEnchants.containsKey(EnrichmentEnchant.INSTANCE)) {
            EnrichmentEnchant.onProc(player, item, event, activeEnchants);
        }

        if (!event.isCancelled() &&
                activeEnchants.containsKey(MagnetEnchant.INSTANCE)) {
            ToolHelper.customBreakBlock(player, event.getBlock(), item, activeEnchants);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Block clicked = event.getClickedBlock();

        // Check if the tool is a hoe
        if (!ItemHelper.isHoe(tool.getType())) return;
        Map<ArcaniaEnchant, Integer> activeEnchants = EnchantHelper.getItemEnchants(tool);

        if (activeEnchants.containsKey(TillerEnchant.INSTANCE)) {
            // Check if the clicked block can be tilled
            if (clicked != null && TillerEnchant.isTillable(clicked.getType()))
                TillerEnchant.onProc(player, tool, clicked, event);
        }
        if (activeEnchants.containsKey(HarvesterEnchant.INSTANCE)) {
            if (clicked != null && HarvesterEnchant.isCrop(clicked.getType()))
                HarvesterEnchant.onProc(player, clicked, tool);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if (player != null) {
            ItemStack item = player.getInventory().getItemInMainHand();
            Map<ArcaniaEnchant, Integer> enchants = EnchantHelper.getItemEnchants(item);

            if (enchants.isEmpty()) return;
            boolean handled = false;
            if (enchants.containsKey(EssenceEnchant.INSTANCE)) {
                EssenceEnchant.onProc(player, event, enchants);
                handled = true;
            }
            if (!handled && enchants.containsKey(MagnetEnchant.INSTANCE)) {
                MagnetEnchant.onProc(player, event, event.getDroppedExp());
            }
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getProjectile() instanceof Arrow arrow)) return;

        ItemStack bow = event.getBow();
        Map<ArcaniaEnchant, Integer> enchants = EnchantHelper.getItemEnchants(bow);
        if (enchants.containsKey(FrostbiteEnchant.INSTANCE)) {
            int level = enchants.get(FrostbiteEnchant.INSTANCE);
            arrow.getPersistentDataContainer().set(
                    new NamespacedKey(Arcania.getInstance(), FrostbiteEnchant.FrostBiteArrowEnum.FROSTBITE_ARROW.toString()),
                    PersistentDataType.INTEGER,
                    level
            );
        }
    }

    @EventHandler
    public void onentityDamgeByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        Entity damager = event.getDamager();
        Entity victim = event.getEntity();
        if (damager instanceof Player player &&
                victim instanceof LivingEntity) {
            ItemStack weapon = player.getInventory().getItemInMainHand();
            if (!ItemHelper.isMeleeWeapon(weapon.getType())) return;
            Map<ArcaniaEnchant, Integer> enchants = EnchantHelper.getItemEnchants(weapon);
            if (enchants.isEmpty()) return;
            if (enchants.containsKey(FrostbiteEnchant.INSTANCE))
                FrostbiteEnchant.onProc(event, enchants.get(FrostbiteEnchant.INSTANCE));
        }
        else if (damager instanceof Arrow arrow
                && arrow.getShooter() instanceof Player
                && victim instanceof LivingEntity) {
            NamespacedKey key = new NamespacedKey(Arcania.getInstance(), FrostbiteEnchant.FrostBiteArrowEnum.FROSTBITE_ARROW.toString());
            Integer level = arrow.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
            if (level != null && level > 0) {
                FrostbiteEnchant.onProc(event, level);
            }
        }
    }
}