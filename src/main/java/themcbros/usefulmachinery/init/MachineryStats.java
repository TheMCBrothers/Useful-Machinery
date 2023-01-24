package themcbros.usefulmachinery.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import themcbros.usefulmachinery.UsefulMachinery;

import static themcbros.usefulmachinery.init.Registration.CUSTOM_STATS;

public class MachineryStats {
    public static final RegistryObject<ResourceLocation> INTERACT_WITH_CRUSHER = CUSTOM_STATS.register("interact_with_crusher", () -> registerCustom("interact_with_crusher"));
    public static final RegistryObject<ResourceLocation> INTERACT_WITH_ELECTRIC_SMELTER = CUSTOM_STATS.register("interact_with_electric_smelter", () -> registerCustom("interact_with_electric_smelter"));
    public static final RegistryObject<ResourceLocation> INTERACT_WITH_COMPACTOR = CUSTOM_STATS.register("interact_with_compactor", () -> registerCustom("interact_with_compactor"));
    public static final RegistryObject<ResourceLocation> INTERACT_WITH_COAL_GENERATOR = CUSTOM_STATS.register("interact_with_coal_generator", () -> registerCustom("interact_with_coal_generator"));
    public static final RegistryObject<ResourceLocation> INTERACT_WITH_LAVA_GENERATOR = CUSTOM_STATS.register("interact_with_lava_generator", () -> registerCustom("interact_with_lava_generator"));

    private static @NotNull ResourceLocation registerCustom(String key) {
        ResourceLocation resourcelocation = UsefulMachinery.getId(key);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, key, resourcelocation);
        Stats.CUSTOM.get(resourcelocation, StatFormatter.DEFAULT);
        return resourcelocation;
    }

    protected static void init() {
    }
}
