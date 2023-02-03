package themcbros.usefulmachinery.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.themcbrothers.lib.util.ContainerHelper;
import themcbros.usefulmachinery.MachineryTags;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;
import themcbros.usefulmachinery.init.MachineryMenus;
import themcbros.usefulmachinery.menu.slot.EnergySlot;
import themcbros.usefulmachinery.recipes.MachineryRecipeTypes;

public class CrusherMenu extends MachineMenu {
    public CrusherMenu(int id, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        this(id, playerInventory, ContainerHelper.getBlockEntity(AbstractMachineBlockEntity.class, playerInventory, byteBuf), byteBuf.readInt());
    }

    public CrusherMenu(int id, Inventory playerInventory, AbstractMachineBlockEntity tileEntity, int upgradeCountSlot) {
        super(MachineryMenus.CRUSHER.get(), id, playerInventory, tileEntity, tileEntity.getContainerData(), upgradeCountSlot);

        this.addSlot(new Slot(tileEntity, 0, 35, 35));
        this.addSlot(new Slot(tileEntity, 1, 95, 24));
        this.addSlot(new Slot(tileEntity, 2, 95, 48));
        this.addSlot(new EnergySlot(tileEntity, 3, 134, 33));

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
                } else if (this.isEnergyItem(slotStack)) {
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

    private boolean isEnergyItem(ItemStack itemstack) {
        return !itemstack.isEmpty() && itemstack.getCapability(ForgeCapabilities.ENERGY)
                .map(IEnergyStorage::canExtract).orElse(false);
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
