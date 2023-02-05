package themcbros.usefulmachinery.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.themcbrothers.lib.util.ContainerHelper;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;
import themcbros.usefulmachinery.init.MachineryMenus;
import themcbros.usefulmachinery.menu.slot.EnergySlot;
import themcbros.usefulmachinery.menu.slot.OutputSlot;
import themcbros.usefulmachinery.recipes.MachineryRecipeTypes;

public class CrusherMenu extends MachineMenu {
    public CrusherMenu(int id, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        this(id, playerInventory, ContainerHelper.getBlockEntity(AbstractMachineBlockEntity.class, playerInventory, byteBuf),
                new SimpleContainer(byteBuf.readInt()), new SimpleContainerData(byteBuf.readInt()));
    }

    public CrusherMenu(int id, Inventory playerInventory, AbstractMachineBlockEntity tileEntity, Container upgradeContainer, ContainerData data) {
        super(MachineryMenus.CRUSHER.get(), id, playerInventory, tileEntity, data, upgradeContainer.getContainerSize());

        this.addSlot(new Slot(tileEntity, 0, 35, 35));
        this.addSlot(new OutputSlot(tileEntity, 1, 95, 24));
        this.addSlot(new OutputSlot(tileEntity, 2, 95, 48));
        this.addSlot(new EnergySlot(tileEntity, 3, 134, 33));

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
            if (index == 1 || index == 2) {
                if (!this.moveItemStackTo(slotStack, invSlotStart, useRowSlotEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, itemstack);
            } else if (index >= invSlotStart) {
                if (this.canCrush(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isEnergyItem(slotStack, false)) {
                    if (!this.moveItemStackTo(slotStack, containerSize - 1, containerSize, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isUpgradeItem(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, containerSize, invSlotStart, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < invSlotEnd) {
                    if (!this.moveItemStackTo(slotStack, useRowSlotStart, useRowSlotEnd, false)) {
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

    protected boolean canCrush(ItemStack stack) {
        return this.level.getRecipeManager()
                .getRecipeFor(MachineryRecipeTypes.CRUSHING.get(), new SimpleContainer(stack), this.level)
                .isPresent();
    }

    public int getCrushTime() {
        return this.fields.get(6);
    }

    public int getCrushTimeTotal() {
        return this.fields.get(7);
    }

    public int getProgressScaled(int width) {
        int i = this.getCrushTime();
        int j = this.getCrushTimeTotal();
        return i != 0 && j != 0 ? i * width / j : 0;
    }
}
