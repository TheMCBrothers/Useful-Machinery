package net.themcbrothers.usefulmachinery.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.themcbrothers.lib.network.PacketMessage;
import net.themcbrothers.lib.network.PacketUtils;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import net.themcbrothers.usefulmachinery.block.entity.CompactorBlockEntity;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;
import net.themcbrothers.usefulmachinery.menu.CompactorMenu;

public record SetCompactorModePacket(CompactorMode mode) implements PacketMessage<PlayPayloadContext> {

    public static final ResourceLocation ID = UsefulMachinery.rl("set_compactor_mode");

    public SetCompactorModePacket(FriendlyByteBuf buffer) {
        this(CompactorMode.byOrdinal(buffer.readByte()));
    }

    @Override
    public void handle(PlayPayloadContext context) {
        PacketUtils.container(context, CompactorMenu.class)
                .ifPresent(menu -> {
                    AbstractMachineBlockEntity blockEntity = menu.getBlockEntity();
                    if (blockEntity instanceof CompactorBlockEntity compactorBlockEntity) {
                        compactorBlockEntity.setMode(this.mode);
                    }
                });
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeByte(this.mode.ordinal());
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
