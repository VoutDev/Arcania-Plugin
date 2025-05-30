package me.vout.paper.arcania;

import me.vout.paper.arcania.command.ArcaniaCommand;
import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import me.vout.paper.arcania.manager.ArcaniaEnchantManager;
import me.vout.paper.arcania.manager.GuiManager;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

public class ArcaniaBootstrap implements PluginBootstrap {
    private static final GuiManager guiManager = new GuiManager();
    private static ArcaniaEnchantManager enchantManager;

    private final ArcaniaCommand arcaniaCommand = new ArcaniaCommand(guiManager);

    @Override
    public void bootstrap(BootstrapContext context) {
        // Register command handler
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register("arcania", arcaniaCommand);
        });

        // Register enchantment handler - this will be called at the right time by Paper
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
            enchantManager = new ArcaniaEnchantManager();

            ArcaniaEnchant enchant = enchantManager.getEnchants().stream().filter(e -> e.getKey().equals(Key.key("arcania", "frostbite"))).findFirst().orElse(null);
            if (enchant != null) {
                event.registry().register(
                    EnchantmentKeys.create(enchant.getKey()),
                    b -> b.description(Component.text(enchant.getName()))
                    .supportedItems(enchant.getSupportedItems())
                    .anvilCost(enchant.getAnvilCost())
                    .maxLevel(enchant.getMaxLevel())
                    .weight(enchant.getWeight())
                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(enchant.getMinModifiedCost(enchant.getStartLevel()), enchant.getStartLevel()))
                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(enchant.getMaxModifiedCost(enchant.getStartLevel()), enchant.getStartLevel()))
                    .activeSlots(enchant.getActiveSlotGroups())
                );
            }
            
            



            // Register all enchantments
            // for (ArcaniaEnchant enchant: enchantManager.getEnchants()) {
            //     event.registry().register(
            //         EnchantmentKeys.create(Key.key(enchant.getKey().getNamespace(), enchant.getKey().getKey())),
            //         b -> 
            //             b.description(Component.text(enchant.getName()))
            //             .supportedItems(enchant.getSupportedItems())
            //             .anvilCost(enchant.getAnvilCost())
            //             .maxLevel(enchant.getMaxLevel())
            //             .weight(enchant.getWeight())
            //             .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(enchant.getMinModifiedCost(enchant.getStartLevel()), enchant.getStartLevel()))
            //             .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(enchant.getMaxModifiedCost(enchant.getStartLevel()), enchant.getStartLevel()))
            //             .activeSlots(enchant.getActiveSlotGroups())
            //     );
            // }
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
