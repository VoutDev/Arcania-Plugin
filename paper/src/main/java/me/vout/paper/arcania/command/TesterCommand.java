package me.vout.paper.arcania.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.vout.core.arcania.gui.GuiTypeEnum;
import me.vout.core.arcania.providers.ArcaniaProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TesterCommand implements BasicCommand {

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        CommandSender sender = commandSourceStack.getSender();

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command."));
            return;
        }
        ArcaniaProvider.getPlugin().getGuiManager().openGui(player, GuiTypeEnum.TESTER);
    }
}
