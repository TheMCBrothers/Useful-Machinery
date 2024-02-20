package net.themcbrothers.usefulmachinery.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.themcbrothers.usefulmachinery.core.MachineryBlockEntities;
import net.themcbrothers.usefulmachinery.core.MachineryStats;
import org.jetbrains.annotations.Nullable;

public class ElectricSmelterBlock extends AbstractMachineBlock {
    private static final MapCodec<ElectricSmelterBlock> CODEC = simpleCodec(ElectricSmelterBlock::new);

    public ElectricSmelterBlock(Properties props) {
        super(props, MachineryStats.INTERACTION_WITH_ELECTRIC_SMELTER);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return MachineryBlockEntities.ELECTRIC_SMELTER.get().create(pos, state);
    }
}
