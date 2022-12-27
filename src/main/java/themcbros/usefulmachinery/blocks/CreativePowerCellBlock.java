package themcbros.usefulmachinery.blocks;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import themcbros.usefulmachinery.blockentity.CreativePowerCellBlockEntity;
import themcbros.usefulmachinery.init.MachineryBlockEntities;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class CreativePowerCellBlock extends BaseEntityBlock {
    public CreativePowerCellBlock(Properties properties) {
        super(properties);
    }

    @Override
    @MethodsReturnNonnullByDefault
    @Nonnull
    public RenderShape getRenderShape(@Nonnull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return MachineryBlockEntities.CREATIVE_POWER_CELL.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, MachineryBlockEntities.CREATIVE_POWER_CELL.get(), CreativePowerCellBlockEntity::serverTick);
    }
}
