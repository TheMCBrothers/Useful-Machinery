package themcbros.usefulmachinery.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import themcbros.usefulmachinery.init.ModTileEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CreativePowerCellTileEntity extends TileEntity implements IEnergyStorage {

    public CreativePowerCellTileEntity() {
        super(ModTileEntities.CREATIVE_POWER_CELL);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 200_000;
    }

    @Override
    public int getEnergyStored() {
        return 2_000_000_000;
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
        if (cap == CapabilityEnergy.ENERGY){
            return LazyOptional.of(() -> this).cast();
        }
        return super.getCapability(cap, side);
    }
}
