package net.themcbrothers.usefulmachinery.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.themcbrothers.lib.inventory.EnergySlot;
import net.themcbrothers.lib.util.ContainerHelper;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import net.themcbrothers.usefulmachinery.block.entity.extension.UpgradeContainer;
import net.themcbrothers.usefulmachinery.core.MachineryMenus;
import net.themcbrothers.usefulmachinery.core.MachineryRecipePropertySet;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;
import net.themcbrothers.usefulmachinery.menu.slot.OutputSlot;

public class CompactorMenu extends AbstractMachineMenu {
    public CompactorMenu(int id, Inventory inventory, FriendlyByteBuf buffer) {
        this(id, inventory, ContainerHelper.getBlockEntity(AbstractMachineBlockEntity.class, inventory, buffer),
                new UpgradeContainer(buffer.readInt()), new SimpleContainerData(buffer.readInt()));
    }

    public CompactorMenu(int id, Inventory inventory, AbstractMachineBlockEntity blockEntity, Container upgradeContainer, ContainerData fields) {
        super(MachineryMenus.COMPACTOR.get(), id, blockEntity, fields, upgradeContainer.getContainerSize(), inventory);

        this.acceptedInputs = this.level.recipeAccess().propertySet(MachineryRecipePropertySet.COMPACTOR_INPUT);

        this.addSlot(new Slot(blockEntity, 0, 35, 33));
        this.addSlot(new OutputSlot(blockEntity, 1, 95, 33));
        this.addSlot(new EnergySlot(blockEntity, 2, 134, 33));

        this.addUpgradeSlots(upgradeContainer);
        this.addPlayerSlots(inventory);
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

            // Checking if shift clicking stack from the output into the inventory
            if (index == 1) {
                if (!this.moveItemStackTo(slotStack, invSlotStart, hotbarSlotEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, stack);
            }
            // Checking if shift clicking stack out of inventory into the machine
            else if (index >= invSlotStart) {
                if (this.canCompact(slotStack)) {
                    // Checking if stack has not been moved into fuel slot
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isEnergyItem(slotStack, EnergySlot.ItemMode.EXTRACT)) {
                    // Checking if stack has not been moved into energy slot
                    if (!this.moveItemStackTo(slotStack, containerSize - 1, containerSize, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.supportsUpgrade(slotStack)) {
                    // Checking if stack has not been moved into the upgrade container
                    if (!this.moveItemStackTo(slotStack, containerSize, invSlotStart, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // Checking if shift clicking stack from the inventory into the hotbar
                else if (index < invSlotEnd) {
                    // Checking if stack has not been moved into the hotbar
                    if (!this.moveItemStackTo(slotStack, hotbarSlotStart, hotbarSlotEnd, false)) {
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

    protected boolean canCompact(ItemStack stack) {
        return this.acceptedInputs.test(stack);
    }

    public CompactorMode getCompactorMode() {
        return CompactorMode.byOrdinal(this.fields.get(6));
    }

    public void setCompactorMode(CompactorMode mode) {
        this.fields.set(6, mode.ordinal());
    }

    public int getProgressScaled(int width) {
        int compactTime = this.fields.get(4);
        int totalCompactTime = this.fields.get(5);

        return compactTime != 0 && totalCompactTime != 0 ? compactTime * width / totalCompactTime : 0;
    }
}
