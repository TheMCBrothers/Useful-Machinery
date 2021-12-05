package themcbros.usefulmachinery.proxy;

import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import themcbros.usefulmachinery.client.gui.*;
import themcbros.usefulmachinery.init.ModContainers;
import themcbros.usefulmachinery.init.ModItems;
import themcbros.usefulmachinery.init.ModTileEntities;
import themcbros.usefulmachinery.machine.MachineTier;
import themcbros.usefulmachinery.tileentity.renderer.MachineRenderer;

public class ClientProxy extends CommonProxy {
    public ClientProxy() {
        super();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerItemColors);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(ModContainers.COAL_GENERATOR, CoalGeneratorScreen::new);
        MenuScreens.register(ModContainers.LAVA_GENERATOR, LavaGeneratorScreen::new);
        MenuScreens.register(ModContainers.CRUSHER, CrusherScreen::new);
        MenuScreens.register(ModContainers.ELECTRIC_SMELTER, ElectricSmelterScreen::new);
        MenuScreens.register(ModContainers.COMPACTOR, CompactorScreen::new);

        BlockEntityRenderers.register(ModTileEntities.COAL_GENERATOR, MachineRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.LAVA_GENERATOR, MachineRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.CRUSHER, MachineRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.ELECTRIC_SMELTER, MachineRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.COMPACTOR, MachineRenderer::new);
    }

    private void registerItemColors(final ColorHandlerEvent.Item event) {
        ItemColors itemColors = event.getItemColors();

        itemColors.register((stack, tintIndex) -> {
            if (!stack.isEmpty() && stack.hasTag() && stack.getTag() != null) {
                MachineTier tier = MachineTier.byOrdinal(stack.getTag().getInt("Tier"));
                return tintIndex == 1 ? tier.getColor() : -1;
            }
            return -1;
        }, ModItems.TIER_UPGRADE);
    }
}
