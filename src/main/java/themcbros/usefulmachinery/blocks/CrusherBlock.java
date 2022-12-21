package themcbros.usefulmachinery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import themcbros.usefulmachinery.init.MachineryStats;
import themcbros.usefulmachinery.init.MachineryBlockEntities;

public class CrusherBlock extends AbstractMachineBlock {
    public CrusherBlock(Properties properties) {
        super(properties, MachineryStats.INTERACT_WITH_CRUSHER);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return MachineryBlockEntities.CRUSHER.get().create(pos, state);
    }
}
