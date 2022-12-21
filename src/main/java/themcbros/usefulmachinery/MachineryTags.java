package themcbros.usefulmachinery;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MachineryTags {
    public static class Blocks {
    }

    public static class Items {
        public static final TagKey<Item> BATTERIES = ItemTags.create(new ResourceLocation("forge", "batteries"));
    }
}
