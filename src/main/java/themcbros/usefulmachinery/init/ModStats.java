package themcbros.usefulmachinery.init;

import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import themcbros.usefulmachinery.UsefulMachinery;

public class ModStats {

    public static final ResourceLocation INTERACT_WITH_CRUSHER = registerCustom("interact_with_crusher", IStatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_ELECTRIC_SMELTER = registerCustom("interact_with_electric_smelter", IStatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_COMPACTOR = registerCustom("interact_with_compactor", IStatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_COAL_GENERATOR = registerCustom("interact_with_coal_generator", IStatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_LAVA_GENERATOR = registerCustom("interact_with_lava_generator", IStatFormatter.DEFAULT);

    private static ResourceLocation registerCustom(String key, IStatFormatter formatter) {
        ResourceLocation resourcelocation = UsefulMachinery.getId(key);
        Registry.register(Registry.CUSTOM_STAT, key, resourcelocation);
        Stats.CUSTOM.get(resourcelocation, formatter);
        return resourcelocation;
    }

}
