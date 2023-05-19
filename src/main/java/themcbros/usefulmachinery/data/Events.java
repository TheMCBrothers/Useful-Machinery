package themcbros.usefulmachinery.data;

import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import themcbros.usefulmachinery.UsefulMachinery;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = UsefulMachinery.MOD_ID)
public class Events {
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        // Client Providers
        gen.addProvider(event.includeClient(), new MachineryBlockStateProvider(output, fileHelper));
        gen.addProvider(event.includeClient(), new MachineryItemModelProvider(output, fileHelper));
        gen.addProvider(event.includeClient(), new MachineryLanguageProviders.EnglishUS(output));
        gen.addProvider(event.includeClient(), new MachineryLanguageProviders.SwissGerman(output));
        gen.addProvider(event.includeClient(), new MachinerySpriteSourceProvider(output, fileHelper));


        // Server Providers
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        BlockTagsProvider blockTagsProvider = new MachineryTagProvider.Block(output, provider, fileHelper);

        gen.addProvider(event.includeServer(), blockTagsProvider);
        gen.addProvider(event.includeServer(), new MachineryTagProvider.Item(output, provider, blockTagsProvider.contentsGetter(), fileHelper));
        gen.addProvider(event.includeServer(), new MachineryLootTableProvider(output));
        gen.addProvider(event.includeServer(), new MachineryRecipeProvider(output));

        gen.addProvider(true, new PackMetadataGenerator(output))
                .add(PackMetadataSection.TYPE,
                        new PackMetadataSection(Component.literal("Useful Machinery Resources"),
                                DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES),
                                Arrays.stream(PackType.values()).collect(Collectors.toMap(Function.identity(), DetectedVersion.BUILT_IN::getPackVersion))));
    }
}
