package themcbros.usefulmachinery.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import themcbros.usefulmachinery.init.ModStats;
import themcbros.usefulmachinery.tileentity.ElectricSmelterTileEntity;

import javax.annotation.Nullable;

public class ElectricSmelterBlock extends MachineBlock {

    public ElectricSmelterBlock(Properties properties) {
        super(properties, ModStats.INTERACT_WITH_ELECTRIC_SMELTER);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ElectricSmelterTileEntity();
    }

}
