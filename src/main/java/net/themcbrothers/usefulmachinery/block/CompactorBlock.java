package net.themcbrothers.usefulmachinery.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.themcbrothers.usefulmachinery.core.MachineryBlockEntities;
import net.themcbrothers.usefulmachinery.core.MachineryStats;
import org.jetbrains.annotations.Nullable;

public class CompactorBlock extends AbstractMachineBlock {
    private static final MapCodec<CompactorBlock> CODEC = simpleCodec(CompactorBlock::new);

    public CompactorBlock(Properties props) {
        super(props, MachineryStats.INTERACTION_WITH_COMPACTOR);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return MachineryBlockEntities.COMPACTOR.get().create(pos, state);
    }
}
