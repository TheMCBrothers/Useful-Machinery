package themcbros.usefulmachinery.energy;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * Reference implementation of {@link IEnergyContainerItem}. Use/extend this or implement your own.
 * 
 * @author TheMCLoveMan
 */
public class EnergyContainerItem extends Item implements IEnergyContainerItem {

	public static final String ENERGY = "Energy";
	
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;
	
	public EnergyContainerItem(Properties builder) {
		super(builder);
	}
	
	public EnergyContainerItem(int capacity, Properties builder) {
		this(capacity, capacity, builder);
	}
	
	public EnergyContainerItem(int capacity, int maxTransfer, Properties builder) {
		this(capacity, maxTransfer, maxTransfer, builder);
	}
	
	public EnergyContainerItem(int capacity, int maxReceive, int maxExtract, Properties builder) {
		super(builder);
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}
	
	public EnergyContainerItem setCapacity(int capacity) {
		this.capacity = capacity;
		return this;
	}
	
	public EnergyContainerItem setMaxTransfer(int maxTransfer) {
		setMaxExtract(maxTransfer);
		setMaxReceive(maxTransfer);
		return this;
	}
	
	public EnergyContainerItem setMaxExtract(int maxExtract) {
		this.maxExtract = maxExtract;
		return this;
	}
	
	public EnergyContainerItem setMaxReceive(int maxReceive) {
		this.maxReceive = maxReceive;
		return this;
	}
	
	/* IEnergyContainerItem */
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
		return capacity;
	}

}
