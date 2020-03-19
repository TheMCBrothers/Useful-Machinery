package themcbros.usefulmachinery.proxy;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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
        ScreenManager.registerFactory(ModContainers.COAL_GENERATOR, CoalGeneratorScreen::new);
        ScreenManager.registerFactory(ModContainers.LAVA_GENERATOR, LavaGeneratorScreen::new);
        ScreenManager.registerFactory(ModContainers.CRUSHER, CrusherScreen::new);
        ScreenManager.registerFactory(ModContainers.ELECTRIC_SMELTER, ElectricSmelterScreen::new);
        ScreenManager.registerFactory(ModContainers.COMPACTOR, CompactorScreen::new);

        ClientRegistry.bindTileEntityRenderer(ModTileEntities.COAL_GENERATOR, MachineRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.LAVA_GENERATOR, MachineRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.CRUSHER, MachineRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.ELECTRIC_SMELTER, MachineRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.COMPACTOR, MachineRenderer::new);
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
