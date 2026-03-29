package net.themcbrothers.usefulmachinery.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.themcbrothers.lib.data.models.GroupedModelProvider;
import net.themcbrothers.usefulfoundation.UsefulFoundation;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.datagen.models.MachineryBlockModelProvider;
import net.themcbrothers.usefulmachinery.datagen.models.MachineryItemModelProvider;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = UsefulMachinery.MOD_ID)
public final class DataGenEvents {
    @SubscribeEvent
    static void onDataGen(final GatherDataEvent.Client event) {
        final DataGenerator generator = event.getGenerator();
        final PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Server resources
        MachineryBlockTagsProvider blockTagsProvider = new MachineryBlockTagsProvider(output, lookupProvider);
        RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder();

        DatapackBuiltinEntriesProvider datapackBuiltinEntriesProvider = new DatapackBuiltinEntriesProvider(output, lookupProvider, registrySetBuilder, Set.of(UsefulMachinery.MOD_ID));
        generator.addProvider(true, datapackBuiltinEntriesProvider);

        LootTableProvider.SubProviderEntry providerEntry = new LootTableProvider.SubProviderEntry(MachineryBlockLootSubProvider::new, LootContextParamSets.BLOCK);

        lookupProvider = datapackBuiltinEntriesProvider.getRegistryProvider();

        generator.addProvider(true, blockTagsProvider);
        generator.addProvider(true, new MachineryItemTagsProvider(output, lookupProvider, blockTagsProvider.contentsGetter()));
        generator.addProvider(true, new MachineryRecipeProvider.Runner(lookupProvider, output));
        generator.addProvider(true, new LootTableProvider(output, Collections.emptySet(), List.of(providerEntry), lookupProvider));

        // Client resources
        generator.addProvider(true, GroupedModelProvider.create(UsefulMachinery.MOD_ID, MachineryItemModelProvider::new, MachineryBlockModelProvider::new));
        generator.addProvider(true, new MachineryLanguageProvider(output));
    }
}
