package net.themcbrothers.usefulmachinery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.themcbrothers.lib.util.EnergyUtils;
import net.themcbrothers.usefulmachinery.core.MachineryBlockEntities;

public class CreativePowerCellBlockEntity extends BlockEntity implements IEnergyStorage {
    public CreativePowerCellBlockEntity(BlockPos pos, BlockState state) {
        super(MachineryBlockEntities.CREATIVE_POWER_CELL.get(), pos, state);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return maxReceive;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return maxExtract;
    }

    @Override
    public int getEnergyStored() {
        return getMaxEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CreativePowerCellBlockEntity blockEntity) {
        if (!level.isClientSide) {
            blockEntity.sendEnergy(level, pos);
        }
    }

    private void sendEnergy(Level level, BlockPos pos) {
        for (Direction facing : Direction.values()) {
            EnergyUtils.getEnergy(level, pos.relative(facing), facing.getOpposite())
                    .ifPresent((energy) -> {
                        if (energy.canReceive()) {
                            energy.receiveEnergy(CreativePowerCellBlockEntity.this.getEnergyStored(), false);
                        }
                    });
        }
    }
}
