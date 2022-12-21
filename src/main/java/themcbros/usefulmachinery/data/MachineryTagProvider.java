package themcbros.usefulmachinery.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import themcbros.usefulmachinery.MachineryTags;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.init.MachineryItems;

import javax.annotation.Nullable;

import static themcbros.usefulmachinery.init.MachineryBlocks.*;

public class MachineryTagProvider {
    public static class Block extends BlockTagsProvider {
        public Block(DataGenerator gen, @Nullable ExistingFileHelper existingFileHelper) {
            super(gen, UsefulMachinery.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(COAL_GENERATOR.get(), COMPACTOR.get(), CRUSHER.get(), ELECTRIC_SMELTER.get(), LAVA_GENERATOR.get());
        }
    }

    public static class Item extends ItemTagsProvider {
        public Item(DataGenerator gen, BlockTagsProvider provider, @Nullable ExistingFileHelper existingFileHelper) {
            super(gen, provider, UsefulMachinery.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            this.tag(MachineryTags.Items.BATTERIES).add(MachineryItems.BATTERY.get());
        }
    }
}
