package themcbros.usefulmachinery.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.themcbrothers.lib.util.ContainerHelper;
import themcbros.usefulmachinery.MachineryTags;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;
import themcbros.usefulmachinery.blockentity.CompactorBlockEntity;
import themcbros.usefulmachinery.blockentity.extension.SimpleCompactor;
import themcbros.usefulmachinery.init.MachineryMenus;
import themcbros.usefulmachinery.machine.CompactorMode;
import themcbros.usefulmachinery.menu.slot.EnergySlot;
import themcbros.usefulmachinery.recipes.MachineryRecipeTypes;

public class CompactorMenu extends MachineMenu {
    public CompactorMenu(int id, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        this(id, playerInventory, ContainerHelper.getBlockEntity(CompactorBlockEntity.class, playerInventory, byteBuf), byteBuf.readInt());
    }

    public CompactorMenu(int id, Inventory playerInventory, AbstractMachineBlockEntity tileEntity, int upgradeSlotCount) {
        super(MachineryMenus.COMPACTOR.get(), id, playerInventory, tileEntity, tileEntity.getContainerData(), upgradeSlotCount);

        this.addSlot(new Slot(tileEntity, 0, 35, 33));
        this.addSlot(new Slot(tileEntity, 1, 95, 33));
        this.addSlot(new EnergySlot(tileEntity, 2, 134, 33));

        this.addUpgradeSlots(tileEntity.getUpgradeContainer());
        this.addPlayerSlots(playerInventory);
    }

    public CompactorMode getCompactorMode() {
        return CompactorMode.byIndex(this.fields.get(8));
    }

    public void setCompactorMode(CompactorMode mode) {
        this.fields.set(8, mode.getIndex());
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
            if (index == 1) {
                if (!this.moveItemStackTo(slotStack, invSlotStart, useRowSlotEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, itemstack);
            } else if (index >= invSlotStart) {
                if (this.canProcess(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isEnergyItem(slotStack, false)) {
                    if (!this.moveItemStackTo(slotStack, containerSize - 1, containerSize, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotStack.is(MachineryTags.Items.MACHINERY_UPGRADES)) {
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

    protected boolean canProcess(ItemStack stack) {
        return this.level.getRecipeManager().getRecipeFor(MachineryRecipeTypes.COMPACTING.get(), new SimpleCompactor(this.getCompactorMode(), stack), this.level).map(compactingRecipe -> compactingRecipe.getCompactorMode().equals(CompactorMenu.this.getCompactorMode())).orElse(false);
    }

    public int getProcessTime() {
        return this.fields.get(6);
    }

    public int getProcessTimeTotal() {
        return this.fields.get(7);
    }

    public int getProgressScaled(int width) {
        int i = this.getProcessTime();
        int j = this.getProcessTimeTotal();
        return i != 0 && j != 0 ? i * width / j : 0;
    }
}
