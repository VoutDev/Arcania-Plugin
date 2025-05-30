package me.vout.paper.arcania.gui;

import me.vout.paper.arcania.gui.base.GuiHolder;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Bukkit;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Deque;

public class GuiBuilder {
    private final Inventory inventory;
    public GuiBuilder(int size, String title, GuiHolder guiHolder) {
        this.inventory = Bukkit.createInventory(guiHolder, size, title);
    }

    public GuiBuilder set(int slot, ItemStack item) {
        inventory.setItem(slot, item);
        return this;
    }

    public GuiBuilder setBorder(ItemStack borderItem) {
        int size = inventory.getSize();
        int width = 9;
        int rows = size / width;

        // Top row
        for (int i = 0; i < width; i++) inventory.setItem(i, borderItem);

        // Bottom row
        for (int i = size - width; i < size; i++) inventory.setItem(i, borderItem);

        // Left and right columns
        for (int row = 1; row < rows - 1; row++) {
            inventory.setItem(row * width, borderItem);           // Left
            inventory.setItem(row * width + width - 1, borderItem); // Right
        }
        return this;
    }

    public GuiBuilder fillEmptySlots(ItemStack fillerItem) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null || (inventory.getItem(i) != null && inventory.getItem(i).getType().isAir())) {
                inventory.setItem(i, fillerItem);
            }
        }
        return this;
    }

    public GuiBuilder fillEmptySlotsWithItems(ItemStack... itemStacks) {
        int stackIndex = 0;

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType().isAir()) {
                // If we've run out of itemStacks, stop
                if (stackIndex >= itemStacks.length) break;

                // Place one item from the current stack
                ItemStack current = itemStacks[stackIndex];
                if (current == null || current.getAmount() <= 0) {
                    stackIndex++;
                    if (stackIndex < itemStacks.length) {
                        current = itemStacks[stackIndex];
                    } else {
                        break;
                    }
                }
                if (current == null || current.getAmount() <= 0) continue;

                // Place a single item in the slot
                ItemStack toPlace = current.clone();
                toPlace.setAmount(1);
                inventory.setItem(i, toPlace);

                // Decrement the amount in the original stack
                current.setAmount(current.getAmount() - 1);

                // If the current stack is empty, move to the next
                if (current.getAmount() <= 0) {
                    stackIndex++;
                }
            }
        }
        return this;
    }


    // Can always be called, only adds if necessary
    public GuiBuilder addBackButton(Deque<GuiTypeEnum> guiHistory) {
        if (inventory.getHolder() instanceof GuiHolder
                && guiHistory != null
                && !guiHistory.isEmpty()) {
            int backSlot = inventory.getSize() - 9;
            ItemStack backButton = new ItemStack(Material.ARROW);
            ItemMeta meta = backButton.getItemMeta();
            meta.setDisplayName(NamedTextColor.YELLOW + "Back");
            GuiHelper.setPersistentData(PersistentDataEnum.BUTTON_TYPE.toString().toLowerCase(),"back", meta);
            backButton.setItemMeta(meta);
            this.set(backSlot, backButton);
        }
        return this;
    }

    public Inventory build() {
        return  inventory;
    }
}