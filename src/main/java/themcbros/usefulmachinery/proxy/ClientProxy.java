package themcbros.usefulmachinery.proxy;

import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import themcbros.usefulmachinery.client.gui.CoalGeneratorScreen;
import themcbros.usefulmachinery.client.gui.CompactorScreen;
import themcbros.usefulmachinery.client.gui.CrusherScreen;
import themcbros.usefulmachinery.client.gui.ElectricSmelterScreen;
import themcbros.usefulmachinery.client.gui.LavaGeneratorScreen;
import themcbros.usefulmachinery.client.renderer.MachineRenderer;
import themcbros.usefulmachinery.init.MachineryBlockEntities;
import themcbros.usefulmachinery.init.MachineryMenus;
import themcbros.usefulmachinery.init.MachineryItems;
import themcbros.usefulmachinery.machine.MachineTier;

public class ClientProxy extends CommonProxy {
    public ClientProxy() {
        super();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerItemColors);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(MachineryMenus.COAL_GENERATOR.get(), CoalGeneratorScreen::new);
        MenuScreens.register(MachineryMenus.LAVA_GENERATOR.get(), LavaGeneratorScreen::new);
        MenuScreens.register(MachineryMenus.CRUSHER.get(), CrusherScreen::new);
        MenuScreens.register(MachineryMenus.ELECTRIC_SMELTER.get(), ElectricSmelterScreen::new);
        MenuScreens.register(MachineryMenus.COMPACTOR.get(), CompactorScreen::new);

        BlockEntityRenderers.register(MachineryBlockEntities.COAL_GENERATOR.get(), context4 -> new MachineRenderer());
        BlockEntityRenderers.register(MachineryBlockEntities.LAVA_GENERATOR.get(), context3 -> new MachineRenderer());
        BlockEntityRenderers.register(MachineryBlockEntities.CRUSHER.get(), context2 -> new MachineRenderer());
        BlockEntityRenderers.register(MachineryBlockEntities.ELECTRIC_SMELTER.get(), context1 -> new MachineRenderer());
        BlockEntityRenderers.register(MachineryBlockEntities.COMPACTOR.get(), context -> new MachineRenderer());
    }

    private void registerItemColors(final ColorHandlerEvent.Item event) {
        ItemColors itemColors = event.getItemColors();

        itemColors.register((stack, tintIndex) -> {
            if (!stack.isEmpty() && stack.hasTag() && stack.getTag() != null) {
                MachineTier tier = MachineTier.byOrdinal(stack.getTag().getInt("Tier"));
                return tintIndex == 1 ? tier.getColor() : -1;
            }
            return -1;
        }, MachineryItems.TIER_UPGRADE.get());
    }
}
