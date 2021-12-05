package themcbros.usefulmachinery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import themcbros.usefulmachinery.init.ModStats;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CrusherBlock extends MachineBlock {
    public CrusherBlock(Properties properties) {
        super(properties, ModStats.INTERACT_WITH_CRUSHER);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
