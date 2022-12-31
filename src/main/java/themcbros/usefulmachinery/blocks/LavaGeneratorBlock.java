package themcbros.usefulmachinery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidUtil;
import org.jetbrains.annotations.NotNull;
import themcbros.usefulmachinery.init.MachineryBlockEntities;
import themcbros.usefulmachinery.init.MachineryStats;

public class LavaGeneratorBlock extends AbstractMachineBlock {
    public LavaGeneratorBlock(Properties properties) {
        super(properties, MachineryStats.INTERACT_WITH_LAVA_GENERATOR);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return MachineryBlockEntities.LAVA_GENERATOR.get().create(pos, state);
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, Player player, @NotNull InteractionHand handIn, @NotNull BlockHitResult hit) {
        if (!FluidUtil.interactWithFluidHandler(player, handIn, worldIn, pos, hit.getDirection())) {
            return super.use(state, worldIn, pos, player, handIn, hit);
        }

        return InteractionResult.SUCCESS;
    }
}
