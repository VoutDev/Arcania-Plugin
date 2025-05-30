package me.vout.spigot.arcania.manager;

import me.vout.spigot.arcania.enchant.ArcaniaEnchant;
import me.vout.spigot.arcania.enchant.hoe.HarvesterEnchant;
import me.vout.spigot.arcania.enchant.hoe.TillerEnchant;
import me.vout.spigot.arcania.enchant.pickaxe.EnrichmentEnchant;
import me.vout.spigot.arcania.enchant.pickaxe.QuarryEnchant;
import me.vout.spigot.arcania.enchant.pickaxe.VeinminerEnchant;
import me.vout.spigot.arcania.enchant.tool.MagnetEnchant;
import me.vout.spigot.arcania.enchant.tool.SmeltEnchant;
import me.vout.spigot.arcania.enchant.weapon.EssenceEnchant;
import me.vout.spigot.arcania.enchant.weapon.FrostbiteEnchant;

import java.util.*;

public class ArcaniaEnchantManager {
    private final List<ArcaniaEnchant> enchants = new ArrayList<>();
    public final Map<String, ArcaniaEnchant> enchantMap = new HashMap<>();

    public ArcaniaEnchantManager() {
        enchantInit();
        buildEnchantMap();
    }

    public List<ArcaniaEnchant> getEnchants() {
        return Collections.unmodifiableList(enchants);
    }

    private void enchantInit() {
        enchants.add(QuarryEnchant.INSTANCE);
        enchants.add(TillerEnchant.INSTANCE);
        enchants.add(EnrichmentEnchant.INSTANCE);
        enchants.add(HarvesterEnchant.INSTANCE);
        enchants.add(FrostbiteEnchant.INSTANCE);
        enchants.add(VeinminerEnchant.INSTANCE);
        enchants.add(MagnetEnchant.INSTANCE);
        enchants.add(EssenceEnchant.INSTANCE);
        enchants.add(SmeltEnchant.INSTANCE);
    }

    private void buildEnchantMap() {
        for (ArcaniaEnchant enchant : enchants) {
            enchantMap.put(enchant.getName(), enchant);
        }
    }
}