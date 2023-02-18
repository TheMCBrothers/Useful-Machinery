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
import net.minecraft.world.item.crafting.RecipeType;
import net.themcbrothers.lib.util.ContainerHelper;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;
import themcbros.usefulmachinery.blockentity.extension.UpgradeContainer;
import themcbros.usefulmachinery.init.MachineryMenus;
import themcbros.usefulmachinery.menu.slot.EnergySlot;
import themcbros.usefulmachinery.menu.slot.OutputSlot;

public class ElectricSmelterMenu extends AbstractMachineMenu {
    public ElectricSmelterMenu(int id, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        this(id, playerInventory, ContainerHelper.getBlockEntity(AbstractMachineBlockEntity.class, playerInventory, byteBuf),
                new UpgradeContainer(byteBuf.readInt()), new SimpleContainerData(byteBuf.readInt()));
    }

    public ElectricSmelterMenu(int id, Inventory playerInventory, AbstractMachineBlockEntity tileEntity, Container upgradeContainer, ContainerData data) {
        super(MachineryMenus.ELECTRIC_SMELTER.get(), id, playerInventory, tileEntity, data, upgradeContainer.getContainerSize());

        this.addSlot(new Slot(tileEntity, 0, 35, 33));
        this.addSlot(new OutputSlot(tileEntity, 1, 95, 33));
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
            if (index == 1) {
                if (!this.moveItemStackTo(slotStack, invSlotStart, useRowSlotEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, itemstack);
            } else if (index >= invSlotStart) {
                if (this.canCook(slotStack)) {
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

    protected boolean canCook(ItemStack stack) {
        return this.level.getRecipeManager()
                .getRecipeFor(RecipeType.BLASTING, new SimpleContainer(stack), this.level)
                .isPresent() || this.level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), this.level)
                .isPresent();
    }

    public int getCookTime() {
        return this.fields.get(6);
    }

    public int getCookTimeTotal() {
        return this.fields.get(7);
    }

    public int getProgressScaled(int width) {
        int i = this.getCookTime();
        int j = this.getCookTimeTotal();
        return i != 0 && j != 0 ? i * width / j : 0;
    }
}
