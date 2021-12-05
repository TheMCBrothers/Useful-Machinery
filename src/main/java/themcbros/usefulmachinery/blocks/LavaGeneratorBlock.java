package themcbros.usefulmachinery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import themcbros.usefulmachinery.init.ModStats;

import javax.annotation.Nullable;

public class LavaGeneratorBlock extends AbstractMachineBlock {
    public LavaGeneratorBlock(Properties properties) {
        super(properties, ModStats.INTERACT_WITH_LAVA_GENERATOR);
    }

    //TODO later on
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
