package net.themcbrothers.usefulmachinery.setup;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

public class ServerSetup extends CommonSetup {
    public ServerSetup(IEventBus modEventBus, ModContainer modContainer) {
        super(modEventBus, modContainer);
    }
}
