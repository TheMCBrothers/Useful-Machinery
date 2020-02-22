package themcbros.usefulmachinery.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import themcbros.usefulmachinery.container.ElectricSmelterContainer;
import themcbros.usefulmachinery.init.ModTileEntities;
import themcbros.usefulmachinery.machine.RedstoneMode;

import javax.annotation.Nullable;

public class ElectricSmelterTileEntity extends MachineTileEntity {

    private static final int RF_PER_TICK = 10;

    private IIntArray fields = new IIntArray() {
        @Override
        public int size() {
            return 7;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4:
                    ElectricSmelterTileEntity.this.redstoneMode = RedstoneMode.byIndex(value);
                    break;
                case 5:
                    ElectricSmelterTileEntity.this.processTime = value;
                    break;
                case 6:
                    ElectricSmelterTileEntity.this.processTimeTotal = value;
                    break;
            }
        }

        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    // Energy lower bytes
                    return ElectricSmelterTileEntity.this.getEnergyStored() & 0xFFFF;
                case 1:
                    // Energy upper bytes
                    return (ElectricSmelterTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2:
                    // Max energy lower bytes
                    return ElectricSmelterTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3:
                    // Max energy upper bytes
                    return (ElectricSmelterTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4:
                    // Redstone mode ordinal
                    return ElectricSmelterTileEntity.this.redstoneMode.ordinal();
                case 5:
                    // Crush time
                    return ElectricSmelterTileEntity.this.processTime;
                case 6:
                    // Total crush time
                    return ElectricSmelterTileEntity.this.processTimeTotal;
                default:
                    return 0;
            }
        }
    };

    public ElectricSmelterTileEntity() {
        super(ModTileEntities.ELECTRIC_SMELTER, false);
    }

    @Override
    int[] getInputSlots() {
        return new int[] {0};
    }

    @Override
    int[] getOutputSlots() {
        return new int[] {1};
    }

    @Override
    public int getSizeInventory() {
        return 3;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.stacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.stacks.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.processTimeTotal = this.getProcessTime();
            this.processTime = 0;
            this.markDirty();
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.usefulmachinery.electric_smelter");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ElectricSmelterContainer(id, playerInventory, this, this.fields);
    }

    private boolean isActive() {
        return this.processTime > 0 && this.energyStorage.getEnergyStored() >= RF_PER_TICK;
    }

    @Override
    public void tick() {

        boolean shouldLit = this.isActive();
        boolean flag1 = false;

        assert this.world != null;
        if (!this.world.isRemote) {
            this.receiveEnergyFromSlot(2);

            if (this.isActive() || this.getEnergyStored() >= RF_PER_TICK && !this.stacks.get(0).isEmpty()) {
                AbstractCookingRecipe recipe = this.world.getRecipeManager().getRecipe(IRecipeType.BLASTING, this, this.world).orElse(null);
                if (recipe == null) recipe = this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, this, this.world).orElse(null);

                if (!this.isActive() && this.canProcess(recipe)) {
                    this.energyStorage.modifyEnergyStored(-RF_PER_TICK);
                    this.processTime++;
                }

                if (this.isActive() && this.canProcess(recipe)) {
                    this.energyStorage.modifyEnergyStored(-RF_PER_TICK);
                    this.processTime++;
                    if (this.processTime == this.processTimeTotal) {
                        this.processTime = 0;
                        this.processTimeTotal = this.getProcessTime();
                        this.processItem(recipe);
                        flag1 = true;
                    }
                } else {
                    this.processTime = 0;
                }
            }

            if (shouldLit != this.isActive()) {
                flag1 = true;
                this.sendUpdate(this.isActive());
            }
        }

        if (flag1) {
            this.markDirty();
        }
    }

    private int getProcessTime() {
        if (world == null) return 200;
        return this.world.getRecipeManager().getRecipe(IRecipeType.BLASTING, this, this.world)
                .map(AbstractCookingRecipe::getCookTime).orElse(this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, this, this.world)
                        .map(AbstractCookingRecipe::getCookTime).orElse(200));
    }

    private boolean canProcess(@Nullable AbstractCookingRecipe recipeIn) {
        if (!this.stacks.get(0).isEmpty() && recipeIn != null && this.redstoneMode.canRun(this)) {
            ItemStack itemstack = recipeIn.getRecipeOutput();
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.stacks.get(1);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.isItemEqual(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    private void processItem(@Nullable AbstractCookingRecipe recipe) {
        if (recipe != null && this.canProcess(recipe)) {
            ItemStack itemstack = this.stacks.get(0);
            ItemStack itemstack1 = recipe.getRecipeOutput();
            ItemStack itemstack2 = this.stacks.get(1);
            if (itemstack2.isEmpty()) {
                this.stacks.set(1, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            itemstack.shrink(1);
        }
    }
}
