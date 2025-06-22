package me.vout.paper.arcania.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.vout.core.arcania.gui.GuiTypeEnum;
import me.vout.core.arcania.providers.ArcaniaProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnchantsCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        CommandSender sender = commandSourceStack.getSender();
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return ;
        }
        ArcaniaProvider.getPlugin().getGuiManager().openGui(player, GuiTypeEnum.ENCHANTS);
    }
}