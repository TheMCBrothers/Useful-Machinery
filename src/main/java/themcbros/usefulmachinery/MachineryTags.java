package themcbros.usefulmachinery;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

public class MachineryTags {
    public static class Blocks {
    }

    public static class Items {
        public static final Tags.IOptionalNamedTag<Item> BATTERIES = ItemTags.createOptional(new ResourceLocation("forge", "batteries"));
    }
}
