package me.vout.paper.arcania.manager;

import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import me.vout.paper.arcania.enchant.hoe.HarvesterEnchant;
import me.vout.paper.arcania.enchant.hoe.TillerEnchant;
import me.vout.paper.arcania.enchant.pickaxe.EnrichmentEnchant;
import me.vout.paper.arcania.enchant.pickaxe.QuarryEnchant;
import me.vout.paper.arcania.enchant.pickaxe.SmeltEnchant;
import me.vout.paper.arcania.enchant.pickaxe.VeinminerEnchant;
import me.vout.paper.arcania.enchant.tool.MagnetEnchant;
import me.vout.paper.arcania.enchant.weapon.EssenceEnchant;
import me.vout.paper.arcania.enchant.weapon.FrostbiteEnchant;
import net.kyori.adventure.key.Key;

import java.util.*;

public class ArcaniaEnchantManager {
    private final List<ArcaniaEnchant> enchants = new ArrayList<>();
    public final Map<Key, ArcaniaEnchant> enchantMap = new HashMap<>();

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
            enchantMap.put(enchant.getKey(), enchant);
        }
    }
}