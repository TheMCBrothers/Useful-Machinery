package net.themcbrothers.usefulmachinery.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.themcbrothers.usefulmachinery.block.entity.CreativePowerCellBlockEntity;
import net.themcbrothers.usefulmachinery.core.MachineryBlockEntities;
import org.jetbrains.annotations.Nullable;

public class CreativePowerCellBlock extends BaseEntityBlock {
    public static final MapCodec<CreativePowerCellBlock> CODEC = simpleCodec(CreativePowerCellBlock::new);

    public CreativePowerCellBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return MachineryBlockEntities.CREATIVE_POWER_CELL.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, MachineryBlockEntities.CREATIVE_POWER_CELL.get(), CreativePowerCellBlockEntity::serverTick);
    }
}
