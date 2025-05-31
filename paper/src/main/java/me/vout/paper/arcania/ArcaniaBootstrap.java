package me.vout.paper.arcania;

import me.vout.paper.arcania.command.ArcaniaCommand;
import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import me.vout.paper.arcania.manager.ArcaniaEnchantManager;
import me.vout.paper.arcania.manager.GuiManager;

import java.util.Set;
import java.util.stream.Collectors;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry.EnchantmentCost;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.tags.EnchantmentTagKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

public class ArcaniaBootstrap implements PluginBootstrap {
    private static final GuiManager guiManager = new GuiManager();
    private static ArcaniaEnchantManager enchantManager;

    private final ArcaniaCommand arcaniaCommand = new ArcaniaCommand(guiManager);

    @Override
    public void bootstrap(BootstrapContext context) {

        LifecycleEventManager<BootstrapContext> lifecycle = context.getLifecycleManager();
        // Register command handler
        lifecycle.registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register("arcania", arcaniaCommand);
        });

        // Register enchantment handler - this will be called at the right time by Paper
        lifecycle.registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
            enchantManager = new ArcaniaEnchantManager();

            // Register all enchantments
            for (ArcaniaEnchant enchant: enchantManager.getEnchants()) {
                event.registry().register(
                    EnchantmentKeys.create(Key.key(enchant.getKey().getNamespace(), enchant.getKey().getKey())),
                    b -> 
                        b.description(Component.text(enchant.getName()))
                        .supportedItems(enchant.getSupportedItems())
                        .primaryItems(enchant.getPrimaryItems())
                        .exclusiveWith(enchant.getExclusiveWith())
                        .anvilCost(enchant.getAnvilCost())
                        .maxLevel(enchant.getMaxLevel())
                        .weight(enchant.getWeight())
                        .minimumCost(EnchantmentCost.of(enchant.getStartLevel(), enchant.getMinModifiedCost(enchant.getStartLevel())))
                        .maximumCost(EnchantmentCost.of(enchant.getStartLevel(), enchant.getMaxModifiedCost(enchant.getStartLevel())))
                        .activeSlots(enchant.getActiveSlotGroups())
                );
            }
        }));

        // add enchants to enchantment table tag
        lifecycle.registerEventHandler(LifecycleEvents.TAGS.postFlatten(RegistryKey.ENCHANTMENT).newHandler(event -> {
            event.registrar().addToTag(
                EnchantmentTagKeys.IN_ENCHANTING_TABLE,
                enchantManager.getEnchants().stream().map(enchant -> EnchantmentKeys.create(Key.key(enchant.getKey().getNamespace(), enchant.getKey().getKey()))).collect(Collectors.toSet())
            );
        }));
    }

    @Override
    public Arcania createPlugin(PluginProviderContext context) {
        return new Arcania();
    }

    public static ArcaniaEnchantManager getEnchantManager() {
        return enchantManager;
    }

    public static GuiManager getGuiManager() {
        return guiManager;
    }

}
