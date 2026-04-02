package net.themcbrothers.usefulmachinery.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.themcbrothers.lib.inventory.EnergySlot;
import net.themcbrothers.lib.util.ContainerHelper;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import net.themcbrothers.usefulmachinery.block.entity.extension.UpgradeContainer;
import net.themcbrothers.usefulmachinery.core.MachineryMenus;
import net.themcbrothers.usefulmachinery.menu.slot.OutputSlot;

public class ElectricSmelterMenu extends AbstractMachineMenu {
    private final RecipePropertySet acceptedFurnaceInputs;
    private final RecipePropertySet acceptedBlastFurnaceInputs;

    public ElectricSmelterMenu(int id, Inventory inventory, FriendlyByteBuf byteBuf) {
        this(id, inventory, ContainerHelper.getBlockEntity(AbstractMachineBlockEntity.class, inventory, byteBuf),
                new UpgradeContainer(byteBuf.readInt()), new SimpleContainerData(byteBuf.readInt()));
    }

    public ElectricSmelterMenu(int id, Inventory inventory, AbstractMachineBlockEntity blockEntity, Container upgradeContainer, ContainerData fields) {
        super(MachineryMenus.ELECTRIC_SMELTER.get(), id, blockEntity, fields, upgradeContainer.getContainerSize(), inventory);

        this.acceptedFurnaceInputs = this.level.recipeAccess().propertySet(RecipePropertySet.FURNACE_INPUT);
        this.acceptedBlastFurnaceInputs = this.level.recipeAccess().propertySet(RecipePropertySet.BLAST_FURNACE_INPUT);

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
                if (this.canCook(slotStack)) {
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

    @Override
    public boolean isProcessing() {
        return false;
    }

    @Override
    public int getProgressScaled(int size) {
        int cookTime = this.fields.get(4);
        int totalCookTime = this.fields.get(5);

        return cookTime != 0 && totalCookTime != 0 ? cookTime * size / totalCookTime : 0;
    }

    protected boolean canCook(ItemStack stack) {
        return this.acceptedFurnaceInputs.test(stack) || this.acceptedBlastFurnaceInputs.test(stack);
    }
}
