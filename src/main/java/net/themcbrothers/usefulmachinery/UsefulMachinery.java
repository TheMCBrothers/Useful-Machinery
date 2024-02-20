package net.themcbrothers.usefulmachinery;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.themcbrothers.lib.util.ComponentFormatter;
import net.themcbrothers.usefulmachinery.setup.ClientSetup;
import net.themcbrothers.usefulmachinery.setup.CommonSetup;
import net.themcbrothers.usefulmachinery.setup.ServerSetup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(UsefulMachinery.MOD_ID)
public class UsefulMachinery {
    public static final String MOD_ID = "usefulmachinery";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ComponentFormatter TEXT_UTILS = new ComponentFormatter(MOD_ID);
    public static CommonSetup setup;

    public UsefulMachinery(IEventBus modEventBus, ModContainer modContainer) {
        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
            setup = new ServerSetup(modEventBus, modContainer);
        } else {
            setup = new ClientSetup(modEventBus, modContainer);
        }
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
