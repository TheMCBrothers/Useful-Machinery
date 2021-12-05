package themcbros.usefulmachinery.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import themcbros.usefulmachinery.UsefulMachinery;

public class ModStats {
    public static final ResourceLocation INTERACT_WITH_CRUSHER = registerCustom("interact_with_crusher", StatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_ELECTRIC_SMELTER = registerCustom("interact_with_electric_smelter", StatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_COMPACTOR = registerCustom("interact_with_compactor", StatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_COAL_GENERATOR = registerCustom("interact_with_coal_generator", StatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_LAVA_GENERATOR = registerCustom("interact_with_lava_generator", StatFormatter.DEFAULT);

    private static ResourceLocation registerCustom(String key, StatFormatter formatter) {
        ResourceLocation resourcelocation = UsefulMachinery.getId(key);
        Registry.register(Registry.CUSTOM_STAT, key, resourcelocation);
        Stats.CUSTOM.get(resourcelocation, formatter);
        return resourcelocation;
    }
}
