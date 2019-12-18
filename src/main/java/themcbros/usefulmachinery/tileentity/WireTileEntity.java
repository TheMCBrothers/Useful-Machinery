package themcbros.usefulmachinery.tileentity;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import themcbros.usefulmachinery.init.ModTileEntities;

import java.util.Map;

public class WireTileEntity extends TileEntity {

    private static final VoxelShape CENTER_SHAPE = Block.makeCuboidShape(5, 5, 5, 11, 11, 11);
    private static final VoxelShape NORTH_SHAPE = Block.makeCuboidShape(5, 5, 0, 11, 11, 5);
    private static final VoxelShape SOUTH_SHAPE = Block.makeCuboidShape(5, 5, 11, 11, 11, 16);
    private static final VoxelShape EAST_SHAPE = Block.makeCuboidShape(11, 5, 5, 16, 11, 11);
    private static final VoxelShape WEST_SHAPE = Block.makeCuboidShape(0, 5, 5, 5, 11, 11);
    private static final VoxelShape UP_SHAPE = Block.makeCuboidShape(5, 11, 5, 11, 16, 11);
    private static final VoxelShape DOWN_SHAPE = Block.makeCuboidShape(5, 0, 5, 11, 5, 11);

    private static final Map<Direction, Boolean> SIDE_STATES = Maps.newHashMap();

    public WireTileEntity() {
        super(ModTileEntities.WIRE);
    }

    public VoxelShape getShape() {
        VoxelShape shape = CENTER_SHAPE;
        if (SIDE_STATES.getOrDefault(Direction.NORTH, false)) shape = VoxelShapes.combine(shape, NORTH_SHAPE, IBooleanFunction.OR);
        if (SIDE_STATES.getOrDefault(Direction.SOUTH, false)) shape = VoxelShapes.combine(shape, SOUTH_SHAPE, IBooleanFunction.OR);
        if (SIDE_STATES.getOrDefault(Direction.EAST, false)) shape = VoxelShapes.combine(shape, EAST_SHAPE, IBooleanFunction.OR);
        if (SIDE_STATES.getOrDefault(Direction.WEST, false)) shape = VoxelShapes.combine(shape, WEST_SHAPE, IBooleanFunction.OR);
        if (SIDE_STATES.getOrDefault(Direction.UP, false)) shape = VoxelShapes.combine(shape, UP_SHAPE, IBooleanFunction.OR);
        if (SIDE_STATES.getOrDefault(Direction.DOWN, false)) shape = VoxelShapes.combine(shape, DOWN_SHAPE, IBooleanFunction.OR);
        return shape;
    }

}
