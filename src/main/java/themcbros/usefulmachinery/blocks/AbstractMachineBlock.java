package themcbros.usefulmachinery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.themcbrothers.lib.wrench.WrenchableBlock;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;
import themcbros.usefulmachinery.items.UpgradeItem;
import themcbros.usefulmachinery.machine.MachineTier;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

public abstract class AbstractMachineBlock extends BaseEntityBlock implements WrenchableBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<MachineTier> TIER = EnumProperty.create("tier", MachineTier.class);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private final Supplier<ResourceLocation> interactStat;

    public AbstractMachineBlock(Properties properties, @Nullable Supplier<ResourceLocation> interactStat) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TIER, MachineTier.SIMPLE).setValue(LIT, Boolean.FALSE));
        this.interactStat = interactStat;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, type, ((l, p, s, be) -> ((AbstractMachineBlockEntity) be).tick()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TIER, LIT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @ParametersAreNonnullByDefault
    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (this.tryWrench(state, worldIn, pos, player, handIn, hit)) {
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }

        if (player.getItemInHand(handIn).getItem() instanceof UpgradeItem) {
            return player.getItemInHand(handIn).useOn(new UseOnContext(player, handIn, hit));
        }

        if (worldIn.getBlockEntity(pos) instanceof AbstractMachineBlockEntity blockEntity && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, blockEntity, data -> {
                data.writeBlockPos(pos);
                data.writeInt(blockEntity.getUpgradeSlotSize());
                data.writeInt(blockEntity.getContainerData().getCount());
            });
            if (interactStat != null) {
                player.awardStat(interactStat.get());
            }
        }

        return InteractionResult.SUCCESS;
    }
}
