package me.vout.paper.arcania.command;

import java.util.Arrays;
import java.util.Collection;

import me.vout.core.arcania.gui.GuiTypeEnum;
import me.vout.core.arcania.providers.ArcaniaProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.vout.paper.arcania.Arcania;
import net.kyori.adventure.text.Component;



@NullMarked
public class ArcaniaCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        if (args.length == 0) {
            commandSourceStack.getSender().sendMessage(Component.text("Usage: /arcania [reload|menu|enchants|tester]"));
            return;
        }

        CommandSender sender = commandSourceStack.getSender();

        String subcommand = args[0].toLowerCase();

        switch (subcommand) { //todo fix this UI to work with the limited necessary paths of enchants and tester
            // case "menu":
            //     if (sender.hasPermission("arcania.menu")) {
            //         sender.sendMessage(Component.text("You do not have permission!"));
            //         return;
            //     }
            //     if (!(sender instanceof Player player)) {
            //         sender.sendMessage(Component.text("Only players can use this command."));
            //         return;
            //     }
            //     // guiManager.openGui(player, GuiTypeEnum.MAIN);
            //     break;
            case "reload":
                if (!sender.hasPermission("arcania.reload")) {
                    sender.sendMessage(Component.text("You do not have permission!"));
                    return;
                }   
                ArcaniaProvider.getPlugin().reloadManagers();
                sender.sendMessage(Component.text("Plugin reloaded!"));
                break;
             case "enchants":
                 if (!sender.hasPermission("arcania.menu.enchants")) {
                     sender.sendMessage(Component.text("You do not have permission!"));
                     return;
                 }
                 if (!(sender instanceof Player player)) {
                     sender.sendMessage(Component.text("Only players can use this command."));
                     return;
                 }
                 Arcania.getGuiManager().openGui(player, GuiTypeEnum.ENCHANTS);
                 break;
            case "tester":
                if (!sender.hasPermission("arcania.menu.tester")) {
                    sender.sendMessage(Component.text("You do not have permission!"));
                    return;
                }
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(Component.text("Only players can use this command."));
                    return;
                }
                Arcania.getGuiManager().openGui(player, GuiTypeEnum.TESTER);
                break;
            default:
                sender.sendMessage(Component.text("Usage: /arcania [reload|menu|enchants|tester]")); //todo set this to check permissions rather than hardcoded
        }
    }

    @Override
    public @Nullable String permission() {
        return "arcania.use";
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
        return Arrays.asList("reload", "menu", "enchants");
    }
}