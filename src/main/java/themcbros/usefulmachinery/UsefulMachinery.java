package themcbros.usefulmachinery;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import themcbros.usefulmachinery.init.ModItems;
import themcbros.usefulmachinery.proxy.ClientProxy;
import themcbros.usefulmachinery.proxy.ServerProxy;

@Mod(UsefulMachinery.MOD_ID)
public class UsefulMachinery {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "usefulmachinery";

    public static final ItemGroup GROUP = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.BATTERY);
        }
    };

    public UsefulMachinery() {
        DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    }

    public static ResourceLocation getId(String pathIn) {
        return new ResourceLocation(MOD_ID, pathIn);
    }

}
