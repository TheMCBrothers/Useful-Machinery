package net.themcbrothers.usefulmachinery.datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.themcbrothers.usefulmachinery.UsefulMachinery;

@Mod.EventBusSubscriber(modid = UsefulMachinery.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenEvents {
    @SubscribeEvent
    static void onDataGen(final GatherDataEvent event) {
        final var generator = event.getGenerator();
        final var output = generator.getPackOutput();
        final var existingFileHelper = event.getExistingFileHelper();
        final var lookupProvider = event.getLookupProvider();

        // Server resources
        MachineryBlockTagsProvider blockTagsProvider = new MachineryBlockTagsProvider(output, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new MachineryItemTagsProvider(output, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new MachineryLootTableProvider(output));
        generator.addProvider(event.includeServer(), new MachineryRecipeProvider(output));

        // Client resources
        generator.addProvider(event.includeClient(), new MachineryBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new MachineryItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new MachineryLanguageProvider(output));
    }
}
