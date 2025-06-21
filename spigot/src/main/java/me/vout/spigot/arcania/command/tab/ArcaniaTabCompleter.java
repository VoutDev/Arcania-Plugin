package me.vout.spigot.arcania.command.tab;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArcaniaTabCompleter implements TabCompleter {

    private final Map<String, String> permissionMapping = new HashMap<>();

    public ArcaniaTabCompleter() {
        initializePermissionMapping();
    }
    private void initializePermissionMapping() {
        permissionMapping.put("arcania.reload", "reload");
        permissionMapping.put("arcania.menu.tinkerer", "tinkerer");
        permissionMapping.put("arcania.menu.disenchanter", "disenchanter");
        permissionMapping.put("arcania.menu.enchanter", "enchanter");
        permissionMapping.put("arcania.menu.enchants", "enchants");
        permissionMapping.put("arcania.menu.tester", "tester");
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender,@NonNull Command command,@NonNull String label,String[] args) {
        List<String> completions = new ArrayList<>();
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return completions;
        }

        if (args.length == 1) {
            for (Map.Entry<String, String> entrySet : permissionMapping.entrySet()) {
                String subCommand = entrySet.getValue();
                if (subCommand.startsWith(args[0].toLowerCase()) && player.hasPermission(entrySet.getKey())) {
                    completions.add(subCommand);
                }
            }
        }
        return completions;
    }
}