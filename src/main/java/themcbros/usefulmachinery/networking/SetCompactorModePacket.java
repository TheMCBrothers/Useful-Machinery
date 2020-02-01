package themcbros.usefulmachinery.networking;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import themcbros.usefulmachinery.container.MachineContainer;
import themcbros.usefulmachinery.machine.CompactorMode;
import themcbros.usefulmachinery.tileentity.CompactorTileEntity;
import themcbros.usefulmachinery.tileentity.MachineTileEntity;

import java.util.function.Supplier;

public class SetCompactorModePacket {

    private CompactorMode mode;

    public SetCompactorModePacket() {
    }

    public SetCompactorModePacket(CompactorMode mode) {
        this.mode = mode;
    }

    public static SetCompactorModePacket fromBytes(PacketBuffer buffer) {
        SetCompactorModePacket packet = new SetCompactorModePacket();
        packet.mode = CompactorMode.byIndex(buffer.readByte());
        return packet;
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeByte(this.mode.ordinal());
    }

    public static void handle(SetCompactorModePacket packet, Supplier<NetworkEvent.Context> context) {
        ServerPlayerEntity player = context.get().getSender();
        context.get().enqueueWork(() -> handlePacket(packet, player));
        context.get().setPacketHandled(true);
    }

    private static void handlePacket(SetCompactorModePacket packet, ServerPlayerEntity player) {
        if (player != null) {
            if (player.openContainer instanceof MachineContainer) {
                MachineTileEntity tileEntity = ((MachineContainer) player.openContainer).machineTileEntity;
                if (tileEntity instanceof CompactorTileEntity) {
                    ((CompactorTileEntity) tileEntity).compactorMode = packet.mode;
                }
            }
        }
    }

}
