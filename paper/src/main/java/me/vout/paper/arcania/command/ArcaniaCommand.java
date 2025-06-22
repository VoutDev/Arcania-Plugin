package me.vout.paper.arcania.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.vout.core.arcania.gui.GuiTypeEnum;
import me.vout.core.arcania.providers.ArcaniaProvider;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;


@NullMarked
public class ArcaniaCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        String subcommand = (args.length == 0) ? "menu" : args[0].toLowerCase(); // Default to "menu"
        CommandSender sender = commandSourceStack.getSender();

        if (sender instanceof Player player) { // Subcommands requiring Player sender
            switch (subcommand) {
                case "menu":
                    // Permission check is usually more concise here
                    if (!player.hasPermission("arcania.menu.base")) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to view the main menu!");
                        return;
                    }
                    ArcaniaProvider.getPlugin().getGuiManager().openGui(player, GuiTypeEnum.MAIN);
                    break;

                case "tinkerer":
                    if (!player.hasPermission("arcania.menu.tinkerer")) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to view the tinkerer menu!");
                        return;
                    }
                    ArcaniaProvider.getPlugin().getGuiManager().openGui(player, GuiTypeEnum.TINKERER);
                    break;

                case "disenchanter":
                    if (!player.hasPermission("arcania.menu.disenchanter")) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to view the disenchanter menu!");
                        return;
                    }
                    ArcaniaProvider.getPlugin().getGuiManager().openGui(player, GuiTypeEnum.DISENCHANTER);
                    break;

                case "enchanter":
                    if (!player.hasPermission("arcania.menu.enchanter")) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to view the enchanter menu!");
                        return;
                    }
                    ArcaniaProvider.getPlugin().getGuiManager().openGui(player, GuiTypeEnum.ENCHANTER);
                    break;

                case "enchants":
                    if (!player.hasPermission("arcania.menu.enchants")) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to view the enchants menu!");
                        return;
                    }
                    ArcaniaProvider.getPlugin().getGuiManager().openGui(player, GuiTypeEnum.ENCHANTS);
                    break;

                case "tester":
                    if (!player.hasPermission("arcania.menu.tester")) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to view the tester menu!");
                        return;
                    }
                    ArcaniaProvider.getPlugin().getGuiManager().openGui(player, GuiTypeEnum.TESTER);
                    break;

                case "reload":
                    handleAdminCommand(sender, subcommand); // Delegate to a new method
                    break;

                default:
                    player.sendMessage(ChatColor.YELLOW + "Usage: /arcania" + " [menu|tinkerer|...|reload|invsee]");
                    break;
            }
        } else {
            handleAdminCommand(sender, subcommand);
        }
    }

    // New helper method to handle commands that can be run by console or player
    private void handleAdminCommand(@NonNull CommandSender sender, String subcommand) {
        if (subcommand.equals("reload")) {
            if (!sender.hasPermission("arcania.reload")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to reload this plugin!");
                return;
            }
            ArcaniaProvider.getPlugin().getJavaPlugin().reloadConfig(); // Use Bukkit's reloadConfig
            ArcaniaProvider.getPlugin().reloadManagers(); // Your custom reload logic
            sender.sendMessage(ChatColor.GREEN + "Plugin reloaded!");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /arcania" + " [subcommand]"); // Fallback for console or invalid player subcommand
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