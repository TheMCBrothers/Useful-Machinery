package themcbros.usefulmachinery.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import themcbros.usefulmachinery.menu.MachineMenu;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;

import java.util.function.Supplier;

public class SetRedstoneModePacket {

    private RedstoneMode mode;

    public SetRedstoneModePacket() {
    }

    public SetRedstoneModePacket(RedstoneMode mode) {
        this.mode = mode;
    }

    public static SetRedstoneModePacket fromBytes(FriendlyByteBuf buffer) {
        SetRedstoneModePacket packet = new SetRedstoneModePacket();
        packet.mode = RedstoneMode.byIndex(buffer.readByte());
        return packet;
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeByte(this.mode.ordinal());
    }

    public static void handle(SetRedstoneModePacket packet, Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        context.get().enqueueWork(() -> handlePacket(packet, player));
        context.get().setPacketHandled(true);
    }

    private static void handlePacket(SetRedstoneModePacket packet, ServerPlayer player) {
        if (player != null) {
            if (player.containerMenu instanceof MachineMenu) {
                AbstractMachineBlockEntity tileEntity = ((MachineMenu) player.containerMenu).abstractMachineBlockEntity;
                if (tileEntity != null) {
                    tileEntity.redstoneMode = packet.mode;
                }
            }
        }
    }
}
