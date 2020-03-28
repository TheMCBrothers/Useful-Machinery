package themcbros.usefulmachinery.bundled_cable;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.util.LazyOptional;
import themcbros.usefulmachinery.blocks.FramedBundledCableBlock;
import themcbros.usefulmachinery.tileentity.FramedBundledCableTileEntity;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class FramedBundledCableNetwork {

    private final IWorldReader world;
    private final Map<BlockPos, Set<Connection>> connections = Maps.newHashMap();
    private boolean connectionsBuilt;
    private final Map<DyeColor, Boolean> signals;

    public FramedBundledCableNetwork(IWorldReader world, Set<BlockPos> cables, Map<DyeColor, Boolean> signals) {
        this.world = world;
        cables.forEach(pos -> connections.put(pos, Collections.emptySet()));
        this.signals = signals;
    }

    public boolean contains(IWorldReader world, BlockPos pos) {
        return this.world == world && this.connections.containsKey(pos);
    }

    public int getCableCount() {
        return this.connections.size();
    }

    public Connection getConnection(BlockPos pos, Direction side) {
        if (connections.containsKey(pos)) {
            for (Connection connection : connections.get(pos)) {
                if (connection.side == side) {
                    return connection;
                }
            }
        }
        return new Connection(this, side);
    }

    private void updateCableRedstone() {
        connections.keySet().forEach(p -> {
            TileEntity tileEntity = world.getTileEntity(p);
            if (tileEntity instanceof FramedBundledCableTileEntity) {
                for (DyeColor color : DyeColor.values()) {
                    ((FramedBundledCableTileEntity) tileEntity).signals.put(color, this.signals.get(color));
                }
            }
        });
    }

    void invalidate() {
        connections.values().forEach(set -> set.forEach(con -> con.getLazyOptional().invalidate()));
    }

    public Map<DyeColor, Boolean> getSignals() {
        return this.signals;
    }

    static FramedBundledCableNetwork buildNetwork(IWorldReader world, BlockPos pos) {
        Set<BlockPos> cables = buildCableSet(world, pos);
//        int energyStored = cables.stream().mapToInt(p -> {
//            TileEntity tileEntity = world.getTileEntity(p);
//            return tileEntity instanceof FramedBundledCableTileEntity ? ((FramedBundledCableTileEntity) tileEntity). : 0;
//        }).sum();
        Map<DyeColor, Boolean> signals = Maps.newHashMap();
        cables.forEach(p -> {
            TileEntity tileEntity = world.getTileEntity(p);
            if (tileEntity instanceof FramedBundledCableTileEntity) {
                for (DyeColor color : DyeColor.values()) {
                    Boolean val = ((FramedBundledCableTileEntity) tileEntity).signals.get(color);
                    if (val) signals.put(color, Boolean.TRUE);
                }
            }
        });
        return new FramedBundledCableNetwork(world, cables, signals);
    }

    private static Set<BlockPos> buildCableSet(IWorldReader world, BlockPos pos) {
        return buildCableSet(world, pos, Sets.newHashSet());
    }

    private static Set<BlockPos> buildCableSet(IWorldReader world, BlockPos pos, Set<BlockPos> set) {
        set.add(pos);
        for (Direction side : Direction.values()) {
            BlockPos pos1 = pos.offset(side);
            Boolean bool = FramedBundledCableBlock.getConnection(world.getBlockState(pos), side);
            if (!set.contains(pos1) && world.getTileEntity(pos1) instanceof FramedBundledCableTileEntity && bool) {
                set.add(pos1);
                set.addAll(buildCableSet(world, pos1, set));
            }
        }
        return set;
    }

    private void buildConnections() {
        // Determine all connections. This will be done once the connections are actually needed.
        if (!connectionsBuilt) {
            connections.keySet().forEach(p -> connections.put(p, getConnections(world, p)));
            connectionsBuilt = true;
        }
    }

    private Set<Connection> getConnections(IBlockReader world, BlockPos pos) {
        // Get all connections for the wire at pos
        Set<Connection> connections = Sets.newHashSet();
        for (Direction direction : Direction.values()) {
            TileEntity te = world.getTileEntity(pos.offset(direction));
            BlockState state = world.getBlockState(pos.offset(direction));
            if (te != null && !(te instanceof FramedBundledCableTileEntity) && state.canProvidePower()) {
                connections.add(new Connection(this, direction));
            }
        }
        return connections;
    }

    @Override
    public String toString() {
        return String.format("FramedBundledCableNetwork %s, %d cables", Integer.toHexString(hashCode()), getCableCount());
    }

    public void sendEnergy() {
        buildConnections();
        updateCableRedstone();
    }

    public static class Connection {
        private final FramedBundledCableNetwork network;
        private final Direction side;
        private final LazyOptional<Connection> lazyOptional;

        Connection(FramedBundledCableNetwork network, Direction side) {
            this.network = network;
            this.side = side;
            this.lazyOptional = LazyOptional.of(() -> this);
        }

        public LazyOptional<Connection> getLazyOptional() {
            return lazyOptional;
        }
    }

}
