package themcbros.usefulmachinery.networking;

import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
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
                .consumer(SetRedstoneModePacket::handle)
                .add();
        channel.messageBuilder(SetCompactorModePacket.class, 2)
                .decoder(SetCompactorModePacket::fromBytes)
                .encoder(SetCompactorModePacket::toBytes)
                .consumer(SetCompactorModePacket::handle)
                .add();
    }

    private Networking() {}

    public static void init() {}

}
