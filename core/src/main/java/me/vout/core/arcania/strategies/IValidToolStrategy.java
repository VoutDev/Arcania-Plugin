package me.vout.core.arcania.strategies;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public interface IValidToolStrategy {
    boolean isValidTool(ItemStack tool, Block block);
}
