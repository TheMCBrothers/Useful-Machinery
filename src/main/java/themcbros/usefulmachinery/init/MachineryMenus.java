package themcbros.usefulmachinery.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.RegistryObject;
import themcbros.usefulmachinery.container.CoalGeneratorContainer;
import themcbros.usefulmachinery.container.CompactorContainer;
import themcbros.usefulmachinery.container.CrusherContainer;
import themcbros.usefulmachinery.container.ElectricSmelterContainer;
import themcbros.usefulmachinery.container.LavaGeneratorContainer;

import static themcbros.usefulmachinery.init.Registration.MENUS;

public class MachineryMenus {
    public static final RegistryObject<MenuType<CoalGeneratorContainer>> COAL_GENERATOR = MENUS.register("coal_generator", () -> new MenuType<>(CoalGeneratorContainer::new));
    public static final RegistryObject<MenuType<LavaGeneratorContainer>> LAVA_GENERATOR = MENUS.register("lava_generator", () -> new MenuType<>(LavaGeneratorContainer::new));
    public static final RegistryObject<MenuType<CrusherContainer>> CRUSHER = MENUS.register("crusher", () -> new MenuType<>(CrusherContainer::new));
    public static final RegistryObject<MenuType<ElectricSmelterContainer>> ELECTRIC_SMELTER = MENUS.register("electric_smelter", () -> new MenuType<>(ElectricSmelterContainer::new));
    public static final RegistryObject<MenuType<CompactorContainer>> COMPACTOR = MENUS.register("compactor", () -> new MenuType<>(CompactorContainer::new));

    protected static void init() {
    }
}
