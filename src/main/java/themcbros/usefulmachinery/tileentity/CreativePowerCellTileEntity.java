package themcbros.usefulmachinery.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import themcbros.usefulmachinery.init.ModTileEntities;
import themcbros.usefulmachinery.util.EnergyUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CreativePowerCellTileEntity extends TileEntity implements IEnergyStorage, ITickableTileEntity {

    public CreativePowerCellTileEntity() {
        super(ModTileEntities.CREATIVE_POWER_CELL);
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
        if (cap == CapabilityEnergy.ENERGY){
            return LazyOptional.of(() -> this).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void tick() {
        assert world != null;
        if (!world.isRemote) {
            this.sendEnergy();
        }
    }

    private void sendEnergy() {
        for (Direction facing : Direction.values()) {
            assert this.world != null;
            IEnergyStorage energy = EnergyUtils.getEnergy(this.world, this.pos.offset(facing), facing.getOpposite());
            if (energy != null && energy.canReceive()) {
                energy.receiveEnergy(this.getEnergyStored(), false);
            }
        }
    }
}
