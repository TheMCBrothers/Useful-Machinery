package themcbros.usefulmachinery.init;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class MachineryCreativeModeTabs {
    public static final RegistryObject<CreativeModeTab> BASE = Registration.CREATIVE_MODE_TABS.register("base",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.usefulmachinery"))
                    .icon(() -> new ItemStack(MachineryItems.BATTERY))
                    .build());


    static void init() {
    }
}
