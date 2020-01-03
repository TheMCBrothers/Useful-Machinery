package themcbros.usefulmachinery.init;

import com.google.common.collect.Lists;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.container.CoalGeneratorContainer;
import themcbros.usefulmachinery.container.CrusherContainer;
import themcbros.usefulmachinery.container.ElectricSmelterContainer;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = UsefulMachinery.MOD_ID)
public class ModContainers {

    private static final List<ContainerType<?>> CONTAINER_TYPES = Lists.newArrayList();

    public static final ContainerType<CoalGeneratorContainer> COAL_GENERATOR = register("coal_generator", new ContainerType<>(CoalGeneratorContainer::new));
    public static final ContainerType<CrusherContainer> CRUSHER = register("crusher", new ContainerType<>(CrusherContainer::new));
    public static final ContainerType<ElectricSmelterContainer> ELECTRIC_SMELTER = register("electric_smelter", new ContainerType<>(ElectricSmelterContainer::new));

    private static <T extends ContainerType<?>> T register(String registryName, T containerType) {
        containerType.setRegistryName(UsefulMachinery.getId(registryName));
        CONTAINER_TYPES.add(containerType);
        return containerType;
    }

    @SubscribeEvent
    public static void onBlockRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
        CONTAINER_TYPES.forEach(event.getRegistry()::register);
    }

}
