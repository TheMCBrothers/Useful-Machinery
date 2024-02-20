package net.themcbrothers.usefulmachinery.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE;
import static net.themcbrothers.usefulmachinery.core.MachineryBlocks.*;

public class MachineryBlockTagsProvider extends BlockTagsProvider {
    public MachineryBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, UsefulMachinery.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(MINEABLE_WITH_PICKAXE).add(COAL_GENERATOR.get(), COMPACTOR.get(), CRUSHER.get(), ELECTRIC_SMELTER.get(), LAVA_GENERATOR.get());
    }
}