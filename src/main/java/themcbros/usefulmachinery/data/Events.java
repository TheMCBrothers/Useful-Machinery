package themcbros.usefulmachinery.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import themcbros.usefulmachinery.UsefulMachinery;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = UsefulMachinery.MOD_ID)
public class Events {
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            gen.addProvider(new MachineryBlockStateProvider(gen, fileHelper));
            gen.addProvider(new MachineryItemModelProvider(gen, fileHelper));
            gen.addProvider(new MachineryLanguageProvider(gen));
        }

        if (event.includeServer()) {
            BlockTagsProvider blockProvider = new MachineryTagProvider.Block(gen, fileHelper);

            gen.addProvider(blockProvider);
            gen.addProvider(new MachineryTagProvider.Item(gen, blockProvider, fileHelper));
            gen.addProvider(new MachineryLootTableProvider(gen));
        }
    }
}
