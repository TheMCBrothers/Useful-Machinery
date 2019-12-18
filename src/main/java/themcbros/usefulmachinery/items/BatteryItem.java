package themcbros.usefulmachinery.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import themcbros.usefulmachinery.caps.CapabilityProviderEnergy;
import themcbros.usefulmachinery.caps.EnergyConversionStorage;
import themcbros.usefulmachinery.energy.IEnergyContainerItem;
import themcbros.usefulmachinery.util.TextUtils;

import javax.annotation.Nullable;
import java.util.List;

public class BatteryItem extends Item implements IEnergyContainerItem {

    public static final String ENERGY = "Energy";
    private final int capacity = 10_000;
    private final int maxReceive = 500;
    private final int maxExtract = 500;

    public BatteryItem(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new CapabilityProviderEnergy<IEnergyStorage>(new EnergyConversionStorage(this, stack), CapabilityEnergy.ENERGY, null);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return this.getEnergyStored(stack) > 0;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0xFF0000;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        double stored = this.getMaxEnergyStored(stack) - this.getEnergyStored(stack) + 1;
        double max = this.getMaxEnergyStored(stack) + 1;
        return stored / max;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        ITextComponent component = TextUtils.energyWithMax(this.getEnergyStored(stack), this.getMaxEnergyStored(stack));
        tooltip.add(component.applyTextStyle(TextFormatting.GRAY));
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            ItemStack itemStack = new ItemStack(this);
            CompoundNBT tag = new CompoundNBT();
            tag.putInt(ENERGY, this.capacity);
            itemStack.setTag(tag);
            items.add(itemStack);
        }
        super.fillItemGroup(group, items);
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        if (!container.hasTag()) {
            container.setTag(new CompoundNBT());
        }
        int stored = Math.min(container.getTag().getInt(ENERGY), getMaxEnergyStored(container));
        int energyReceived = Math.min(capacity - stored, Math.min(this.maxReceive, maxReceive));

        if (!simulate) {
            stored += energyReceived;
            container.getTag().putInt(ENERGY, stored);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if (container.getTag() == null || !container.getTag().contains(ENERGY)) {
            return 0;
        }
        int stored = Math.min(container.getTag().getInt(ENERGY), getMaxEnergyStored(container));
        int energyExtracted = Math.min(stored, Math.min(this.maxExtract, maxExtract));

        if (!simulate) {
            stored -= energyExtracted;
            container.getTag().putInt(ENERGY, stored);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        if (container.getTag() == null || !container.getTag().contains(ENERGY)) {
            return 0;
        }
        return Math.min(container.getTag().getInt(ENERGY), getMaxEnergyStored(container));
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return this.capacity;
    }
}
