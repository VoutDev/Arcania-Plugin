package me.vout.spigot.arcania.command;

import me.vout.spigot.arcania.gui.GuiTypeEnum;
import me.vout.spigot.arcania.manager.GuiManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnchanterCommand implements CommandExecutor {

    private final GuiManager guiManager;

    public EnchanterCommand(GuiManager guiManager) {
        this.guiManager = guiManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        guiManager.openGui(player, GuiTypeEnum.ENCHANTER);
        return true;
    }
}
