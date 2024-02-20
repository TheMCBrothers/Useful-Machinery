package net.themcbrothers.usefulmachinery.core;

import com.google.gson.JsonArray;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.themcbrothers.usefulmachinery.menu.*;

import static net.themcbrothers.usefulmachinery.core.Registration.MENUS;

public class MachineryMenus {
    public static final DeferredHolder<MenuType<?>, MenuType<CoalGeneratorMenu>> COAL_GENERATOR = MENUS.register("coal_generator", CoalGeneratorMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<LavaGeneratorMenu>> LAVA_GENERATOR = MENUS.register("lava_generator", LavaGeneratorMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<ElectricSmelterMenu>> ELECTRIC_SMELTER = MENUS.register("electric_smelter", ElectricSmelterMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<CrusherMenu>> CRUSHER = MENUS.register("crusher", CrusherMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<CompactorMenu>> COMPACTOR = MENUS.register("compactor", CompactorMenu::new);

    static void init() {
    }
}
