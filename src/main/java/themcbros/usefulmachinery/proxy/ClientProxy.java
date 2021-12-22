package themcbros.usefulmachinery.proxy;

import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import themcbros.usefulmachinery.client.gui.*;
import themcbros.usefulmachinery.init.MachineryBlockEntities;
import themcbros.usefulmachinery.init.MachineryContainers;
import themcbros.usefulmachinery.init.MachineryItems;
import themcbros.usefulmachinery.machine.MachineTier;
import themcbros.usefulmachinery.blockentity.renderer.MachineRenderer;

public class ClientProxy extends CommonProxy {
    public ClientProxy() {
        super();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerItemColors);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(MachineryContainers.COAL_GENERATOR, CoalGeneratorScreen::new);
        MenuScreens.register(MachineryContainers.LAVA_GENERATOR, LavaGeneratorScreen::new);
        MenuScreens.register(MachineryContainers.CRUSHER, CrusherScreen::new);
        MenuScreens.register(MachineryContainers.ELECTRIC_SMELTER, ElectricSmelterScreen::new);
        MenuScreens.register(MachineryContainers.COMPACTOR, CompactorScreen::new);

        BlockEntityRenderers.register(MachineryBlockEntities.COAL_GENERATOR, MachineRenderer::new);
        BlockEntityRenderers.register(MachineryBlockEntities.LAVA_GENERATOR, MachineRenderer::new);
        BlockEntityRenderers.register(MachineryBlockEntities.CRUSHER, MachineRenderer::new);
        BlockEntityRenderers.register(MachineryBlockEntities.ELECTRIC_SMELTER, MachineRenderer::new);
        BlockEntityRenderers.register(MachineryBlockEntities.COMPACTOR, MachineRenderer::new);
    }

    private void registerItemColors(final ColorHandlerEvent.Item event) {
        ItemColors itemColors = event.getItemColors();

        itemColors.register((stack, tintIndex) -> {
            if (!stack.isEmpty() && stack.hasTag() && stack.getTag() != null) {
                MachineTier tier = MachineTier.byOrdinal(stack.getTag().getInt("Tier"));
                return tintIndex == 1 ? tier.getColor() : -1;
            }
            return -1;
        }, MachineryItems.TIER_UPGRADE);
    }
}
