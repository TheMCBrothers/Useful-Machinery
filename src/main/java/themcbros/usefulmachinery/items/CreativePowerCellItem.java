package themcbros.usefulmachinery.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import themcbros.usefulmachinery.caps.CapabilityProviderEnergy;
import themcbros.usefulmachinery.caps.EnergyConversionStorage;
import themcbros.usefulmachinery.energy.IEnergyContainerItem;

import javax.annotation.Nullable;

public class CreativePowerCellItem extends BlockItem implements IEnergyContainerItem {

    public CreativePowerCellItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new CapabilityProviderEnergy<>(new EnergyConversionStorage(this, stack), CapabilityEnergy.ENERGY, null);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0xFF0000;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 0.0d;
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        return maxReceive;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        return maxExtract;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        return this.getMaxEnergyStored(container);
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return 2_000_000_000;
    }
}
