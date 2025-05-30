package me.vout.arcania.command;

import me.vout.arcania.gui.GuiTypeEnum;
import me.vout.arcania.manager.GuiManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisenchanterCommand implements CommandExecutor {
    private final GuiManager guiManager;
    public DisenchanterCommand(GuiManager guiManager) {
        this.guiManager = guiManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        guiManager.openGui(player, GuiTypeEnum.DISENCHANTER);
        return true;
    }
}
