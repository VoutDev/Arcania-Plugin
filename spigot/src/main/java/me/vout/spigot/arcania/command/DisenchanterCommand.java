package me.vout.spigot.arcania.command;

import me.vout.core.arcania.gui.GuiTypeEnum;
import me.vout.spigot.arcania.manager.GuiManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class DisenchanterCommand implements CommandExecutor {
    private final GuiManager guiManager;
    public DisenchanterCommand(GuiManager guiManager) {
        this.guiManager = guiManager;
    }
    @Override
    public boolean onCommand(@NonNull CommandSender sender,@NonNull Command command,@NonNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        guiManager.openGui(player, GuiTypeEnum.DISENCHANTER);
        return true;
    }
}
