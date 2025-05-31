package me.vout.paper.arcania.enchant.registry;

import java.util.Set;

import org.bukkit.inventory.ItemType;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.ItemTypeKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.TagEntry;
import net.kyori.adventure.key.Key;

public class RegistryTags {
    public static final TagKey<ItemType> TOOLS = ItemTypeTagKeys.create(Key.key("arcania:tools"));
    public static final TagKey<ItemType> SWORDS_AND_TOOLS = ItemTypeTagKeys.create(Key.key("arcania:sword_and_tools"));
    public static final TagKey<ItemType> SWORDS_AND_RANGED = ItemTypeTagKeys.create(Key.key("arcania:swords_and_ranged"));
    public static final TagKey<ItemType> PICKAXES = ItemTypeTagKeys.create(Key.key("arcania:pickaxes"));
    public static final TagKey<ItemType> AXES = ItemTypeTagKeys.create(Key.key("arcania:axes"));
    public static final TagKey<ItemType> SHOVELS = ItemTypeTagKeys.create(Key.key("arcania:shovels"));
    public static final TagKey<ItemType> HOES = ItemTypeTagKeys.create(Key.key("arcania:hoes"));
    public static final TagKey<ItemType> SWORDS = ItemTypeTagKeys.create(Key.key("arcania:swords"));
    public static final TagKey<ItemType> RANGED = ItemTypeTagKeys.create(Key.key("arcania:ranged"));


    public static final Set<TagEntry<ItemType>> PICKAXES_SET = Set.of(
            TagEntry.tagEntry(ItemTypeTagKeys.PICKAXES));

    public static final Set<TagEntry<ItemType>> TOOLS_SET = Set.of(
            TagEntry.tagEntry(ItemTypeTagKeys.PICKAXES),
            TagEntry.tagEntry(ItemTypeTagKeys.AXES),
            TagEntry.tagEntry(ItemTypeTagKeys.SHOVELS),
            TagEntry.tagEntry(ItemTypeTagKeys.HOES));

    public static final Set<TagEntry<ItemType>> SWORDS_AND_TOOLS_SET = Set.of(
            TagEntry.tagEntry(ItemTypeTagKeys.SWORDS),
            TagEntry.tagEntry(ItemTypeTagKeys.PICKAXES),
            TagEntry.tagEntry(ItemTypeTagKeys.AXES),
            TagEntry.tagEntry(ItemTypeTagKeys.SHOVELS),
            TagEntry.tagEntry(ItemTypeTagKeys.HOES));

    public static final Set<TagEntry<ItemType>> AXES_SET = Set.of(
            TagEntry.tagEntry(ItemTypeTagKeys.AXES));

    public static final Set<TagEntry<ItemType>> SHOVELS_SET = Set.of(
            TagEntry.tagEntry(ItemTypeTagKeys.SHOVELS));

    public static final Set<TagEntry<ItemType>> HOES_SET = Set.of(
            TagEntry.tagEntry(ItemTypeTagKeys.HOES));

    public static final Set<TagEntry<ItemType>> SWORDS_SET = Set.of(
            TagEntry.tagEntry(ItemTypeTagKeys.SWORDS));

    public static final Set<TagEntry<ItemType>> RANGED_SET = Set.of(
            TagEntry.tagEntry(TagKey.create(RegistryKey.ITEM, ItemTypeKeys.BOW)),
            TagEntry.tagEntry(TagKey.create(RegistryKey.ITEM, ItemTypeKeys.CROSSBOW)));

    public static final Set<TagEntry<ItemType>> SWORDS_AND_RANGED_SET = Set.of(
            TagEntry.tagEntry(ItemTypeTagKeys.SWORDS),
            TagEntry.tagEntry(TagKey.create(RegistryKey.ITEM, ItemTypeKeys.BOW)),
            TagEntry.tagEntry(TagKey.create(RegistryKey.ITEM, ItemTypeKeys.CROSSBOW)));
}
