package me.vout.core.arcania.listener;

import me.vout.core.arcania.enchants.bow.BlinkHelper;
import me.vout.core.arcania.enchants.hoe.HarvesterHelper;
import me.vout.core.arcania.enchants.hoe.TillerHelper;
import me.vout.core.arcania.enchants.tool.MagnetHelper;
import me.vout.core.arcania.enchants.weapon.EssenceHelper;
import me.vout.core.arcania.enchants.weapon.FrostbiteHelper;
import me.vout.core.arcania.enums.ArcaniaEnchantType;
import me.vout.core.arcania.providers.ArcaniaProvider;
import me.vout.core.arcania.service.BlockTaggingService;
import me.vout.core.arcania.util.InventoryHelper;
import me.vout.core.arcania.util.ItemHelper;
import me.vout.core.arcania.util.ToolHelper;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class ArcaniaEnchantListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        boolean useVanillaBreak = false;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        BlockTaggingService blockTaggingService = ArcaniaProvider.getPlugin().getBlockTaggingService();

        if (item.getType().isAir() || !ItemHelper.isHarvestingTool(item.getType())) return;

        Map<NamespacedKey, Integer> activeEnchants = ArcaniaProvider.getPlugin().getEnchantStrategy().getActiveEnchantments(item);

        if (activeEnchants.isEmpty()) {
            if (blockTaggingService.isPlayerPlaced(event.getBlock()))
                blockTaggingService.removePlayerPlacedTag(event.getBlock());
            return;
        }

        if (event.getBlock().getState() instanceof Container container)
            useVanillaBreak = InventoryHelper.isContainerBreak(event.getBlock(), container, player, activeEnchants);
        else if (activeEnchants.containsKey(ArcaniaEnchantType.MAGNET.getKey()) &&
                event.getBlock().getState() instanceof BlockInventoryHolder blockInventoryHolder) // for Chiseled Bookshelf
            useVanillaBreak = me.vout.core.arcania.util.InventoryHelper.processContainerInventory(player, blockInventoryHolder.getInventory());

        if (!useVanillaBreak) {

            ToolHelper.customBreakBlock(player, event, item, activeEnchants);
        }

        if (blockTaggingService.isPlayerPlaced(event.getBlock()))
            blockTaggingService.removePlayerPlacedTag(event.getBlock());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;
        ArcaniaProvider.getPlugin().getBlockTaggingService().tagBlockAsPlayerPlaced(event.getBlockPlaced());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Block clicked = event.getClickedBlock();

        // Check if the tool is a hoe
        if (!me.vout.core.arcania.util.ItemHelper.isHoe(tool.getType())) return;
        Map<NamespacedKey, Integer> activeEnchants = ArcaniaProvider.getPlugin().getEnchantStrategy().getActiveEnchantments(tool); //EnchantHelper.getItemEnchants(tool);

        if (activeEnchants.containsKey(ArcaniaEnchantType.TILLER.getKey())) {
            // Check if the clicked block can be tilled
            if (clicked != null && TillerHelper.isTillable(clicked.getType()))
                TillerHelper.onProc(player, tool, clicked, event);
        }
        if (activeEnchants.containsKey(ArcaniaEnchantType.HARVESTER.getKey())) {
            if (clicked != null && HarvesterHelper.isCrop(clicked.getType()))
                HarvesterHelper.onProc(player, clicked, tool);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if (player != null) {
            ItemStack item = player.getInventory().getItemInMainHand();
            Map<NamespacedKey, Integer> activeEnchants = ArcaniaProvider.getPlugin().getEnchantStrategy().getActiveEnchantments(item); //EnchantHelper.getItemEnchants(item);

            if (activeEnchants.isEmpty()) return;
            boolean handled = false;
            if (activeEnchants.containsKey(ArcaniaEnchantType.ESSENCE.getKey())) {
                EssenceHelper.onProc(player, event, activeEnchants);
                handled = true;
            }
            if (!handled && activeEnchants.containsKey(ArcaniaEnchantType.MAGNET.getKey())) {
                MagnetHelper.onProc(player, event, event.getDroppedExp());
            }
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getProjectile() instanceof Arrow arrow)) return;
        ItemStack offhand = player.getInventory().getItemInOffHand();

        ItemStack bow = event.getBow();
        if (bow == null) return;
        Map<NamespacedKey, Integer> activeEnchants = ArcaniaProvider.getPlugin().getEnchantStrategy().getActiveEnchantments(bow); //EnchantHelper.getItemEnchants(bow);
        if (activeEnchants.containsKey(ArcaniaEnchantType.FROSTBITE.getKey())) {
            int level = activeEnchants.get(ArcaniaEnchantType.FROSTBITE.getKey());
            arrow.getPersistentDataContainer().set(
                    new NamespacedKey(ArcaniaProvider.getPlugin().getJavaPlugin(), FrostbiteHelper.FrostBiteArrowEnum.FROSTBITE_ARROW.toString()),
                    PersistentDataType.INTEGER,
                    level
            );
        }
        if (activeEnchants.containsKey(ArcaniaEnchantType.BLINK.getKey()) && offhand.getType() == Material.ENDER_PEARL) {
            BlinkHelper.onProc(player, event, offhand);
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
            if (!me.vout.core.arcania.util.ItemHelper.isMeleeWeapon(weapon.getType())) return;
            Map<NamespacedKey, Integer> activeEnchants = ArcaniaProvider.getPlugin().getEnchantStrategy().getActiveEnchantments(weapon); //EnchantHelper.getItemEnchants(weapon);
            if (activeEnchants.isEmpty()) return;
            if (activeEnchants.containsKey(ArcaniaEnchantType.FROSTBITE.getKey()))
                FrostbiteHelper.onProc(event, activeEnchants.get(ArcaniaEnchantType.FROSTBITE.getKey()));
        } else if (damager instanceof Arrow arrow
                && arrow.getShooter() instanceof Player
                && victim instanceof LivingEntity) {
            NamespacedKey key = new NamespacedKey(ArcaniaProvider.getPlugin().getJavaPlugin(), FrostbiteHelper.FrostBiteArrowEnum.FROSTBITE_ARROW.toString());
            Integer level = arrow.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
            if (level != null && level > 0) {
                FrostbiteHelper.onProc(event, level);
            }
        }
    }
}
