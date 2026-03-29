package net.themcbrothers.usefulmachinery.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.themcbrothers.lib.wrench.WrenchableBlock;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import net.themcbrothers.usefulmachinery.block.entity.LavaGeneratorBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class AbstractMachineBlock extends BaseEntityBlock implements WrenchableBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    @Nullable
    private final Supplier<Identifier> interactStat;

    protected AbstractMachineBlock(Properties props, @Nullable Supplier<Identifier> interactStat) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.FALSE));
        this.interactStat = interactStat;
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, type, ((l, p, s, be) -> ((AbstractMachineBlockEntity) be).tick()));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (this.tryWrench(state, level, pos, player, hand, hit)) {
            return InteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) instanceof AbstractMachineBlockEntity blockEntity && player instanceof ServerPlayer) {
            if (blockEntity instanceof LavaGeneratorBlockEntity lavaGeneratorBlockEntity) {
                FluidTank lavaTank = lavaGeneratorBlockEntity.getLavaTank();
                IItemHandler itemHandler = IItemHandler.of(Objects.requireNonNull(player.getCapability(Capabilities.Item.ENTITY)));
                FluidActionResult actionResult = FluidUtil.tryEmptyContainerAndStow(stack, lavaTank, itemHandler, Integer.MAX_VALUE, player, true);

                if (actionResult.isSuccess()) {
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof AbstractMachineBlockEntity blockEntity && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(blockEntity, data -> {
                data.writeBlockPos(pos);
                data.writeInt(blockEntity.getUpgradeSlotSize());
                data.writeInt(blockEntity.getContainerData().getCount());
            });

            if (this.interactStat != null) {
                player.awardStat(this.interactStat.get());
            }
        }

        return InteractionResult.SUCCESS;
    }
}
