package net.themcbrothers.usefulmachinery.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.themcbrothers.lib.network.PacketMessage;
import net.themcbrothers.lib.network.PacketUtils;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import net.themcbrothers.usefulmachinery.machine.RedstoneMode;
import net.themcbrothers.usefulmachinery.menu.AbstractMachineMenu;

public record SetRedstoneModePacket(RedstoneMode mode) implements PacketMessage<PlayPayloadContext> {

    public static final ResourceLocation ID = UsefulMachinery.rl("set_redstone_mode");

    public SetRedstoneModePacket(FriendlyByteBuf buffer) {
        this(RedstoneMode.byOrdinal(buffer.readByte()));
    }

    @Override
    public void handle(PlayPayloadContext context) {
        PacketUtils.container(context, AbstractMachineMenu.class)
                .ifPresent(menu -> {
                    AbstractMachineBlockEntity blockEntity = menu.getBlockEntity();
                    blockEntity.setRedstoneMode(this.mode);
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
