package themcbros.usefulmachinery.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
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
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        // Client Providers
        gen.addProvider(event.includeClient(), new MachineryBlockStateProvider(gen, fileHelper));
        gen.addProvider(event.includeClient(), new MachineryItemModelProvider(gen, fileHelper));
        gen.addProvider(event.includeClient(), new MachineryLanguageProvider(gen));


        // Server Providers
        BlockTagsProvider blockProvider = new MachineryTagProvider.Block(gen, fileHelper);

        gen.addProvider(event.includeServer(), blockProvider);
        gen.addProvider(event.includeServer(), new MachineryTagProvider.Item(gen, blockProvider, fileHelper));
        gen.addProvider(event.includeServer(), new MachineryLootTableProvider(gen));
        gen.addProvider(event.includeServer(), new MachineryRecipeProvider(gen));
    }
}
