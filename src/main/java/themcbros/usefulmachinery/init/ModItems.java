package themcbros.usefulmachinery.init;

import com.google.common.collect.Lists;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.items.BatteryItem;
import themcbros.usefulmachinery.items.CreativePowerCellItem;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = UsefulMachinery.MOD_ID)
public class ModItems {

    private static final List<Item> ITEMS = Lists.newArrayList();

    public static final BlockItem COAL_GENERATOR = register("coal_generator", new BlockItem(ModBlocks.COAL_GENERATOR, new Item.Properties().group(UsefulMachinery.GROUP)));
    public static final BlockItem LAVA_GENERATOR = register("lava_generator", new BlockItem(ModBlocks.LAVA_GENERATOR, new Item.Properties().group(UsefulMachinery.GROUP)));
    public static final BlockItem CRUSHER = register("crusher", new BlockItem(ModBlocks.CRUSHER, new Item.Properties().group(UsefulMachinery.GROUP)));
    public static final BlockItem ELECTRIC_SMELTER = register("electric_smelter", new BlockItem(ModBlocks.ELECTRIC_SMELTER, new Item.Properties().group(UsefulMachinery.GROUP)));
    public static final BlockItem COMPACTOR = register("compactor", new BlockItem(ModBlocks.COMPACTOR, new Item.Properties().group(UsefulMachinery.GROUP)));
    public static final BlockItem CREATIVE_POWER_CELL = register("creative_power_cell", new CreativePowerCellItem(ModBlocks.CREATIVE_POWER_CELL, new Item.Properties().group(UsefulMachinery.GROUP).maxStackSize(1)));

    public static final BatteryItem BATTERY = register("battery", new BatteryItem(new Item.Properties().group(UsefulMachinery.GROUP).maxStackSize(1)));
    public static final Item MACHINE_FRAME = register("machine_frame", new Item(new Item.Properties().group(UsefulMachinery.GROUP)));
    public static final Item COMPACTOR_KIT = register("compactor_kit", new Item(new Item.Properties().group(UsefulMachinery.GROUP).maxStackSize(1)));

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
