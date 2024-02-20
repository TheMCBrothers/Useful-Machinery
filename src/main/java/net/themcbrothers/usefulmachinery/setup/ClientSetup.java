package net.themcbrothers.usefulmachinery.setup;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.themcbrothers.usefulmachinery.block.AbstractMachineBlock;
import net.themcbrothers.usefulmachinery.client.screen.*;
import net.themcbrothers.usefulmachinery.core.MachineryItems;
import net.themcbrothers.usefulmachinery.core.MachineryMenus;
import net.themcbrothers.usefulmachinery.machine.MachineTier;

import static net.themcbrothers.usefulmachinery.core.MachineryBlocks.*;

public class ClientSetup extends CommonSetup {
    public ClientSetup(IEventBus modEventBus, ModContainer modContainer) {
        super(modEventBus, modContainer);

        modEventBus.addListener(this::menuScreens);
        modEventBus.addListener(this::itemColors);
        modEventBus.addListener(this::blockColors);
    }

    private void menuScreens(final RegisterMenuScreensEvent event) {
        event.register(MachineryMenus.COAL_GENERATOR.get(), CoalGeneratorScreen::new);
        event.register(MachineryMenus.COMPACTOR.get(), CompactorScreen::new);
        event.register(MachineryMenus.CRUSHER.get(), CrusherScreen::new);
        event.register(MachineryMenus.ELECTRIC_SMELTER.get(), ElectricSmelterScreen::new);
        event.register(MachineryMenus.LAVA_GENERATOR.get(), LavaGeneratorScreen::new);
    }

    private void itemColors(final RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (stack.hasTag() && stack.getTag() != null) {
                MachineTier tier = MachineTier.byOrdinal(stack.getTag().getInt("Tier"));
                return tintIndex == 1 ? tier.getColor() : -1;
            }

            return -1;
        }, MachineryItems.TIER_UPGRADE.get());

        event.register((stack, tintIndex) -> {
            MachineTier tier = MachineTier.SIMPLE;
            CompoundTag tag = stack.getTagElement(BlockItem.BLOCK_STATE_TAG);

            if (!stack.isEmpty() && tag != null) {
                MachineTier tierFromItem = MachineTier.CODEC.byName(tag.getString("tier"));
                tier = tierFromItem != null ? tierFromItem : tier;
            }

            return tier.getColor();
        }, COAL_GENERATOR, COMPACTOR, CRUSHER, ELECTRIC_SMELTER, LAVA_GENERATOR);
    }

    private void blockColors(final RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {
            EnumProperty<MachineTier> tier = AbstractMachineBlock.TIER;

            if (state.hasProperty(tier)) {
                return state.getValue(tier).getColor();
            }

            return -1;
        }, COAL_GENERATOR.get(), COMPACTOR.get(), CRUSHER.get(), ELECTRIC_SMELTER.get(), LAVA_GENERATOR.get());
    }
}
