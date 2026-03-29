package net.themcbrothers.usefulmachinery.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.themcbrothers.lib.network.PacketMessage;
import net.themcbrothers.lib.network.PacketUtils;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import net.themcbrothers.usefulmachinery.machine.RedstoneMode;
import net.themcbrothers.usefulmachinery.menu.AbstractMachineMenu;

public record SetRedstoneModePacket(RedstoneMode mode) implements PacketMessage {
    public static final Type<SetRedstoneModePacket> TYPE = new Type<>(UsefulMachinery.id("set_redstone_mode"));
    public static final StreamCodec<FriendlyByteBuf, SetRedstoneModePacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SetRedstoneModePacket decode(FriendlyByteBuf buffer) {
            return new SetRedstoneModePacket(RedstoneMode.STREAM_CODEC.decode(buffer));
        }

        @Override
        public void encode(FriendlyByteBuf buffer, SetRedstoneModePacket packet) {
            RedstoneMode.STREAM_CODEC.encode(buffer, packet.mode());
        }
    };

    @Override
    public void handle(IPayloadContext context) {
        PacketUtils.container(context, AbstractMachineMenu.class)
                .ifPresent(menu -> {
                    AbstractMachineBlockEntity blockEntity = menu.getBlockEntity();
                    blockEntity.setRedstoneMode(this.mode);
                });
    }

    @Override
    public Type<SetRedstoneModePacket> type() {
        return TYPE;
    }
}
