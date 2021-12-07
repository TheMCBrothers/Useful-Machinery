package themcbros.usefulmachinery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import themcbros.usefulmachinery.init.ModStats;
import themcbros.usefulmachinery.init.ModTileEntities;

public class CoalGeneratorBlock extends AbstractMachineBlock {
    public CoalGeneratorBlock(Properties properties) {
        super(properties, ModStats.INTERACT_WITH_COAL_GENERATOR);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModTileEntities.COAL_GENERATOR.create(pos, state);
    }
}
