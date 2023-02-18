package themcbros.usefulmachinery.menu;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.themcbrothers.lib.util.ContainerHelper;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;
import themcbros.usefulmachinery.blockentity.LavaGeneratorBlockEntity;
import themcbros.usefulmachinery.blockentity.extension.UpgradeContainer;
import themcbros.usefulmachinery.init.MachineryMenus;
import themcbros.usefulmachinery.menu.slot.EnergySlot;
import themcbros.usefulmachinery.menu.slot.FluidItemSlot;
import themcbros.usefulmachinery.menu.slot.OutputSlot;

import javax.annotation.Nonnull;

public class LavaGeneratorMenu extends AbstractMachineMenu {
    public LavaGeneratorMenu(int id, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        this(id, playerInventory, ContainerHelper.getBlockEntity(AbstractMachineBlockEntity.class, playerInventory, byteBuf),
                new UpgradeContainer(byteBuf.readInt()), new SimpleContainerData(byteBuf.readInt()));
    }

    public LavaGeneratorMenu(int id, Inventory playerInventory, AbstractMachineBlockEntity tileEntity, Container upgradeContainer, ContainerData data) {
        super(MachineryMenus.LAVA_GENERATOR.get(), id, playerInventory, tileEntity, data, upgradeContainer.getContainerSize());

        this.addSlot(new FluidItemSlot(tileEntity, 0, 26, 17, fluidStack -> fluidStack.getFluid().isSame(Fluids.LAVA)));
        this.addSlot(new OutputSlot(tileEntity, 1, 26, 51));
        this.addSlot(new EnergySlot(tileEntity, 2, 134, 33));

        this.addUpgradeSlots(upgradeContainer);
        this.addPlayerSlots(playerInventory);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        // information about slot indexes
        final int containerSize = this.blockEntity.getContainerSize();
        final int invSlotStart = containerSize + this.upgradeSlotCount;
        final int invSlotEnd = invSlotStart + 27;
        final int useRowSlotStart = invSlotEnd;
        final int useRowSlotEnd = useRowSlotStart + 9;

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();
            if (index >= invSlotStart) {
                if (ForgeHooks.getBurnTime(slotStack, null) == 1600) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isEnergyItem(slotStack, true)) {
                    if (!this.moveItemStackTo(slotStack, containerSize - 1, containerSize, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isUpgradeItem(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, containerSize, invSlotStart, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < invSlotEnd) {
                    if (!this.moveItemStackTo(slotStack, invSlotEnd, useRowSlotEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < useRowSlotEnd && !this.moveItemStackTo(slotStack, invSlotStart, invSlotEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, invSlotStart, useRowSlotEnd, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }

        return itemstack;
    }

    public int getBurnTimeScaled() {
        int i = this.fields.get(6);
        int j = LavaGeneratorBlockEntity.TICKS_PER_MB * LavaGeneratorBlockEntity.MB_PER_USE;
        return i != 0 ? i * 13 / j : 0;
    }

    public boolean isBurning() {
        return this.fields.get(6) > 0;
    }

    public FluidStack getTankStack() {
        return new FluidStack(this.getTankFluid(), this.getFluidAmount());
    }

    public int getFluidAmount() {
        return this.fields.get(7);
    }

    public int getTankCapacity() {
        return this.fields.get(8) > 0 ? this.fields.get(8) : LavaGeneratorBlockEntity.TANK_CAPACITY;
    }

    public Fluid getTankFluid() {
        return BuiltInRegistries.FLUID.byId(this.fields.get(9));
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
                return LavaGeneratorMenu.this.getTankCapacity();
            }

            @Nonnull
            @Override
            public FluidStack getFluidInTank(int tank) {
                return LavaGeneratorMenu.this.getTankStack();
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
