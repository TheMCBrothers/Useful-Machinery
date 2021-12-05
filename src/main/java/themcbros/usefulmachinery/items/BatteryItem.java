package themcbros.usefulmachinery.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import themcbros.usefulmachinery.caps.CapabilityProviderEnergy;
import themcbros.usefulmachinery.caps.EnergyConversionStorage;
import themcbros.usefulmachinery.energy.IEnergyContainerItem;
import themcbros.usefulmachinery.util.TextUtils;

import javax.annotation.Nullable;
import java.util.List;

public class BatteryItem extends Item implements IEnergyContainerItem {
    public static final String ENERGY = "Energy";
    private final int capacity = 10_000;

    public BatteryItem(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new CapabilityProviderEnergy<>(new EnergyConversionStorage(this, stack), CapabilityEnergy.ENERGY, null);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return this.getEnergyStored(stack) > 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xFF0000;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        double stored = this.getMaxEnergyStored(stack) - this.getEnergyStored(stack) + 1;
        double max = this.getMaxEnergyStored(stack) + 1;
        return Math.round(13.0F - (float) stored * 13.0F / (float) max);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        MutableComponent component = (MutableComponent) TextUtils.energyWithMax(this.getEnergyStored(stack), this.getMaxEnergyStored(stack));
        tooltip.add(component.withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            ItemStack itemStack = new ItemStack(this);
            CompoundTag tag = new CompoundTag();

            tag.putInt(ENERGY, this.capacity);
            itemStack.setTag(tag);
            items.add(itemStack);
        }
        super.fillItemCategory(group, items);
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        if (!container.hasTag()) {
            container.setTag(new CompoundTag());
        }
        int stored = Math.min(container.getTag().getInt(ENERGY), getMaxEnergyStored(container));
        int maxReceive1 = 500;
        int energyReceived = Math.min(capacity - stored, Math.min(maxReceive1, maxReceive));

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
        int maxExtract1 = 500;
        int energyExtracted = Math.min(stored, Math.min(maxExtract1, maxExtract));

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
