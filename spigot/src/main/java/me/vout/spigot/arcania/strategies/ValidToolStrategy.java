package me.vout.spigot.arcania.strategies;

import me.vout.core.arcania.strategies.IValidToolStrategy;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class ValidToolStrategy implements IValidToolStrategy {
    @Override
    public boolean isValidTool(ItemStack tool, Block block) {
        return block.isPreferredTool(tool);
    }
}
