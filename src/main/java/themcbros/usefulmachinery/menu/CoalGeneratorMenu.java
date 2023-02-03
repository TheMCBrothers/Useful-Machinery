package themcbros.usefulmachinery.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.themcbrothers.lib.util.ContainerHelper;
import themcbros.usefulmachinery.MachineryTags;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;
import themcbros.usefulmachinery.blockentity.CoalGeneratorBlockEntity;
import themcbros.usefulmachinery.init.MachineryMenus;
import themcbros.usefulmachinery.menu.slot.EnergySlot;

public class CoalGeneratorMenu extends MachineMenu {
    public CoalGeneratorMenu(int id, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        this(id, playerInventory, ContainerHelper.getBlockEntity(CoalGeneratorBlockEntity.class, playerInventory, byteBuf), byteBuf.readInt());
    }

    public CoalGeneratorMenu(int id, Inventory playerInventory, AbstractMachineBlockEntity tileEntity, int upgradeSlotCount) {
        super(MachineryMenus.COAL_GENERATOR.get(), id, playerInventory, tileEntity, tileEntity.getContainerData(), upgradeSlotCount);

        this.addSlot(new Slot(tileEntity, 0, 80, 33));
        this.addSlot(new EnergySlot(tileEntity, 1, 134, 33));

        this.addUpgradeSlots(tileEntity.getUpgradeContainer());
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

        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (index >= invSlotStart) {
                if (AbstractFurnaceBlockEntity.isFuel(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isEnergyItem(slotStack, true)) {
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

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }

        return itemStack;
    }

    public int getBurnTimeScaled() {
        int i = this.fields.get(6);
        int j = this.fields.get(7);
        return i != 0 && j != 0 ? i * 13 / j : 0;
    }

    public boolean isBurning() {
        return this.fields.get(6) > 0;
    }
}
