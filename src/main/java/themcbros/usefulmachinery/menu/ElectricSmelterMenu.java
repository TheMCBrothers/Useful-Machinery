package themcbros.usefulmachinery.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;
import themcbros.usefulmachinery.blockentity.ElectricSmelterBlockEntity;
import themcbros.usefulmachinery.menu.slot.EnergySlot;
import themcbros.usefulmachinery.init.MachineryBlocks;
import themcbros.usefulmachinery.init.MachineryMenus;

public class ElectricSmelterMenu extends MachineMenu {
    private final Level level;

    public ElectricSmelterMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new ElectricSmelterBlockEntity(BlockPos.ZERO, MachineryBlocks.ELECTRIC_SMELTER.get().defaultBlockState()), new SimpleContainerData(7));
    }

    public ElectricSmelterMenu(int id, Inventory playerInventory, AbstractMachineBlockEntity tileEntity, ContainerData fields) {
        super(MachineryMenus.ELECTRIC_SMELTER.get(), id, playerInventory, tileEntity, fields);
        this.level = playerInventory.player.level;

        this.addSlot(new Slot(tileEntity, 0, 35, 33));
        this.addSlot(new Slot(tileEntity, 1, 95, 33));
        this.addSlot(new EnergySlot(tileEntity, 2, 134, 33));

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
            if (index == 1) {
                if (!this.moveItemStackTo(slotStack, i, 36 + i, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, itemstack1);
            } else if (index != 0) {
                if (this.canCook(slotStack)) {
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
        return !itemstack1.isEmpty() && itemstack1.getCapability(ForgeCapabilities.ENERGY)
                .map(IEnergyStorage::canExtract).orElse(false);
    }

    protected boolean canCook(ItemStack stack) {
        return this.level.getRecipeManager()
                .getRecipeFor(RecipeType.BLASTING, new SimpleContainer(stack), this.level)
                .isPresent() || this.level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), this.level)
                .isPresent();
    }

    public int getCookTime() {
        return this.fields.get(5);
    }

    public int getCookTimeTotal() {
        return this.fields.get(6);
    }

    public int getProgressScaled(int width) {
        int i = this.getCookTime();
        int j = this.getCookTimeTotal();
        return i != 0 && j != 0 ? i * width / j : 0;
    }
}
