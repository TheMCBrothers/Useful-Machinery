package net.themcbrothers.usefulmachinery.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
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
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.themcbrothers.lib.wrench.WrenchableBlock;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import net.themcbrothers.usefulmachinery.block.entity.LavaGeneratorBlockEntity;
import net.themcbrothers.usefulmachinery.item.UpgradeItem;
import net.themcbrothers.usefulmachinery.machine.MachineTier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

// TODO: pick item copy state!
public abstract class AbstractMachineBlock extends BaseEntityBlock implements WrenchableBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<MachineTier> TIER = EnumProperty.create("tier", MachineTier.class);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    @Nullable
    private final Supplier<ResourceLocation> interactStat;

    protected AbstractMachineBlock(Properties props, @Nullable Supplier<ResourceLocation> interactStat) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TIER, MachineTier.SIMPLE).setValue(LIT, Boolean.FALSE));
        this.interactStat = interactStat;
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TIER, LIT);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);

        if (level.getBlockEntity(pos) instanceof AbstractMachineBlockEntity blockEntity) {
            MachineTier tier = blockEntity.getMachineTier(state);

            if (tier != MachineTier.SIMPLE) {
                CompoundTag stateTag = stack.getOrCreateTagElement(BlockItem.BLOCK_STATE_TAG);
                stateTag.putString(TIER.getName(), tier.getSerializedName());
            }
        }

        return stack;
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interaction, BlockHitResult hit) {
        if (this.tryWrench(state, level, pos, player, interaction, hit)) {
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        ItemStack stack = player.getItemInHand(interaction);

        if (stack.getItem() instanceof UpgradeItem) {
            InteractionResult interactionResult = stack.useOn(new UseOnContext(player, interaction, hit));

            if (interactionResult != InteractionResult.PASS) {
                return interactionResult;
            }
        }

        if (level.getBlockEntity(pos) instanceof AbstractMachineBlockEntity blockEntity && player instanceof ServerPlayer serverPlayer) {
            if (blockEntity instanceof LavaGeneratorBlockEntity lavaGeneratorBlockEntity) {
                FluidTank lavaTank = lavaGeneratorBlockEntity.getLavaTank();
                IItemHandler itemHandler = player.getCapability(Capabilities.ItemHandler.ENTITY);
                FluidActionResult actionResult = FluidUtil.tryEmptyContainerAndStow(stack, lavaTank, itemHandler, Integer.MAX_VALUE, player, true);

                if (actionResult.isSuccess()) {
                    return InteractionResult.sidedSuccess(level.isClientSide());
                }
            }

            serverPlayer.openMenu(blockEntity, data -> {
                data.writeBlockPos(pos);
                data.writeInt(blockEntity.getUpgradeSlotSize());
                data.writeInt(blockEntity.getContainerData().getCount());
            });

            if (this.interactStat != null) {
                player.awardStat(this.interactStat.get());
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
