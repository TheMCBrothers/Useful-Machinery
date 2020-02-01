package themcbros.usefulmachinery.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import themcbros.usefulmachinery.container.slot.EnergySlot;
import themcbros.usefulmachinery.init.ModContainers;
import themcbros.usefulmachinery.machine.CompactorMode;
import themcbros.usefulmachinery.recipes.ModRecipeTypes;
import themcbros.usefulmachinery.tileentity.CompactorTileEntity;
import themcbros.usefulmachinery.tileentity.MachineTileEntity;

public class CompactorContainer extends MachineContainer {

    private World world;

    public CompactorContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new CompactorTileEntity(), new IntArray(8));
    }

    public CompactorContainer(int id, PlayerInventory playerInventory, MachineTileEntity tileEntity, IIntArray fields) {
        super(ModContainers.COMPACTOR, id, playerInventory, tileEntity, fields);
        this.world = playerInventory.player.world;

        this.addSlot(new Slot(tileEntity, 0, 35, 33));
        this.addSlot(new Slot(tileEntity, 1, 95, 33));
        this.addSlot(new EnergySlot(tileEntity, 2, 134, 33));

        this.addPlayerSlots(playerInventory);

    }

    public CompactorMode getCompactorMode() {
        return CompactorMode.byIndex(this.fields.get(7));
    }

    public void setCompactorMode(CompactorMode mode) {
        this.fields.set(7, mode.getIndex());
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        int i = this.machineTileEntity.getSizeInventory();
        ItemStack itemstack1 = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemstack1 = slotStack.copy();
            if (index == 1) {
                if (!this.mergeItemStack(slotStack, i, 36 + i, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(slotStack, itemstack1);
            } else if (index != 0) {
                if (this.canProcess(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isEnergyItem(slotStack)) {
                    if (!this.mergeItemStack(slotStack, i - 1, i, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= i && index < 27 + i) {
                    if (!this.mergeItemStack(slotStack, 27 + i, 36 + i, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 27 + i && index < 36 + i && !this.mergeItemStack(slotStack, i, 27 + i, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(slotStack, i, 36 + i, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (slotStack.getCount() == itemstack1.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }

        return itemstack1;
    }

    private boolean isEnergyItem(ItemStack itemstack1) {
        return !itemstack1.isEmpty() && itemstack1.getCapability(CapabilityEnergy.ENERGY)
                .map(IEnergyStorage::canExtract).orElse(false);
    }

    protected boolean canProcess(ItemStack stack) {
        return this.world.getRecipeManager().getRecipe(ModRecipeTypes.COMPACTING, new Inventory(stack), this.world)
                        .map(compactingRecipe -> compactingRecipe.getCompactorMode().equals(CompactorContainer.this.getCompactorMode())).orElse(false);
    }

    public int getProcessTime() {
        return this.fields.get(5);
    }

    public int getProcessTimeTotal() {
        return this.fields.get(6);
    }

    public int getProgressScaled(int width) {
        int i = this.getProcessTime();
        int j = this.getProcessTimeTotal();
        return i != 0 && j != 0 ? i * width / j : 0;
    }

}
