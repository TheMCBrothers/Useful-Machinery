package themcbros.usefulmachinery.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;
import themcbros.usefulmachinery.blockentity.CoalGeneratorBlockEntity;
import themcbros.usefulmachinery.menu.slot.EnergySlot;
import themcbros.usefulmachinery.init.MachineryBlocks;
import themcbros.usefulmachinery.init.MachineryMenus;

public class CoalGeneratorMenu extends MachineMenu {
    public CoalGeneratorMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new CoalGeneratorBlockEntity(BlockPos.ZERO, MachineryBlocks.COAL_GENERATOR.get().defaultBlockState()), new SimpleContainerData(7));
    }

    public CoalGeneratorMenu(int id, Inventory playerInventory, AbstractMachineBlockEntity tileEntity, ContainerData fields) {
        super(MachineryMenus.COAL_GENERATOR.get(), id, playerInventory, tileEntity, fields);

        this.addSlot(new Slot(tileEntity, 0, 80, 33));
        this.addSlot(new EnergySlot(tileEntity, 1, 134, 33));

        this.addPlayerSlots(playerInventory);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        int i = this.abstractMachineBlockEntity.getContainerSize();
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index != 0) {
                if (AbstractFurnaceBlockEntity.isFuel(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (isEnergyItem(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= i && index < 27 + i) {
                    if (!this.moveItemStackTo(itemstack1, 27 + i, 36 + i, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 27 + i && index < 36 + i && !this.moveItemStackTo(itemstack1, i, 27 + i, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, i, 36 + i, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    private boolean isEnergyItem(ItemStack itemstack1) {
        return !itemstack1.isEmpty() && itemstack1.getCapability(ForgeCapabilities.ENERGY)
                .map(IEnergyStorage::canExtract).orElse(false);
    }

    public int getBurnTimeScaled() {
        int i = this.fields.get(5);
        int j = this.fields.get(6);
        return i != 0 && j != 0 ? i * 13 / j : 0;
    }

    public boolean isBurning() {
        return this.fields.get(5) > 0;
    }
}