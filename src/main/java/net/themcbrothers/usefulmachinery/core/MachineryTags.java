package net.themcbrothers.usefulmachinery.core;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.themcbrothers.lib.util.TagUtils;
import net.themcbrothers.usefulmachinery.UsefulMachinery;

public class MachineryTags {
    public static class Blocks {

    }

    public static class Items {
        public static final TagKey<Item> BATTERIES = TagUtils.commonItemTag("batteries");
        public static final TagKey<Item> MACHINERY_UPGRADES = ItemTags.create(UsefulMachinery.id("machinery_upgrades"));
    }
}
