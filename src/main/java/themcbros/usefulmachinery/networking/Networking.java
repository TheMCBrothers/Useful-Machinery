package themcbros.usefulmachinery.networking;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import themcbros.usefulmachinery.UsefulMachinery;

import java.util.Objects;

public class Networking {
    public static SimpleChannel channel;

    static {
        channel = NetworkRegistry.ChannelBuilder.named(UsefulMachinery.getId("network"))
                .clientAcceptedVersions(s -> Objects.equals(s, "1"))
                .serverAcceptedVersions(s -> Objects.equals(s, "1"))
                .networkProtocolVersion(() -> "1")
                .simpleChannel();

        channel.messageBuilder(SetRedstoneModePacket.class, 1)
                .decoder(SetRedstoneModePacket::fromBytes)
                .encoder(SetRedstoneModePacket::toBytes)
                .consumerMainThread(SetRedstoneModePacket::handle)
                .add();
        channel.messageBuilder(SetCompactorModePacket.class, 2)
                .decoder(SetCompactorModePacket::fromBytes)
                .encoder(SetCompactorModePacket::toBytes)
                .consumerMainThread(SetCompactorModePacket::handle)
                .add();
    }

    private Networking() {
    }

    public static void init() {
    }

}
