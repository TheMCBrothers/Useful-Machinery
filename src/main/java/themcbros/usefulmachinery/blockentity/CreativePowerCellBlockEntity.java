package themcbros.usefulmachinery.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import themcbros.usefulmachinery.init.MachineryBlockEntities;
import themcbros.usefulmachinery.util.EnergyUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CreativePowerCellBlockEntity extends BlockEntity implements IEnergyStorage {
    public CreativePowerCellBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(MachineryBlockEntities.CREATIVE_POWER_CELL, blockPos, blockState);
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
        return 2_000_000_000;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return LazyOptional.of(() -> this).cast();
        }
        return super.getCapability(cap, side);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, CreativePowerCellBlockEntity blockEntity) {
        if (!level.isClientSide) {
            blockEntity.sendEnergy(level, blockPos);
        }
    }

    private void sendEnergy(Level level, BlockPos blockPos) {
        for (Direction facing : Direction.values()) {
            IEnergyStorage energy = EnergyUtils.getEnergy(level, blockPos.relative(facing), facing.getOpposite());

            if (energy != null && energy.canReceive()) {
                energy.receiveEnergy(this.getEnergyStored(), false);
            }
        }
    }
}
