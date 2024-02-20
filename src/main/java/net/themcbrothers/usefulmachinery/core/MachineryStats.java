package net.themcbrothers.usefulmachinery.core;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import static net.themcbrothers.usefulmachinery.core.Registration.CUSTOM_STATS;

public class MachineryStats {
    public static final DeferredHolder<ResourceLocation, ResourceLocation> INTERACTION_WITH_CRUSHER = CUSTOM_STATS.register("interact_with_crusher", location -> location);
    public static final DeferredHolder<ResourceLocation, ResourceLocation> INTERACTION_WITH_ELECTRIC_SMELTER = CUSTOM_STATS.register("interact_with_electric_smelter", location -> location);
    public static final DeferredHolder<ResourceLocation, ResourceLocation> INTERACTION_WITH_COMPACTOR = CUSTOM_STATS.register("interact_with_compactor", location -> location);
    public static final DeferredHolder<ResourceLocation, ResourceLocation> INTERACTION_WITH_COAL_GENERATOR = CUSTOM_STATS.register("interact_with_coal_generator", location -> location);
    public static final DeferredHolder<ResourceLocation, ResourceLocation> INTERACTION_WITH_LAVA_GENERATOR = CUSTOM_STATS.register("interact_with_lava_generator", location -> location);

    static void init() {
    }
}
