package themcbros.usefulmachinery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import themcbros.usefulmachinery.init.ModStats;
import themcbros.usefulmachinery.init.ModTileEntities;

public class CompactorBlock extends AbstractMachineBlock {
    public CompactorBlock(Properties properties) {
        super(properties, ModStats.INTERACT_WITH_COMPACTOR);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModTileEntities.COMPACTOR.create(pos, state);
    }
}
