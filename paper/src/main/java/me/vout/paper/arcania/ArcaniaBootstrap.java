package me.vout.paper.arcania;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.tags.EnchantmentTagKeys;
import me.vout.paper.arcania.command.ArcaniaCommand;
import me.vout.paper.arcania.enchant.ArcaniaEnchant;
import me.vout.paper.arcania.manager.ArcaniaEnchantManager;
import net.kyori.adventure.key.Key;

import java.util.stream.Collectors;

public class ArcaniaBootstrap implements PluginBootstrap {
    private static ArcaniaEnchantManager enchantManager;

    private final ArcaniaCommand arcaniaCommand = new ArcaniaCommand();

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

            // Register all arcania enchantments
            // ArcaniaEnchant implements Enchantment so we can just pass all the attributes directly to the builder
            for (ArcaniaEnchant enchant: enchantManager.getEnchants()) {
                enchant.register(event.registry());
            }
        }));

        // add enchants to enchantment table tag
        lifecycle.registerEventHandler(LifecycleEvents.TAGS.postFlatten(RegistryKey.ENCHANTMENT).newHandler(event -> {
            event.registrar().addToTag(
                EnchantmentTagKeys.IN_ENCHANTING_TABLE,
                // create a set of all arcania enchantment keys
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
}
