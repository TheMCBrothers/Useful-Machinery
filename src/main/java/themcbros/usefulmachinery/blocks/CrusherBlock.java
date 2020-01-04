package themcbros.usefulmachinery.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import themcbros.usefulmachinery.init.ModStats;
import themcbros.usefulmachinery.tileentity.CrusherTileEntity;

import javax.annotation.Nullable;

public class CrusherBlock extends MachineBlock {

    public CrusherBlock(Properties properties) {
        super(properties, ModStats.INTERACT_WITH_CRUSHER);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CrusherTileEntity();
    }

}
