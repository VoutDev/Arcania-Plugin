package me.vout.arcania.util;

import me.vout.arcania.Arcania;
import me.vout.arcania.enchant.ArcaniaEnchant;
import me.vout.arcania.enchant.pickaxe.EnrichmentEnchant;
import me.vout.arcania.enchant.pickaxe.QuarryEnchant;
import me.vout.arcania.enchant.pickaxe.VeinminerEnchant;
import me.vout.arcania.enchant.tool.MagnetEnchant;
import me.vout.arcania.enchant.tool.SmeltEnchant;
import me.vout.arcania.enchant.weapon.EssenceEnchant;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;


public class ToolHelper {
    public static void customBreakBlock(
            Player player,
            BlockBreakEvent event,
            ItemStack tool,
            Map<ArcaniaEnchant, Integer> enchants
    ) {
        // get the block to break
        Block block = event.getBlock();

        // prepare xp to give
        final float[] xpToGive = {event.getExpToDrop()};

        // prepare list of blocks to attempt to break and add the block to break
        List<Block> blocksToAttemptToBreak = new ArrayList<>();
        blocksToAttemptToBreak.add(block);

        // first process quarry to collect which blocks to break
        boolean hasQuarry = enchants.containsKey(QuarryEnchant.INSTANCE);
        
        if (hasQuarry) {
            // Arcania.getInstance().getLogger().log(Level.INFO, "hasQuarry!");
            List<Block> quarryBlocks = QuarryEnchant.getBlocksToBreak(player, tool, event, blocksToAttemptToBreak);
            blocksToAttemptToBreak.addAll(quarryBlocks);
        }

        // then process veinminer to collect which blocks to break based on the quarry
        boolean hasVeinminer = enchants.containsKey(VeinminerEnchant.INSTANCE);
        if (hasVeinminer) {
            // Arcania.getInstance().getLogger().log(Level.INFO, "hasVeinminer!");
            List<Block> veinminerQueue = new ArrayList<>(blocksToAttemptToBreak);
            for (Block blockToBreak : veinminerQueue) {
                if (VeinminerEnchant.isVeinMineBlock(blockToBreak.getType())) {
                    List<Block> veinBlocks = VeinminerEnchant.getBlocksToBreak(player, blockToBreak, enchants, blocksToAttemptToBreak);
                    // Arcania.getInstance().getLogger().log(Level.INFO, "veinBlocks: " + veinBlocks.size());
                    blocksToAttemptToBreak.addAll(veinBlocks);
                }
            }
        }
        
        ArrayList<Block> blocksToBreak = new ArrayList<>();
        // loop through each block and add them to the brokenBlocks list until the tool is broken
        blocksToAttemptToBreak.stream().distinct().forEach(blockToBreak -> {
            blocksToBreak.add(blockToBreak);
            if (!ToolHelper.damageTool(player, tool, 1)) {
                return;
            }
        });

        // then process smelting to modify the drops
        boolean hasSmelting = enchants.containsKey(SmeltEnchant.INSTANCE);
        List<ItemStack> blocksToDrop = new ArrayList<>();
        
        // for each block to break, get the drops and add them to the blocksToDrop list
        // if we have smelting enchant, and the block has a furnace recipe, add the experience to the xpToGive array and add the smelting result to the blocksToDrop list
        // if the block does not have a furnace recipe, add the drop to the blocksToDrop list
        final float[] smeltingExperience = {0};
        blocksToBreak.stream().forEach(blockToBreak -> {
            blockToBreak.getDrops(tool).forEach(itemStack -> {
                if (hasSmelting) {
                    FurnaceRecipe furnaceRecipe = ItemHelper.getFurnaceRecipeForItemStack(itemStack);
                    if (furnaceRecipe != null) {
                        // Arcania.getInstance().getLogger().log(Level.INFO, "hasSmelting: " + itemStack.getType().name() + " " + itemStack.getAmount());
                        ItemStack smeltedItemStack = furnaceRecipe.getResult().clone();
                        smeltedItemStack.setAmount(itemStack.getAmount());
                        blocksToDrop.add(smeltedItemStack);
                        float experienceToGive = furnaceRecipe.getExperience();
                        smeltingExperience[0] += experienceToGive;
                    } else {
                        blocksToDrop.add(itemStack);
                    }
                } else {
                    blocksToDrop.add(itemStack);
                }
            });
        });

        if (smeltingExperience[0] > 0) {
            // Arcania.getInstance().getLogger().log(Level.INFO, "smelting experience: " + smeltingExperience[0]);
            xpToGive[0] += smeltingExperience[0];
        }


        // break all the blocks
        blocksToBreak.forEach(blockToBreak -> {
            blockToBreak.setType(Material.AIR, true);
        });

        // then process magnet to collect the drops + xp
        boolean hasMagnet = enchants.containsKey(MagnetEnchant.INSTANCE);

        // give the drops
        blocksToDrop.forEach(itemStack -> {
            // Arcania.getInstance().getLogger().log(Level.INFO, "itemStack: " + itemStack.getType().name() + " " + itemStack.getAmount());
            if (hasMagnet) {
                InventoryHelper.giveOrDrop(player, itemStack);
            } else {
                Location dropLoc = block.getLocation().add(0.5, 0.5, 0.5);
                block.getWorld().dropItemNaturally(dropLoc, itemStack);
            }
        });

        // process enrichment to modify the xp before giving it
        boolean hasEnrichment = enchants.containsKey(EnrichmentEnchant.INSTANCE);
        // Arcania.getInstance().getLogger().log(Level.INFO, "hasEnrichment: " + hasEnrichment);
        
        // give the xp
        if (xpToGive[0] > 0) {
            
            if (hasEnrichment) {
                // Arcania.getInstance().getLogger().log(Level.INFO, "xpToGive before enrichment: " + xpToGive[0]);
                // Arcania.getInstance().getLogger().log(Level.INFO, "enrichment level: " + enchants.get(EnrichmentEnchant.INSTANCE));
                xpToGive[0] = EssenceEnchant.getScaledXP(xpToGive[0], enchants.get(EnrichmentEnchant.INSTANCE)); 
                // Arcania.getInstance().getLogger().log(Level.INFO, "xpToGive after enrichment: " + xpToGive[0]);
            } else {
                // Arcania.getInstance().getLogger().log(Level.INFO, "xpToGive:" + xpToGive[0]);
            }
            Location xpLoc = hasMagnet ? player.getLocation() : block.getLocation();
            block.getWorld().spawn(xpLoc, ExperienceOrb.class).setExperience((int)xpToGive[0]);
        }

        // clear the drops from the original block
        block.getDrops().clear();

        return;
    }

    public static boolean damageTool(Player player, ItemStack tool, int amount) {
        int unbreaking = tool.getEnchantmentLevel(Enchantment.UNBREAKING);
        int actualDamage = 0;

        for (int i = 0; i < amount; i++) {
            if (unbreaking > 0) {
                // Chance to ignore damage: unbreaking / (unbreaking + 1)
                if (ThreadLocalRandom.current().nextInt(unbreaking + 1) == 0)
                    actualDamage++; // Only apply damage if random == 0
            } else
                actualDamage++;
        }

        // For 1.13+ (ItemMeta.setDamage)
        ItemMeta meta = tool.getItemMeta();
        if (meta instanceof Damageable damageable) {
            int newDamage = damageable.getDamage() + actualDamage;
            damageable.setDamage(newDamage);
            tool.setItemMeta(meta);

            if (newDamage >= tool.getType().getMaxDurability()) {
                player.getInventory().remove(tool);
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                return false;
            }
        }
        return true;
    }
}