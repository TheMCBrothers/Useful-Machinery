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
    protected void registerPackets(PacketRegistrar registrar) {
        registrar.playToServer(SetRedstoneModePacket.TYPE, SetRedstoneModePacket.STREAM_CODEC);
        registrar.playToServer(SetCompactorModePacket.TYPE, SetCompactorModePacket.STREAM_CODEC);
    }

    @Override
    protected void registerPacketsNetworkThread(PacketRegistrar registrar) {
    }
}
