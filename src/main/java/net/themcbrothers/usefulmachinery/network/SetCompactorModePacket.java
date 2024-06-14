package net.themcbrothers.usefulmachinery.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.themcbrothers.lib.network.PacketMessage;
import net.themcbrothers.lib.network.PacketUtils;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import net.themcbrothers.usefulmachinery.block.entity.CompactorBlockEntity;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;
import net.themcbrothers.usefulmachinery.menu.CompactorMenu;

public record SetCompactorModePacket(CompactorMode mode) implements PacketMessage {
    public static final Type<SetCompactorModePacket> TYPE = new Type<>(UsefulMachinery.rl("set_compactor_mode"));
    public static final StreamCodec<FriendlyByteBuf, SetCompactorModePacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SetCompactorModePacket decode(FriendlyByteBuf buffer) {
            return new SetCompactorModePacket(CompactorMode.STREAM_CODEC.decode(buffer));
        }

        @Override
        public void encode(FriendlyByteBuf buffer, SetCompactorModePacket packet) {
            CompactorMode.STREAM_CODEC.encode(buffer, packet.mode());
        }
    };

    @Override
    public void handle(IPayloadContext context) {
        PacketUtils.container(context, CompactorMenu.class)
                .ifPresent(menu -> {
                    AbstractMachineBlockEntity blockEntity = menu.getBlockEntity();
                    if (blockEntity instanceof CompactorBlockEntity compactorBlockEntity) {
                        compactorBlockEntity.setMode(this.mode);
                    }
                });
    }

    @Override
    public Type<SetCompactorModePacket> type() {
        return TYPE;
    }
}
