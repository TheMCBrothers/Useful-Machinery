package themcbros.usefulmachinery.init;

import com.google.common.collect.Lists;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import themcbros.usefulmachinery.UsefulMachinery;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = UsefulMachinery.MOD_ID)
public class ModItems {

    private static final List<Item> ITEMS = Lists.newArrayList();

    public static final BlockItem COAL_GENERATOR = register("coal_generator", new BlockItem(ModBlocks.COAL_GENERATOR, new Item.Properties().group(ItemGroup.DECORATIONS)));
    public static final BlockItem CRUSHER = register("crusher", new BlockItem(ModBlocks.CRUSHER, new Item.Properties().group(ItemGroup.DECORATIONS)));

    private static <T extends Item> T register(String registryName, T item) {
        item.setRegistryName(UsefulMachinery.getId(registryName));
        ITEMS.add(item);
        return item;
    }

    @SubscribeEvent
    public static void onBlockRegistry(final RegistryEvent.Register<Item> event) {
        ITEMS.forEach(event.getRegistry()::register);
    }

}
