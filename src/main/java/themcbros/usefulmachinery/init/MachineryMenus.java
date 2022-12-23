package themcbros.usefulmachinery.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.RegistryObject;
import themcbros.usefulmachinery.menu.CoalGeneratorMenu;
import themcbros.usefulmachinery.menu.CompactorMenu;
import themcbros.usefulmachinery.menu.CrusherMenu;
import themcbros.usefulmachinery.menu.ElectricSmelterMenu;
import themcbros.usefulmachinery.menu.LavaGeneratorMenu;

import static themcbros.usefulmachinery.init.Registration.MENUS;

public class MachineryMenus {
    public static final RegistryObject<MenuType<CoalGeneratorMenu>> COAL_GENERATOR = MENUS.register("coal_generator", () -> new MenuType<>(CoalGeneratorMenu::new));
    public static final RegistryObject<MenuType<LavaGeneratorMenu>> LAVA_GENERATOR = MENUS.register("lava_generator", () -> new MenuType<>(LavaGeneratorMenu::new));
    public static final RegistryObject<MenuType<CrusherMenu>> CRUSHER = MENUS.register("crusher", () -> new MenuType<>(CrusherMenu::new));
    public static final RegistryObject<MenuType<ElectricSmelterMenu>> ELECTRIC_SMELTER = MENUS.register("electric_smelter", () -> new MenuType<>(ElectricSmelterMenu::new));
    public static final RegistryObject<MenuType<CompactorMenu>> COMPACTOR = MENUS.register("compactor", () -> new MenuType<>(CompactorMenu::new));

    protected static void init() {
    }
}
