package me.vout.arcania.command.tab;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArcaniaTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            // Suggest subcommands for the first argument
            List<String> subcommands = Arrays.asList("reload", "tinkerer", "disenchanter", "enchanter", "enchants");
            List<String> completions = new ArrayList<>();
            for (String sub : subcommands) {
                if (sub.startsWith(args[0].toLowerCase())) {
                    completions.add(sub);
                }
            }
            return completions;
        }
        // No suggestions for further arguments
        return new ArrayList<>();
    }
}