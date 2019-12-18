package themcbros.usefulmachinery;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import themcbros.usefulmachinery.proxy.ClientProxy;
import themcbros.usefulmachinery.proxy.ServerProxy;

@Mod(UsefulMachinery.MOD_ID)
public class UsefulMachinery {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "usefulmachinery";

    public UsefulMachinery() {
        DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    }

    public static ResourceLocation getId(String pathIn) {
        return new ResourceLocation(MOD_ID, pathIn);
    }

}
