package themcbros.usefulmachinery.init;

import com.google.common.collect.Lists;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.container.*;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = UsefulMachinery.MOD_ID)
public class MachineryContainers {
    private static final List<MenuType<?>> CONTAINER_TYPES = Lists.newArrayList();

    public static final MenuType<CoalGeneratorContainer> COAL_GENERATOR = register("coal_generator", new MenuType<>(CoalGeneratorContainer::new));
    public static final MenuType<LavaGeneratorContainer> LAVA_GENERATOR = register("lava_generator", new MenuType<>(LavaGeneratorContainer::new));
    public static final MenuType<CrusherContainer> CRUSHER = register("crusher", new MenuType<>(CrusherContainer::new));
    public static final MenuType<ElectricSmelterContainer> ELECTRIC_SMELTER = register("electric_smelter", new MenuType<>(ElectricSmelterContainer::new));
    public static final MenuType<CompactorContainer> COMPACTOR = register("compactor", new MenuType<>(CompactorContainer::new));

    private static <T extends MenuType<?>> T register(String registryName, T containerType) {
        containerType.setRegistryName(UsefulMachinery.getId(registryName));
        CONTAINER_TYPES.add(containerType);
        return containerType;
    }

    @SubscribeEvent
    public static void onBlockRegistry(final RegistryEvent.Register<MenuType<?>> event) {
        CONTAINER_TYPES.forEach(event.getRegistry()::register);
    }
}
