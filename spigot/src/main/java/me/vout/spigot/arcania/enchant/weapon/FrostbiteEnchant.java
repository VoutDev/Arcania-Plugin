package me.vout.spigot.arcania.enchant.weapon;

import me.vout.spigot.arcania.Arcania;
import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.EnchantRarityEnum;
import me.vout.spigot.arcania.util.ItemHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class FrostbiteEnchant extends ArcaniaEnchant {
    public static final FrostbiteEnchant INSTANCE = new FrostbiteEnchant();

    private FrostbiteEnchant() {
        super("Frostbite",
                "Slows attacked entity",
                EnchantRarityEnum.UNCOMMON,
                2,
                0.6,
                2,
                ItemHelper::isWeapon);
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

    @Override
    public boolean canApplyWith(ArcaniaEnchant enchant) {
        return true;
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
}