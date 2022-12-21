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
import themcbros.usefulmachinery.init.MachineryMenus;
import themcbros.usefulmachinery.recipes.MachineryRecipeTypes;
import themcbros.usefulmachinery.blockentity.CrusherBlockEntity;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;

public class CrusherContainer extends MachineContainer {
    private final Level level;

    public CrusherContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new CrusherBlockEntity(BlockPos.ZERO, MachineryBlocks.CRUSHER.get().defaultBlockState()), new SimpleContainerData(7));
    }

    public CrusherContainer(int id, Inventory playerInventory, AbstractMachineBlockEntity tileEntity, ContainerData fields) {
        super(MachineryMenus.CRUSHER.get(), id, playerInventory, tileEntity, fields);
        this.level = playerInventory.player.level;

        this.addSlot(new Slot(tileEntity, 0, 35, 35));
        this.addSlot(new Slot(tileEntity, 1, 95, 24));
        this.addSlot(new Slot(tileEntity, 2, 95, 48));
        this.addSlot(new EnergySlot(tileEntity, 3, 134, 33));

        this.addPlayerSlots(playerInventory);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        int i = this.abstractMachineBlockEntity.getContainerSize();
        ItemStack itemstack1 = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack1 = slotStack.copy();
            if (index == 1 || index == 2) {
                if (!this.moveItemStackTo(slotStack, i, 36 + i, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, itemstack1);
            } else if (index != 0) {
                if (this.canCrush(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isEnergyItem(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 3, 4, false)) {
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

    protected boolean canCrush(ItemStack stack) {
        return this.level.getRecipeManager()
                .getRecipeFor(MachineryRecipeTypes.CRUSHING.get(), new SimpleContainer(stack), this.level)
                .isPresent();
    }

    public int getCrushTime() {
        return this.fields.get(5);
    }

    public int getCrushTimeTotal() {
        return this.fields.get(6);
    }

    public int getProgressScaled(int width) {
        int i = this.getCrushTime();
        int j = this.getCrushTimeTotal();
        return i != 0 && j != 0 ? i * width / j : 0;
    }
}
