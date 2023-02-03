package themcbros.usefulmachinery.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import themcbros.usefulmachinery.menu.MachineMenu;
import themcbros.usefulmachinery.machine.CompactorMode;
import themcbros.usefulmachinery.blockentity.CompactorBlockEntity;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;

import java.util.function.Supplier;

public class SetCompactorModePacket {
    private CompactorMode mode;

    public SetCompactorModePacket() {
    }

    public SetCompactorModePacket(CompactorMode mode) {
        this.mode = mode;
    }

    public static SetCompactorModePacket fromBytes(FriendlyByteBuf buffer) {
        SetCompactorModePacket packet = new SetCompactorModePacket();
        packet.mode = CompactorMode.byIndex(buffer.readByte());
        return packet;
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeByte(this.mode.ordinal());
    }

    public static void handle(SetCompactorModePacket packet, Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        context.get().enqueueWork(() -> handlePacket(packet, player));
        context.get().setPacketHandled(true);
    }

    private static void handlePacket(SetCompactorModePacket packet, ServerPlayer player) {
        if (player != null) {
            if (player.containerMenu instanceof MachineMenu) {
                AbstractMachineBlockEntity tileEntity = ((MachineMenu) player.containerMenu).getBlockEntity();
                if (tileEntity instanceof CompactorBlockEntity) {
                    ((CompactorBlockEntity) tileEntity).compactorMode = packet.mode;
                }
            }
        }
    }
}
