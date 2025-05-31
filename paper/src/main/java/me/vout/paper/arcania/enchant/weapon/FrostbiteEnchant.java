package me.vout.paper.arcania.enchant.weapon;

import me.vout.paper.arcania.Arcania;
import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.papermc.paper.enchantments.EnchantmentRarity;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.ItemTypeKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;

import java.util.Random;
import java.util.Set;

public class FrostbiteEnchant extends ArcaniaEnchant {
    public static final FrostbiteEnchant INSTANCE = new FrostbiteEnchant();

    private FrostbiteEnchant() {
        super(
            "Frostbite", 
            "frostbite", 
            "Slows attacked entity", 
            2, 
            1, 
            10,
            10,
            15,
            1
        );
    }

    public enum FrostBiteArrowEnum {
        FROSTBITE_ARROW("frostbite_arrow");

        private final String key;
        FrostBiteArrowEnum(String key) {
            this.key = key;
        }
        @Override
        public String toString() {
            return key;
        }
    }

    public static void onProc(EntityDamageByEntityEvent event, int level) {
        if (!shouldApplyEffect(level) ||
                !(event.getEntity() instanceof LivingEntity)) return;
        if (event.getEntity() instanceof Player playerVictim) {
            playerVictim.setFreezeTicks(100); // frozen for 5s
            startSnowTrail(playerVictim);
        }
        else if (event.getEntity() instanceof Mob mobVictim) {
            mobVictim.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 3));
            startSnowTrail(mobVictim);
        }
    }

    private static boolean shouldApplyEffect(int level) {
        Random random = new Random();
        float chance = switch (level) {
            case 1 -> 0.2f; // 20% chance
            case 2 -> 0.35f; // 35% chance
            default -> 0f;
        };
        return random.nextFloat() < chance;
    }

    public static void startSnowTrail(LivingEntity entity) {
        new BukkitRunnable() {
            @Override
            public void run() {
                boolean active;
                if (entity instanceof Player player)
                    active = player.getFreezeTicks() > 0;
                else
                    active = entity.hasPotionEffect(PotionEffectType.SLOWNESS);

                if (!active || entity.isDead()) {
                    this.cancel();
                    return;
                }

                Location loc = entity.getLocation().getBlock().getLocation();
                Block blockBelow = loc.clone().subtract(0, 1, 0).getBlock();
                Block blockAtFeet = loc.getBlock();

                if (blockAtFeet.getType() == Material.AIR &&
                        blockBelow.getType().isSolid() &&
                        blockBelow.getType() != Material.SNOW &&
                        blockBelow.getType() != Material.SNOW_BLOCK) {
                    blockAtFeet.setType(Material.SNOW);

                    // Schedule removal after 5 seconds (100 ticks)
                    Bukkit.getScheduler().runTaskLater(Arcania.getInstance(), () -> {
                        // Only remove if it's still snow (not mined or replaced)
                        if (blockAtFeet.getType() == Material.SNOW) {
                            blockAtFeet.setType(Material.AIR);
                        }
                    }, 100L);
                }
            }
        }.runTaskTimer(Arcania.getInstance(), 0L, 5L); // Runs every 5 ticks (0.25s)
    }
    @Override
    public @NotNull String getTranslationKey() {
        return "enchantment." + NAMESPACE + "." + key;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack arg0) {
        return true;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment arg0) {
        return false;
    }

    @Override
    public @NotNull Component description() {
        return Component.text(description);
    }

    @Override
    public @NotNull Component displayName(int arg0) {
        return Component.text(name);
    }

    @Override
    public @NotNull Set<EquipmentSlotGroup> getActiveSlotGroups() {
        return Set.of(EquipmentSlotGroup.MAINHAND);
    }

    @Override
    public int getAnvilCost() {
        return anvilCost;
    }

    @Override
    public float getDamageIncrease(int arg0, @NotNull EntityCategory arg1) {
        return 0;
    }

    @Override
    public float getDamageIncrease(int arg0, @NotNull EntityType arg1) {
        return 0;
    }
    
    @Override
    public @NotNull RegistryKeySet<Enchantment> getExclusiveWith() {
        return RegistrySet.keySet(EnchantmentKeys.create(Key.key(NAMESPACE, key)).registryKey());
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getMaxModifiedCost(int arg0) {
        return maxModifiedCost;
    }

    @Override
    public int getMinModifiedCost(int arg0) {
        return minModifiedCost;
    }

    @Override
    public @Nullable RegistryKeySet<ItemType> getPrimaryItems() {
        return RegistrySet.keySet(RegistryKey.ITEM, ItemTypeKeys.ENCHANTED_BOOK, ItemTypeKeys.STONE_SWORD, ItemTypeKeys.IRON_SWORD, ItemTypeKeys.GOLDEN_SWORD, ItemTypeKeys.DIAMOND_SWORD, ItemTypeKeys.NETHERITE_SWORD, ItemTypeKeys.BOW, ItemTypeKeys.CROSSBOW);
    }

    @Override
    public @NotNull EnchantmentRarity getRarity() {
        return EnchantmentRarity.RARE;
    }

    @Override
    public int getStartLevel() {
        return startLevel;
    }

    @Override
    public @NotNull RegistryKeySet<ItemType> getSupportedItems() {
        return RegistrySet.keySet(RegistryKey.ITEM, ItemTypeKeys.ENCHANTED_BOOK, ItemTypeKeys.STONE_SWORD, ItemTypeKeys.IRON_SWORD, ItemTypeKeys.GOLDEN_SWORD, ItemTypeKeys.DIAMOND_SWORD, ItemTypeKeys.NETHERITE_SWORD, ItemTypeKeys.BOW, ItemTypeKeys.CROSSBOW);
    }

    @Override
    public int getWeight() {
        return weight;
    }
    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return true;
    }
    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }
    @Override
    public @NotNull String translationKey() {
        return "enchantment." + NAMESPACE + "." + key;
    }

    @Override
    public @NotNull EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.WEAPON;
    }
}