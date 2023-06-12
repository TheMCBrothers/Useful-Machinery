package themcbros.usefulmachinery;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.themcbrothers.lib.util.ComponentFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import themcbros.usefulmachinery.init.Registration;
import themcbros.usefulmachinery.proxy.ClientProxy;
import themcbros.usefulmachinery.proxy.CommonProxy;
import themcbros.usefulmachinery.proxy.ServerProxy;

@Mod(UsefulMachinery.MOD_ID)
public class UsefulMachinery {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "usefulmachinery";
    public static final ComponentFormatter TEXT_UTILS = new ComponentFormatter(MOD_ID);
    public static CommonProxy proxy;

    public UsefulMachinery() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Registration.register(bus);
        proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    }

    public static ResourceLocation getId(String pathIn) {
        return new ResourceLocation(MOD_ID, pathIn);
    }
}
