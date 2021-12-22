package themcbros.usefulmachinery;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import themcbros.usefulmachinery.init.MachineryItems;
import themcbros.usefulmachinery.proxy.ClientProxy;
import themcbros.usefulmachinery.proxy.ServerProxy;

import javax.annotation.Nonnull;

@Mod(UsefulMachinery.MOD_ID)
public class UsefulMachinery {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "usefulmachinery";

    public static final CreativeModeTab GROUP = new CreativeModeTab(MOD_ID) {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(MachineryItems.BATTERY);
        }
    };

    public UsefulMachinery() {
        DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    }

    public static ResourceLocation getId(String pathIn) {
        return new ResourceLocation(MOD_ID, pathIn);
    }
}
