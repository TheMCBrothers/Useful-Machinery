package themcbros.usefulmachinery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import themcbros.usefulmachinery.init.ModStats;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CompactorBlock extends MachineBlock {
    public CompactorBlock(Properties properties) {
        super(properties, ModStats.INTERACT_WITH_COMPACTOR);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
