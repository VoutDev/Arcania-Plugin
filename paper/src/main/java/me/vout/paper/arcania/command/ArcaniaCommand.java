package me.vout.paper.arcania.command;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.vout.paper.arcania.Arcania;
import me.vout.paper.arcania.manager.GuiManager;
import net.kyori.adventure.text.Component;


@NullMarked
public class ArcaniaCommand implements BasicCommand {
    // private final GuiManager guiManager;

    public ArcaniaCommand(GuiManager guiManager) {
        // this.guiManager = guiManager;
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        if (args.length == 0) {
            commandSourceStack.getSender().sendMessage(Component.text("Usage: /arcania [reload|menu|enchants]"));
            return;
        }

        CommandSender sender = commandSourceStack.getSender();

        

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
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
                Arcania.getInstance().reloadManagers(Arcania.getInstance().getServer());
                sender.sendMessage(Component.text("Plugin reloaded!"));
                break;
            // case "enchants":
            //     if (!sender.hasPermission("arcania.menu.enchants")) {
            //         sender.sendMessage(Component.text("You do not have permission!"));  
            //         return;
            //     }
            //     if (!(sender instanceof Player player)) {
            //         sender.sendMessage(Component.text("Only players can use this command."));
            //         return;
            //     }
            //     guiManager.openGui(player, GuiTypeEnum.ENCHANTS);
            //     break;
            default:
                sender.sendMessage(Component.text("Usage: /arcania [reload|menu|enchants]"));
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