package themcbros.usefulmachinery.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import themcbros.usefulmachinery.MachineryTags;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.init.MachineryItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import static themcbros.usefulmachinery.init.MachineryBlocks.*;

public class MachineryTagProvider {
    public static class Block extends BlockTagsProvider {
        public Block(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, provider, UsefulMachinery.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(@Nonnull HolderLookup.Provider provider) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(COAL_GENERATOR.get(), COMPACTOR.get(), CRUSHER.get(), ELECTRIC_SMELTER.get(), LAVA_GENERATOR.get());
        }
    }

    public static class Item extends ItemTagsProvider {
        public Item(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<net.minecraft.world.level.block.Block>> blockTagLookup, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, provider, blockTagLookup, UsefulMachinery.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(@Nonnull HolderLookup.Provider provider) {
            this.tag(MachineryTags.Items.BATTERIES).add(MachineryItems.BATTERY.get());
            this.tag(MachineryTags.Items.MACHINERY_UPGRADES).add(MachineryItems.EFFICIENCY_UPGRADE.get(), MachineryItems.PRECISION_UPGRADE.get(), MachineryItems.SUSTAINED_UPGRADE.get());
        }
    }


}
