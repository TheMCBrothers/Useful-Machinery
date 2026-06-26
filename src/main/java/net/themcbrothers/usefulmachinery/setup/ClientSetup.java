package net.themcbrothers.usefulmachinery.setup;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.client.screen.*;
import net.themcbrothers.usefulmachinery.client.tintsources.TierBlockTintSource;
import net.themcbrothers.usefulmachinery.client.tintsources.TierItemTintSource;
import net.themcbrothers.usefulmachinery.core.MachineryMenus;

import java.util.List;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.id;
import static net.themcbrothers.usefulmachinery.core.MachineryBlocks.*;

@Mod(value = UsefulMachinery.MOD_ID, dist = Dist.CLIENT)
public class ClientSetup {
    public ClientSetup(IEventBus modEventBus) {
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

    private void itemColors(final RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(id("tier"), TierItemTintSource.MAP_CODEC);
    }

    private void blockColors(final RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(
                List.of(TierBlockTintSource.INSTANCE),
                COAL_GENERATOR.get(),
                COMPACTOR.get(),
                CRUSHER.get(),
                ELECTRIC_SMELTER.get(),
                LAVA_GENERATOR.get()
        );
    }
}
