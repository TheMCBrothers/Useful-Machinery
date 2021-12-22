package themcbros.usefulmachinery.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import themcbros.usefulmachinery.container.slot.EnergySlot;
import themcbros.usefulmachinery.init.MachineryBlocks;
import themcbros.usefulmachinery.init.MachineryContainers;
import themcbros.usefulmachinery.machine.CompactorMode;
import themcbros.usefulmachinery.recipes.ModRecipeTypes;
import themcbros.usefulmachinery.blockentity.CompactorBlockEntity;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;

public class CompactorContainer extends MachineContainer {
    private final Level level;

    public CompactorContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new CompactorBlockEntity(BlockPos.ZERO, MachineryBlocks.COMPACTOR.defaultBlockState()), new SimpleContainerData(8));
    }

    public CompactorContainer(int id, Inventory playerInventory, AbstractMachineBlockEntity tileEntity, ContainerData fields) {
        super(MachineryContainers.COMPACTOR, id, playerInventory, tileEntity, fields);
        this.level = playerInventory.player.level;

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
    public ItemStack quickMoveStack(Player playerIn, int index) {
        int i = this.abstractMachineBlockEntity.getContainerSize();
        ItemStack itemstack1 = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack1 = slotStack.copy();
            if (index == 1) {
                if (!this.moveItemStackTo(slotStack, i, 36 + i, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, itemstack1);
            } else if (index != 0) {
                if (this.canProcess(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isEnergyItem(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, i - 1, i, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= i && index < 27 + i) {
                    if (!this.moveItemStackTo(slotStack, 27 + i, 36 + i, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 27 + i && index < 36 + i && !this.moveItemStackTo(slotStack, i, 27 + i, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, i, 36 + i, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
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
        return this.level.getRecipeManager().getRecipeFor(ModRecipeTypes.COMPACTING, new SimpleContainer(stack), this.level)
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
