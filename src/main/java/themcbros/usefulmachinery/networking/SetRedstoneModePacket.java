package themcbros.usefulmachinery.networking;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import themcbros.usefulmachinery.container.MachineContainer;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.tileentity.MachineTileEntity;

import java.util.function.Supplier;

public class SetRedstoneModePacket {

    private RedstoneMode mode;

    public SetRedstoneModePacket() {
    }

    public SetRedstoneModePacket(RedstoneMode mode) {
        this.mode = mode;
    }

    public static SetRedstoneModePacket fromBytes(PacketBuffer buffer) {
        SetRedstoneModePacket packet = new SetRedstoneModePacket();
        packet.mode = RedstoneMode.byIndex(buffer.readByte());
        return packet;
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeByte(this.mode.ordinal());
    }

    public static void handle(SetRedstoneModePacket packet, Supplier<NetworkEvent.Context> context) {
        ServerPlayerEntity player = context.get().getSender();
        context.get().enqueueWork(() -> handlePacket(packet, player));
        context.get().setPacketHandled(true);
    }

    private static void handlePacket(SetRedstoneModePacket packet, ServerPlayerEntity player) {
        if (player != null) {
            if (player.openContainer instanceof MachineContainer) {
                MachineTileEntity tileEntity = ((MachineContainer) player.openContainer).machineTileEntity;
                if (tileEntity != null) {
                    tileEntity.redstoneMode = packet.mode;
                }
            }
        }
    }

}
