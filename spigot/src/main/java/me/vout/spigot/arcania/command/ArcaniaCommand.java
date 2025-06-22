package me.vout.spigot.arcania.command;

import me.vout.core.arcania.gui.GuiTypeEnum;
import me.vout.core.arcania.providers.ArcaniaProvider;
import me.vout.spigot.arcania.util.ItemHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ArcaniaCommand implements CommandExecutor {

    @Override

   public boolean onCommand(@NonNull CommandSender sender,@NonNull Command command,@NonNull String label,@NonNull String[] args) {
        String subcommand = (args.length == 0) ? "menu" : args[0].toLowerCase(); // Default to "menu"

        if (sender instanceof Player player) { // Subcommands requiring Player sender
            switch (subcommand) {
                case "menu":
                    // Permission check is usually more concise here
                    if (!player.hasPermission("arcania.menu.base")) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to view the main menu!");
                        return true;
                    }
                    ArcaniaProvider.getPlugin().getGuiManager().openGui(player, GuiTypeEnum.MAIN);
                    break;

                case "tinkerer":
                    if (!player.hasPermission("arcania.menu.tinkerer")) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to view the tinkerer menu!");
                        return true;
                    }
                    ArcaniaProvider.getPlugin().getGuiManager().openGui(player, GuiTypeEnum.TINKERER);
                    break;

                case "disenchanter":
                    if (!player.hasPermission("arcania.menu.disenchanter")) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to view the disenchanter menu!");
                        return true;
                    }
                    ArcaniaProvider.getPlugin().getGuiManager().openGui(player, GuiTypeEnum.DISENCHANTER);
                    break;

                case "enchanter":
                    if (!player.hasPermission("arcania.menu.enchanter")) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to view the enchanter menu!");
                        return true;
                    }
                    ArcaniaProvider.getPlugin().getGuiManager().openGui(player, GuiTypeEnum.ENCHANTER);
                    break;

                case "enchants":
                    if (!player.hasPermission("arcania.menu.enchants")) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to view the enchants menu!");
                        return true;
                    }
                    ArcaniaProvider.getPlugin().getGuiManager().openGui(player, GuiTypeEnum.ENCHANTS);
                    break;

                case "tester":
                    if (!player.hasPermission("arcania.menu.tester")) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to view the tester menu!");
                        return true;
                    }
                    ArcaniaProvider.getPlugin().getGuiManager().openGui(player, GuiTypeEnum.TESTER);
                    break;

                case "reload":
                    handleAdminCommand(sender, subcommand, label); // Delegate to a new method
                    break;
                case "clear":
                    handleAdminCommand(sender, subcommand, label);
                    break;

                default:
                    player.sendMessage(ChatColor.YELLOW + "Usage: /" + label + " [menu|tinkerer|...|reload|invsee]");
                    break;
            }
        } else {
            handleAdminCommand(sender, subcommand, label);
        }
        return true;
    }

    // New helper method to handle commands that can be run by console or player
    private void handleAdminCommand(@NonNull CommandSender sender, String subcommand, String label) {
        if (subcommand.equals("reload")) {
            if (!sender.hasPermission("arcania.reload")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to reload this plugin!");
                return;
            }
            ArcaniaProvider.getPlugin().getJavaPlugin().reloadConfig(); // Use Bukkit's reloadConfig
            ArcaniaProvider.getPlugin().reloadManagers(); // Your custom reload logic
            sender.sendMessage(ChatColor.GREEN + "Plugin reloaded!");
        }
        else if (subcommand.equals("clear")) {
            if (!sender.hasPermission("arcania.clear")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use Arcania clear!");
                return;
            }
            Player player = (Player) sender;
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            if (heldItem.getType().isAir()) {
                sender.sendMessage(ChatColor.YELLOW + "Must be holding an item");
                return;
            }
            ItemHelper.clearDataForItem(heldItem);
            sender.sendMessage(ChatColor.GREEN + "Held item cleared!");
        }
        else {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /" + label + " [subcommand]"); // Fallback for console or invalid player subcommand
        }
    }
}