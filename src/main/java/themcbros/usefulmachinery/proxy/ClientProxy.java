package themcbros.usefulmachinery.proxy;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import themcbros.usefulmachinery.blocks.AbstractMachineBlock;
import themcbros.usefulmachinery.client.screen.*;
import themcbros.usefulmachinery.init.MachineryBlocks;
import themcbros.usefulmachinery.init.MachineryItems;
import themcbros.usefulmachinery.init.MachineryMenus;
import themcbros.usefulmachinery.machine.MachineTier;

public class ClientProxy extends CommonProxy {
    public ClientProxy() {
        super();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerItemColors);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerBlockColors);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(MachineryMenus.COAL_GENERATOR.get(), CoalGeneratorScreen::new);
        MenuScreens.register(MachineryMenus.LAVA_GENERATOR.get(), LavaGeneratorScreen::new);
        MenuScreens.register(MachineryMenus.CRUSHER.get(), CrusherScreen::new);
        MenuScreens.register(MachineryMenus.ELECTRIC_SMELTER.get(), ElectricSmelterScreen::new);
        MenuScreens.register(MachineryMenus.COMPACTOR.get(), CompactorScreen::new);
    }

    private void registerItemColors(final RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (!stack.isEmpty() && stack.hasTag() && stack.getTag() != null) {
                MachineTier tier = MachineTier.byOrdinal(stack.getTag().getInt("Tier"));
                return tintIndex == 1 ? tier.getColor() : -1;
            }
            return -1;
        }, MachineryItems.TIER_UPGRADE.get());

        event.register((stack, tintIndex) -> {
            CompoundTag tag = stack.getTagElement(BlockItem.BLOCK_STATE_TAG);
            if (!stack.isEmpty() && tag != null) {
                MachineTier tier = MachineTier.valueOf(tag.getString("Tier"));

                return tintIndex == 0 ? tier.getColor() : -1;
            }
            return -1;
        }, MachineryBlocks.COAL_GENERATOR, MachineryBlocks.COMPACTOR, MachineryBlocks.LAVA_GENERATOR, MachineryBlocks.CRUSHER, MachineryBlocks.ELECTRIC_SMELTER);
    }

    private void registerBlockColors(final RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {
            if (!state.hasProperty(AbstractMachineBlock.TIER)) {
                return state.getValue(AbstractMachineBlock.TIER).getColor();
            }
            return -1;
        }, MachineryBlocks.COAL_GENERATOR.get(), MachineryBlocks.COMPACTOR.get(), MachineryBlocks.LAVA_GENERATOR.get(), MachineryBlocks.CRUSHER.get(), MachineryBlocks.ELECTRIC_SMELTER.get());
    }
}
