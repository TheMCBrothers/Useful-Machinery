package themcbros.usefulmachinery.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import themcbros.usefulmachinery.container.slot.EnergySlot;
import themcbros.usefulmachinery.container.slot.FluidItemSlot;
import themcbros.usefulmachinery.container.slot.OutputSlot;
import themcbros.usefulmachinery.init.ModContainers;
import themcbros.usefulmachinery.tileentity.LavaGeneratorTileEntity;
import themcbros.usefulmachinery.tileentity.MachineTileEntity;

public class LavaGeneratorContainer extends MachineContainer {

    public LavaGeneratorContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new LavaGeneratorTileEntity(), new IntArray(9));
    }

    public LavaGeneratorContainer(int id, PlayerInventory playerInventory, MachineTileEntity tileEntity, IIntArray fields) {
        super(ModContainers.LAVA_GENERATOR, id, playerInventory, tileEntity, fields);

        this.addSlot(new FluidItemSlot(tileEntity, 0, 26, 17, fluidStack -> fluidStack.getFluid().isIn(FluidTags.LAVA)));
        this.addSlot(new OutputSlot(tileEntity, 1, 26, 51));
        this.addSlot(new EnergySlot(tileEntity, 2, 134, 33));

        this.addPlayerSlots(playerInventory);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        int i = this.machineTileEntity.getSizeInventory();
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index >= i) {
                if (AbstractFurnaceTileEntity.isFuel(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (isEnergyItem(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, i - 1, i, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= i && index < 27 + i) {
                    if (!this.mergeItemStack(itemstack1, 27 + i, 36 + i, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 27 + i && index < 36 + i && !this.mergeItemStack(itemstack1, i, 27 + i, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, i, 36 + i, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    private boolean isEnergyItem(ItemStack itemstack1) {
        return !itemstack1.isEmpty() && itemstack1.getCapability(CapabilityEnergy.ENERGY)
                .map(IEnergyStorage::canReceive).orElse(false);
    }

    public int getBurnTimeScaled() {
        int i = this.fields.get(5);
        int j = LavaGeneratorTileEntity.TICKS_PER_MB * LavaGeneratorTileEntity.MB_PER_USE;
        return i != 0 && j != 0 ? i * 13 / j : 0;
    }

    public boolean isBurning() {
        return this.fields.get(5) > 0;
    }

    public FluidStack getTankStack() {
        return new FluidStack(this.getTankFluid(), this.getFluidAmount());
    }

    public int getFluidAmount() {
        return this.fields.get(6);
    }

    public int getTankCapacity() {
        return this.fields.get(7) > 0 ? this.fields.get(7) : LavaGeneratorTileEntity.TANK_CAPACITY;
    }

    public Fluid getTankFluid() {
        return Registry.FLUID.getByValue(this.fields.get(8));
    }

    public IFluidHandler getFluidTankHandler() {
        return new IFluidHandler() {

            @Override
            public boolean isFluidValid(int tank, FluidStack stack) {
                return true;
            }

            @Override
            public int getTanks() {
                return 1;
            }

            @Override
            public int getTankCapacity(int tank) {
                return LavaGeneratorContainer.this.getTankCapacity();
            }

            @Override
            public FluidStack getFluidInTank(int tank) {
                return LavaGeneratorContainer.this.getTankStack();
            }

            @Override
            public int fill(FluidStack resource, FluidAction action) {
                return 0;
            }

            @Override
            public FluidStack drain(int maxDrain, FluidAction action) {
                return FluidStack.EMPTY;
            }

            @Override
            public FluidStack drain(FluidStack resource, FluidAction action) {
                return resource;
            }
        };
    }

}
