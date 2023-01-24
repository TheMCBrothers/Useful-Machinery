package themcbros.usefulmachinery.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import themcbros.usefulmachinery.UsefulMachinery;

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
        BlockTagsProvider blockProvider = new MachineryTagProvider.Block(output, event.getLookupProvider(), fileHelper);

        gen.addProvider(event.includeServer(), blockProvider);
        gen.addProvider(event.includeServer(), new MachineryTagProvider.Item(output, event.getLookupProvider(), blockProvider, fileHelper));
        gen.addProvider(event.includeServer(), new MachineryLootTableProvider(output));
        gen.addProvider(event.includeServer(), new MachineryRecipeProvider(output));
    }
}
