package me.vout.paper.arcania.listener;

import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
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

import me.vout.paper.arcania.Arcania;
import me.vout.paper.arcania.enchant.hoe.HarvesterEnchant;
import me.vout.paper.arcania.enchant.hoe.TillerEnchant;
import me.vout.paper.arcania.enchant.tool.MagnetEnchant;
import me.vout.paper.arcania.enchant.weapon.EssenceEnchant;
import me.vout.paper.arcania.enchant.weapon.FrostbiteEnchant;
import me.vout.paper.arcania.util.ItemHelper;
import me.vout.paper.arcania.util.ToolHelper;

public class ArcaniaEnchantListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir() ||  !ItemHelper.isBlockBreakTool(item.getType())) return;

        Map<Enchantment, Integer> activeEnchants = item.getEnchantments();

        if (activeEnchants.isEmpty()) return;

        ToolHelper.customBreakBlock(player, event, item, activeEnchants);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Block clicked = event.getClickedBlock();

        // Check if the tool is a hoe
        if (!ItemHelper.isHoe(tool.getType())) return;
        Map<Enchantment, Integer> activeEnchants = tool.getEnchantments();

        Enchantment tillerEnchant = Arcania.getEnchantRegistry().get(TillerEnchant.INSTANCE.getKey());

        if (activeEnchants.containsKey(tillerEnchant)) {
            // Check if the clicked block can be tilled
            if (clicked != null && TillerEnchant.isTillable(clicked.getType()))
                TillerEnchant.onProc(player, tool, clicked, event);
        }

        Enchantment harvesterEnchant = Arcania.getEnchantRegistry().get(HarvesterEnchant.INSTANCE.getKey());

        if (activeEnchants.containsKey(harvesterEnchant)) {
            if (clicked != null && HarvesterEnchant.isCrop(clicked.getType()))
                HarvesterEnchant.onProc(player, clicked, tool);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if (player != null) {
            ItemStack item = player.getInventory().getItemInMainHand();
            Map<Enchantment, Integer> enchants = item.getEnchantments();

            if (enchants.isEmpty()) return;

            Enchantment essenceEnchant = Arcania.getEnchantRegistry().get(EssenceEnchant.INSTANCE.getKey());
            Enchantment magnetEnchant = Arcania.getEnchantRegistry().get(MagnetEnchant.INSTANCE.getKey());

            if (enchants.containsKey(essenceEnchant)) {
                EssenceEnchant.onProc(player, event, enchants);
            } else if (enchants.containsKey(magnetEnchant)) {
                MagnetEnchant.onProc(player, event, event.getDroppedExp());
            }
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getProjectile() instanceof Arrow arrow)) return;

        ItemStack bow = event.getBow();
        Map<Enchantment, Integer> enchants = bow.getEnchantments();
        Enchantment frostbiteEnchant = Arcania.getEnchantRegistry().get(FrostbiteEnchant.INSTANCE.getKey());
        if (enchants.containsKey(frostbiteEnchant)) {
            int level = enchants.get(frostbiteEnchant);
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
            Map<Enchantment, Integer> enchants = weapon.getEnchantments();
            if (enchants.isEmpty()) return;
            Enchantment frostbiteEnchant = Arcania.getEnchantRegistry().get(FrostbiteEnchant.INSTANCE.getKey());
            if (enchants.containsKey(frostbiteEnchant))
                FrostbiteEnchant.onProc(event, enchants.get(frostbiteEnchant));
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