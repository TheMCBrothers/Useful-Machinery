package net.themcbrothers.usefulmachinery.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.themcbrothers.usefulmachinery.core.MachineryItems.*;
import static net.themcbrothers.usefulmachinery.core.MachineryTags.Items.BATTERIES;
import static net.themcbrothers.usefulmachinery.core.MachineryTags.Items.MACHINERY_UPGRADES;

public class MachineryItemTagsProvider extends ItemTagsProvider {
    public MachineryItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, UsefulMachinery.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BATTERIES).add(BATTERY.get());
        this.tag(MACHINERY_UPGRADES).add(EFFICIENCY_UPGRADE.get(), PRECISION_UPGRADE.get(), SUSTAINED_UPGRADE.get());
    }
}
