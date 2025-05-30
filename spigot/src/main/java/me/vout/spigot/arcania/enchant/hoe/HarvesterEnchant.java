package me.vout.spigot.arcania.enchant.hoe;

import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;
import me.vout.spigot.arcania.enchant.tool.MagnetEnchant;
import me.vout.spigot.arcania.util.EnchantHelper;
import me.vout.spigot.arcania.util.InventoryHelper;
import me.vout.spigot.arcania.util.ItemHelper;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;

public class HarvesterEnchant extends ArcaniaEnchant {
    public  static  final HarvesterEnchant INSTANCE = new HarvesterEnchant();
    private HarvesterEnchant() {
        super("Harvester",
                "Right click to collect and replant crop",
                EnchantRarityEnum.COMMON,
                1,
                0.6,
                1,
                ItemHelper::isHoe);
    }
    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
    }

    public static void onProc(Player player, Block crop, ItemStack hoe) {
        if (crop.getBlockData() instanceof Ageable ageable) {
            if (ageable.getAge() != ageable.getMaximumAge()) return;
            Collection<ItemStack> drops = crop.getDrops(hoe);

            Map<ArcaniaEnchant, Integer> enchants = EnchantHelper.getItemEnchants(hoe);
            boolean hasMagnet = enchants.containsKey(MagnetEnchant.INSTANCE);
            if (hasMagnet)
                InventoryHelper.giveOrDrop(player, drops.toArray(new ItemStack[0]));

            else {
                World world = crop.getWorld();
                for (ItemStack drop : drops) {
                    world.dropItemNaturally(crop.getLocation().add(0.5, 0.5, 0.5), drop);
                }
            }
            ageable.setAge(0);
            crop.setBlockData((BlockData) ageable);
        }
    }

    public static boolean isCrop(Material mat) {
        return mat == Material.WHEAT
                || mat == Material.CARROTS
                || mat == Material.POTATOES
                || mat == Material.BEETROOTS
                || mat == Material.NETHER_WART;
    }
}
