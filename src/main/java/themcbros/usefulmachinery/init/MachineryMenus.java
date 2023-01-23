package themcbros.usefulmachinery.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.RegistryObject;
import themcbros.usefulmachinery.menu.*;

import static themcbros.usefulmachinery.init.Registration.MENUS;

public class MachineryMenus {
    public static final RegistryObject<MenuType<CoalGeneratorMenu>> COAL_GENERATOR = MENUS.register("coal_generator", CoalGeneratorMenu::new);
    public static final RegistryObject<MenuType<LavaGeneratorMenu>> LAVA_GENERATOR = MENUS.register("lava_generator", LavaGeneratorMenu::new);
    public static final RegistryObject<MenuType<CrusherMenu>> CRUSHER = MENUS.register("crusher", CrusherMenu::new);
    public static final RegistryObject<MenuType<ElectricSmelterMenu>> ELECTRIC_SMELTER = MENUS.register("electric_smelter", ElectricSmelterMenu::new);
    public static final RegistryObject<MenuType<CompactorMenu>> COMPACTOR = MENUS.register("compactor", CompactorMenu::new);

    protected static void init() {
    }
}
