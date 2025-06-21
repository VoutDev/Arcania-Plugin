package me.vout.spigot.arcania.command;

import me.vout.core.arcania.gui.GuiTypeEnum;
import me.vout.spigot.arcania.Arcania;
import me.vout.spigot.arcania.manager.GuiManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ArcaniaCommand implements CommandExecutor {
    private final GuiManager guiManager;
    private final Arcania plugin;

    public ArcaniaCommand(GuiManager guiManager, Arcania plugin) {
        this.guiManager = guiManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender,@NonNull Command command,@NonNull String label,@NonNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        String subcommand = (args.length == 0) ? "menu" : args[0].toLowerCase();

        switch (subcommand) {
            case "menu": //Probably set this as default case and remove the menu from here and plugin.yml for arcania
                if (!player.hasPermission("arcania.menu")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to view the main menu!");
                    return true;
                }
                guiManager.openGui(player, GuiTypeEnum.MAIN);
                break;

            case "reload":
                if (!player.hasPermission("arcania.reload")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to reload this plugin!");
                    return true;
                }
                plugin.reloadManagers();
                player.sendMessage(ChatColor.GREEN + "Plugin reloaded!");
                break;

            case "tinkerer":
                if (!player.hasPermission("arcania.menu.tinkerer")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to view the tinkerer menu!");
                    return true;
                }
                guiManager.openGui(player, GuiTypeEnum.TINKERER);
                break;

            case "disenchanter":
                if (!player.hasPermission("arcania.menu.disenchanter")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to view the disenchanter menu!");
                    return true;
                }
                guiManager.openGui(player, GuiTypeEnum.DISENCHANTER);
                break;

            case "enchanter":
                if (!player.hasPermission("arcania.menu.enchanter")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to view the enchanter menu!");
                    return true;
                }
                guiManager.openGui(player, GuiTypeEnum.ENCHANTER);
                break;

            case "enchants":
                if (!player.hasPermission("arcania.menu.enchants")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to view the enchants menu!");
                    return true;
                }
                guiManager.openGui(player, GuiTypeEnum.ENCHANTS);
                break;
            case "tester":
                if (!player.hasPermission("arcania.menu.tester")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to view the tester menu!");
                    return true;
                }
                guiManager.openGui(player, GuiTypeEnum.TESTER);
                break;

            default:
                player.sendMessage(ChatColor.YELLOW + "Usage: /" + label + " [subcommand]");
                break;
        }
        return true;
    }
}