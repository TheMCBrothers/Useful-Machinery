package themcbros.usefulmachinery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import themcbros.usefulmachinery.init.ModStats;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class ElectricSmelterBlock extends MachineBlock {

    public ElectricSmelterBlock(Properties properties) {
        super(properties, ModStats.INTERACT_WITH_ELECTRIC_SMELTER);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
