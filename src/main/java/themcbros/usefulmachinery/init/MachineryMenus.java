package themcbros.usefulmachinery.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.RegistryObject;
import themcbros.usefulmachinery.container.*;

import static themcbros.usefulmachinery.init.Registration.MENUS;

public class MachineryMenus {
    public static final RegistryObject<MenuType<CoalGeneratorContainer>> COAL_GENERATOR = MENUS.register("coal_generator", CoalGeneratorContainer::new);
    public static final RegistryObject<MenuType<LavaGeneratorContainer>> LAVA_GENERATOR = MENUS.register("lava_generator", LavaGeneratorContainer::new);
    public static final RegistryObject<MenuType<CrusherContainer>> CRUSHER = MENUS.register("crusher", CrusherContainer::new);
    public static final RegistryObject<MenuType<ElectricSmelterContainer>> ELECTRIC_SMELTER = MENUS.register("electric_smelter", ElectricSmelterContainer::new);
    public static final RegistryObject<MenuType<CompactorContainer>> COMPACTOR = MENUS.register("compactor", CompactorContainer::new);

    protected static void init() {
    }
}
