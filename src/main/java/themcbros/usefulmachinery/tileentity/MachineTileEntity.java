package themcbros.usefulmachinery.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import themcbros.usefulmachinery.blocks.MachineBlock;
import themcbros.usefulmachinery.energy.MachineEnergyStorage;
import themcbros.usefulmachinery.machine.RedstoneMode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class MachineTileEntity extends TileEntity implements ITickableTileEntity, ISidedInventory, INamedContainerProvider {

    protected static final int ENERGY_CAPACITY = 20_000;
    protected static final int MAX_TRANSFER = 100;

    protected final NonNullList<ItemStack> stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

    public MachineEnergyStorage energyStorage;
    public RedstoneMode redstoneMode = RedstoneMode.IGNORED;
    private boolean isGenerator;

    MachineTileEntity(TileEntityType<?> tileEntityTypeIn, boolean isGenerator) {
        super(tileEntityTypeIn);
        this.isGenerator = isGenerator;
        this.energyStorage = new MachineEnergyStorage(ENERGY_CAPACITY, !isGenerator ? MAX_TRANSFER : 0, isGenerator ? MAX_TRANSFER : 0);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("RedstoneMode", redstoneMode.ordinal());
        compound.putInt("EnergyStored", this.energyStorage.getEnergyStored());
        ItemStackHelper.saveAllItems(compound, this.stacks, false);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        this.redstoneMode = RedstoneMode.byIndex(compound.getInt("RedstoneMode"));
        this.energyStorage = new MachineEnergyStorage(ENERGY_CAPACITY, !isGenerator ? MAX_TRANSFER : 0, isGenerator ? MAX_TRANSFER : 0, compound.getInt("EnergyStored"));
        ItemStackHelper.loadAllItems(compound, this.stacks);
        super.read(compound);
    }

    abstract int[] getInputSlots();

    abstract int[] getOutputSlots();

    @Override
    public int[] getSlotsForFace(Direction side) {
        // TODO: Implement side config
        return side == Direction.DOWN ? this.getOutputSlots() : this.getInputSlots();
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        // TODO: Implement side config
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        // TODO: Implement side config
        return true;
    }

    @Override
    public abstract int getSizeInventory();

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.stacks) {
            if (!stack.isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.stacks.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.stacks, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.stacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        // TODO
        this.stacks.set(index, stack);
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
        assert this.world != null;
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
                    (double) this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void clear() {
        this.stacks.clear();
    }

    public void sendUpdate(boolean lit) {
        assert this.world != null;
        this.world.setBlockState(this.pos, this.getBlockState().with(MachineBlock.LIT, lit));
    }

    private LazyOptional<IItemHandlerModifiable>[] itemHandlers = SidedInvWrapper.create(this, Direction.values());
    private LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> this.energyStorage);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.removed && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandlers[side.getIndex()].cast();
        } else if (cap == CapabilityEnergy.ENERGY) {
            return energyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    public int getEnergyStored() {
        return this.energyStorage.getEnergyStored();
    }

    public int getMaxEnergyStored() {
        return this.energyStorage.getMaxEnergyStored();
    }

    // Util methods

    protected void sendEnergyToSlot(int slotIndex) {
        final ItemStack energyStack = this.stacks.get(slotIndex);
        if (!energyStack.isEmpty()) {
            IEnergyStorage energy = energyStack.getCapability(CapabilityEnergy.ENERGY).orElse(null);
            if (energy != null && energy.canReceive()) {
                int accepted = energy.receiveEnergy(Math.min(MAX_TRANSFER, this.getEnergyStored()), false);
                this.energyStorage.modifyEnergyStored(-accepted);
            }
        }
    }

    protected void receiveEnergyFromSlot(int slotIndex) {
        final ItemStack energyStack = this.stacks.get(slotIndex);
        if (!energyStack.isEmpty()) {
            IEnergyStorage energy = energyStack.getCapability(CapabilityEnergy.ENERGY).orElse(null);
            if (energy != null && energy.canExtract()) {
                int accept = energy.extractEnergy(Math.min(this.getMaxEnergyStored() - this.getEnergyStored(), MAX_TRANSFER), true);
                if(this.getEnergyStored() <= this.getMaxEnergyStored() - accept)
                    this.energyStorage.modifyEnergyStored(energy.extractEnergy(accept, false));
            }
        }
    }

}
