package themcbros.usefulmachinery.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import themcbros.usefulmachinery.tileentity.CreativePowerCellTileEntity;

import javax.annotation.Nullable;

public class CreativePowerCellBlock extends Block {

    public CreativePowerCellBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CreativePowerCellTileEntity();
    }
}
