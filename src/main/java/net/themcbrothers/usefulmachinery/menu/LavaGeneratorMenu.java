package net.themcbrothers.usefulmachinery.menu;

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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.themcbrothers.lib.inventory.EnergySlot;
import net.themcbrothers.lib.util.ContainerHelper;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import net.themcbrothers.usefulmachinery.block.entity.LavaGeneratorBlockEntity;
import net.themcbrothers.usefulmachinery.block.entity.extension.UpgradeContainer;
import net.themcbrothers.usefulmachinery.core.MachineryMenus;
import net.themcbrothers.usefulmachinery.menu.slot.FluidItemSlot;
import net.themcbrothers.usefulmachinery.menu.slot.OutputSlot;

import static net.themcbrothers.usefulmachinery.core.MachineryItems.SUSTAINED_UPGRADE;

public class LavaGeneratorMenu extends AbstractMachineMenu {
    public LavaGeneratorMenu(int id, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        this(id, playerInventory, ContainerHelper.getBlockEntity(AbstractMachineBlockEntity.class, playerInventory, byteBuf),
                new UpgradeContainer(byteBuf.readInt()), new SimpleContainerData(byteBuf.readInt()));
    }


    public LavaGeneratorMenu(int id, Inventory playerInventory, AbstractMachineBlockEntity blockEntity, Container upgradeContainer, ContainerData fields) {
        super(MachineryMenus.LAVA_GENERATOR.get(), id, blockEntity, fields, upgradeContainer.getContainerSize());

        this.addSlot(new FluidItemSlot(blockEntity, 0, 26, 17, stack -> stack.getFluid().isSame(Fluids.LAVA)));
        this.addSlot(new OutputSlot(blockEntity, 1, 26, 51));
        this.addSlot(new EnergySlot(blockEntity, 2, 134, 33));

        this.addUpgradeSlots(upgradeContainer);
        this.addPlayerSlots(playerInventory);
    }

    @Override
    protected boolean isUpgradeItem(ItemStack stack) {
        return stack.is(SUSTAINED_UPGRADE.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // information about slot indexes
        final int containerSize = this.blockEntity.getContainerSize();
        final int invSlotStart = containerSize + this.upgradeSlotCount;
        final int invSlotEnd = invSlotStart + 27;
        final int hotbarSlotStart = invSlotEnd;
        final int hotbarSlotEnd = hotbarSlotStart + 9;

        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();

            // Checking if shift clicking stack out of inventory into the machine
            if (index >= invSlotStart) {
                FluidTank lavaTank = ((LavaGeneratorBlockEntity) this.blockEntity).getLavaTank();
                boolean isLava = FluidUtil.tryEmptyContainer(slotStack, lavaTank, Integer.MAX_VALUE, null, false).isSuccess();

                if (isLava) {
                    // Checking if stack has not been moved into fuel slot
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isEnergyItem(slotStack, EnergySlot.ItemMode.RECEIVE)) {
                    // Checking if stack has not been moved into energy slot
                    if (!this.moveItemStackTo(slotStack, containerSize - 1, containerSize, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isUpgradeItem(slotStack)) {
                    // Checking if stack has not been moved into the upgrade container
                    if (!this.moveItemStackTo(slotStack, containerSize, invSlotStart, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // Checking if shift clicking stack from the inventory into the hotbar
                else if (index < invSlotEnd) {
                    // Checking if stack has not been moved into the hotbar
                    if (!this.moveItemStackTo(slotStack, invSlotEnd, hotbarSlotEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // Checking if shift clicking stack from the hotbar into the inventory
                else if (index < hotbarSlotEnd) {
                    // Checking if stack has not been moved into the inventory
                    if (!this.moveItemStackTo(slotStack, invSlotStart, invSlotEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            // Checking if shift clicking stack from the hotbar into the inventory
            else if (!this.moveItemStackTo(slotStack, invSlotStart, hotbarSlotEnd, false)) {
                return ItemStack.EMPTY;
            }

            // If here then logic successful
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return stack;
    }

    public int getBurnTimeScaled() {
        // Burn time
        int i = this.fields.get(4);
        // Total burn time
        int j = this.fields.get(5);

        return j != 0 ? i * 13 / j : 0;
    }

    public boolean isBurning() {
        return this.fields.get(4) > 0;
    }

    public FluidStack getTank() {
        return new FluidStack(this.getTankFluid(), this.getFluidAmount());
    }

    public int getFluidAmount() {
        return this.fields.get(6);
    }

    public int getTankCapacity() {
        return this.fields.get(7) > 0 ? this.fields.get(7) : LavaGeneratorBlockEntity.TANK_CAPACITY;
    }

    public Fluid getTankFluid() {
        return BuiltInRegistries.FLUID.byId(this.fields.get(8));
    }

    public IFluidHandler getFluidTankHandler() {
        return new IFluidHandler() {
            @Override
            public int getTanks() {
                return 1;
            }

            @Override
            public FluidStack getFluidInTank(int tank) {
                return LavaGeneratorMenu.this.getTank();
            }

            @Override
            public int getTankCapacity(int tank) {
                return LavaGeneratorMenu.this.getTankCapacity();
            }

            @Override
            public boolean isFluidValid(int tank, FluidStack stack) {
                return true;
            }

            @Override
            public int fill(FluidStack resource, FluidAction action) {
                return 0;
            }

            @Override
            public FluidStack drain(FluidStack resource, FluidAction action) {
                return resource;
            }

            @Override
            public FluidStack drain(int maxDrain, FluidAction action) {
                return FluidStack.EMPTY;
            }
        };
    }
}
