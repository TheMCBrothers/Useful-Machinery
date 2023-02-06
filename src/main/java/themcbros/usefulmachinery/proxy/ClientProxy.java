package themcbros.usefulmachinery.proxy;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
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

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::clientSetup);
        bus.addListener(this::blockColors);
        bus.addListener(this::itemColors);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(MachineryMenus.COAL_GENERATOR.get(), CoalGeneratorScreen::new);
        MenuScreens.register(MachineryMenus.LAVA_GENERATOR.get(), LavaGeneratorScreen::new);
        MenuScreens.register(MachineryMenus.CRUSHER.get(), CrusherScreen::new);
        MenuScreens.register(MachineryMenus.ELECTRIC_SMELTER.get(), ElectricSmelterScreen::new);
        MenuScreens.register(MachineryMenus.COMPACTOR.get(), CompactorScreen::new);
    }

    private void itemColors(final RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (!stack.isEmpty() && stack.hasTag() && stack.getTag() != null) {
                MachineTier tier = MachineTier.byOrdinal(stack.getTag().getInt("Tier"));
                return tintIndex == 1 ? tier.getColor() : -1;
            }
            return -1;
        }, MachineryItems.TIER_UPGRADE.get());

        event.register((stack, tintIndex) -> {
            MachineTier tier = MachineTier.SIMPLE;
            CompoundTag tag = stack.getTagElement(BlockItem.BLOCK_STATE_TAG);
            if (!stack.isEmpty() && tag != null) {
                MachineTier tierFromItem = MachineTier.byName(tag.getString("tier"));
                tier = tierFromItem != null ? tierFromItem : tier;
            }
            return tier.getColor();
        }, MachineryBlocks.COAL_GENERATOR, MachineryBlocks.COMPACTOR, MachineryBlocks.LAVA_GENERATOR, MachineryBlocks.CRUSHER, MachineryBlocks.ELECTRIC_SMELTER);
    }

    private void blockColors(final RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {
            if (state.hasProperty(AbstractMachineBlock.TIER)) {
                return state.getValue(AbstractMachineBlock.TIER).getColor();
            }
            return -1;
        }, MachineryBlocks.COAL_GENERATOR.get(), MachineryBlocks.COMPACTOR.get(), MachineryBlocks.LAVA_GENERATOR.get(), MachineryBlocks.CRUSHER.get(), MachineryBlocks.ELECTRIC_SMELTER.get());
    }
}
