package net.themcbrothers.usefulmachinery.client.tintsources;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;

public record TierBlockTintSource() implements BlockTintSource {
    public static final TierBlockTintSource INSTANCE = new TierBlockTintSource();

    @Override
    public int color(BlockState state) {
        return -1;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof AbstractMachineBlockEntity machineBlockEntity) {
            return machineBlockEntity.getMachineTier().getColor();
        }

        return -1;
    }
}
