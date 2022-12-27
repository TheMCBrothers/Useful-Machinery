package themcbros.usefulmachinery.container;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;
import themcbros.usefulmachinery.blockentity.LavaGeneratorBlockEntity;
import themcbros.usefulmachinery.init.MachineryBlocks;
import themcbros.usefulmachinery.init.MachineryMenus;
import themcbros.usefulmachinery.container.slot.EnergySlot;
import themcbros.usefulmachinery.container.slot.FluidItemSlot;
import themcbros.usefulmachinery.container.slot.OutputSlot;

import javax.annotation.Nonnull;

public class LavaGeneratorContainer extends MachineContainer {
    public LavaGeneratorContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new LavaGeneratorBlockEntity(BlockPos.ZERO, MachineryBlocks.COAL_GENERATOR.get().defaultBlockState()), new SimpleContainerData(9));
    }

    public LavaGeneratorContainer(int id, Inventory playerInventory, AbstractMachineBlockEntity tileEntity, ContainerData fields) {
        super(MachineryMenus.LAVA_GENERATOR.get(), id, playerInventory, tileEntity, fields);

        this.addSlot(new FluidItemSlot(tileEntity, 0, 26, 17, fluidStack -> fluidStack.getFluid().isSame(Fluids.LAVA)));
        this.addSlot(new OutputSlot(tileEntity, 1, 26, 51));
        this.addSlot(new EnergySlot(tileEntity, 2, 134, 33));

        this.addPlayerSlots(playerInventory);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        int i = this.abstractMachineBlockEntity.getContainerSize();
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index >= i) {
                if (ForgeHooks.getBurnTime(itemstack1, null) == 1600) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (isEnergyItem(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, i - 1, i, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 27 + i) {
                    if (!this.moveItemStackTo(itemstack1, 27 + i, 36 + i, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 27 + i && index < 36 + i && !this.moveItemStackTo(itemstack1, i, 27 + i, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, i, 36 + i, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    private boolean isEnergyItem(ItemStack itemstack1) {
        return !itemstack1.isEmpty() && itemstack1.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::canReceive).orElse(false);
    }

    public int getBurnTimeScaled() {
        int i = this.fields.get(5);
        int j = LavaGeneratorBlockEntity.TICKS_PER_MB * LavaGeneratorBlockEntity.MB_PER_USE;
        return i != 0 ? i * 13 / j : 0;
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
        return this.fields.get(7) > 0 ? this.fields.get(7) : LavaGeneratorBlockEntity.TANK_CAPACITY;
    }

    public Fluid getTankFluid() {
        return Registry.FLUID.byId(this.fields.get(8));
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

            @Nonnull
            @Override
            public FluidStack getFluidInTank(int tank) {
                return LavaGeneratorContainer.this.getTankStack();
            }

            @Override
            public int fill(FluidStack resource, FluidAction action) {
                return 0;
            }

            @Nonnull
            @Override
            public FluidStack drain(int maxDrain, FluidAction action) {
                return FluidStack.EMPTY;
            }

            @Nonnull
            @Override
            public FluidStack drain(FluidStack resource, FluidAction action) {
                return resource;
            }
        };
    }
}
