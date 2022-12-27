package themcbros.usefulmachinery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import themcbros.usefulmachinery.init.MachineryStats;
import themcbros.usefulmachinery.init.MachineryBlockEntities;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class CompactorBlock extends AbstractMachineBlock {
    public CompactorBlock(Properties properties) {
        super(properties, MachineryStats.INTERACT_WITH_COMPACTOR);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return MachineryBlockEntities.COMPACTOR.get().create(pos, state);
    }
}
