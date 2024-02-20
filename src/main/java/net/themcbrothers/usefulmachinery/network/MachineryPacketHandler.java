package net.themcbrothers.usefulmachinery.network;

import net.neoforged.bus.api.IEventBus;
import net.themcbrothers.lib.network.BasePacketHandler;
import net.themcbrothers.lib.util.Version;
import net.themcbrothers.usefulmachinery.UsefulMachinery;

public class MachineryPacketHandler extends BasePacketHandler {
    public MachineryPacketHandler(IEventBus modEventBus, Version version) {
        super(modEventBus, UsefulMachinery.MOD_ID, version);
    }

    @Override
    protected void registerClientToServer(PacketRegistrar registrar) {
        registrar.play(SetRedstoneModePacket.ID, SetRedstoneModePacket::new);
        registrar.play(SetCompactorModePacket.ID, SetCompactorModePacket::new);
    }

    @Override
    protected void registerServerToClient(PacketRegistrar registrar) {
    }
}
