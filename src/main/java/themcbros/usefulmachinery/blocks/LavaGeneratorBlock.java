package themcbros.usefulmachinery.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import themcbros.usefulmachinery.init.ModStats;
import themcbros.usefulmachinery.tileentity.LavaGeneratorTileEntity;

import javax.annotation.Nullable;

public class LavaGeneratorBlock extends MachineBlock {

    public LavaGeneratorBlock(Properties properties) {
        super(properties, ModStats.INTERACT_WITH_LAVA_GENERATOR);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new LavaGeneratorTileEntity();
    }
}
