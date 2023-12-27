package net.themcbrothers.examplemod.datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.themcbrothers.examplemod.ExampleMod;

@Mod.EventBusSubscriber(modid = ExampleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenEvents {
    @SubscribeEvent
    static void onDataGen(final GatherDataEvent event) {
        final var generator = event.getGenerator();
        final var packOutput = generator.getPackOutput();
        final var existingFileHelper = event.getExistingFileHelper();
        final var lookupProvider = event.getLookupProvider();

        // Server resources
        var blockTags = new ExampleBlockTagsProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ExampleItemTagsProvider(packOutput, lookupProvider, blockTags.contentsGetter(), existingFileHelper));

        // Client resources
        generator.addProvider(event.includeClient(), new ExampleBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ExampleItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ExampleLanguageProvider(packOutput));
    }
}
