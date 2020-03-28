package themcbros.usefulmachinery.bundled_cable;

import com.google.common.collect.Lists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import themcbros.usefulmachinery.UsefulMachinery;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = UsefulMachinery.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FramedBundledCableNetworkManager {

    public static final Collection<LazyOptional<FramedBundledCableNetwork>> NETWORK_LIST = Collections.synchronizedList(Lists.newArrayList());

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static FramedBundledCableNetwork get(IWorldReader world, BlockPos pos) {
        return getLazy(world, pos).orElse(null);
    }

    public static LazyOptional<FramedBundledCableNetwork> getLazy(IWorldReader world, BlockPos pos) {
        synchronized (NETWORK_LIST) {
            for (LazyOptional<FramedBundledCableNetwork> network : NETWORK_LIST) {
                if (network.isPresent()) {
                    FramedBundledCableNetwork net = network.orElseThrow(IllegalStateException::new);
                    if (net.contains(world, pos)) {
                        return network;
                    }
                }
            }
        }

        // Create new network
        FramedBundledCableNetwork network = FramedBundledCableNetwork.buildNetwork(world, pos);
        LazyOptional<FramedBundledCableNetwork> lazy = LazyOptional.of(() -> network);
        NETWORK_LIST.add(lazy);
        UsefulMachinery.LOGGER.debug("Created network {}", network);
        return lazy;
    }

    public static void invalidateNetwork(IWorldReader world, BlockPos pos) {
        Collection<LazyOptional<FramedBundledCableNetwork>> toRemove = NETWORK_LIST.stream()
                .filter(n -> n != null && n.isPresent() && n.orElseThrow(IllegalStateException::new).contains(world, pos))
                .collect(Collectors.toList());
        toRemove.forEach(FramedBundledCableNetworkManager::invalidateNetwork);
    }

    private static void invalidateNetwork(LazyOptional<FramedBundledCableNetwork> network) {
        UsefulMachinery.LOGGER.debug("Invalidate network {}", network);
        NETWORK_LIST.removeIf(n -> n.isPresent() && n.equals(network));
        network.ifPresent(FramedBundledCableNetwork::invalidate);
        network.invalidate();
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        NETWORK_LIST.stream()
                .filter(n -> n != null && n.isPresent())
                .forEach(n -> n.ifPresent(FramedBundledCableNetwork::sendEnergy));
    }

}
