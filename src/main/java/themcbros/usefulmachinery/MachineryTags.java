package themcbros.usefulmachinery;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.themcbrothers.lib.util.TagUtils;

public class MachineryTags {
    public static class Blocks {
    }

    public static class Items {
        public static final TagKey<Item> BATTERIES = TagUtils.forgeItemTag("batteries");
    }
}
