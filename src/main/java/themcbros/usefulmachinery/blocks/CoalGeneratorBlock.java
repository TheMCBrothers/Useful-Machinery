package themcbros.usefulmachinery.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import themcbros.usefulmachinery.init.ModStats;
import themcbros.usefulmachinery.tileentity.CoalGeneratorTileEntity;

import javax.annotation.Nullable;

public class CoalGeneratorBlock extends MachineBlock {

    public CoalGeneratorBlock(Properties properties) {
        super(properties, ModStats.INTERACT_WITH_COAL_GENERATOR);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CoalGeneratorTileEntity();
    }
}
