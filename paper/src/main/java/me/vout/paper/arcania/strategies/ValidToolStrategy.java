package me.vout.paper.arcania.strategies;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import me.vout.core.arcania.strategies.IValidToolStrategy;
import org.bukkit.block.Block;
import org.bukkit.block.BlockType;
import org.bukkit.inventory.ItemStack;

public class ValidToolStrategy implements IValidToolStrategy {

    @Override
    public boolean isValidTool(ItemStack tool, Block block) {
        TypedKey<BlockType> typedKeyBlockType = TypedKey.create(RegistryKey.BLOCK, block.getType().getKey());
        return tool.getData(DataComponentTypes.TOOL).rules().stream().anyMatch(rule -> rule.blocks().contains(typedKeyBlockType));
    }
}
