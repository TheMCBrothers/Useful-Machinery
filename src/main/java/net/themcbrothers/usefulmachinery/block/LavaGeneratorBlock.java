package net.themcbrothers.usefulmachinery.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.themcbrothers.usefulmachinery.core.MachineryBlockEntities;
import net.themcbrothers.usefulmachinery.core.MachineryStats;
import org.jetbrains.annotations.Nullable;

public class LavaGeneratorBlock extends AbstractMachineBlock {
    public static final MapCodec<LavaGeneratorBlock> CODEC = simpleCodec(LavaGeneratorBlock::new);

    public LavaGeneratorBlock(Properties props) {
        super(props, MachineryStats.INTERACTION_WITH_LAVA_GENERATOR);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return MachineryBlockEntities.LAVA_GENERATOR.get().create(pos, state);
    }
}
