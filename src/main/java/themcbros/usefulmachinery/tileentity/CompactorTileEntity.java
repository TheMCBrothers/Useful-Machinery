package themcbros.usefulmachinery.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;
import themcbros.usefulmachinery.container.CompactorContainer;
import themcbros.usefulmachinery.init.ModTileEntities;
import themcbros.usefulmachinery.machine.CompactorMode;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.recipes.CompactingRecipe;
import themcbros.usefulmachinery.recipes.ModRecipeTypes;
import themcbros.usefulmachinery.util.TextUtils;

import javax.annotation.Nullable;

public class CompactorTileEntity extends MachineTileEntity {

    private static final int RF_PER_TICK = 15;

    private IIntArray fields = new IIntArray() {
        @Override
        public int size() {
            return 8;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4:
                    CompactorTileEntity.this.redstoneMode = RedstoneMode.byIndex(value);
                    break;
                case 5:
                    CompactorTileEntity.this.processTime = value;
                    break;
                case 6:
                    CompactorTileEntity.this.processTimeTotal = value;
                    break;
                case 7:
                    CompactorTileEntity.this.compactorMode = CompactorMode.byIndex(value);
                    break;
            }
        }

        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    // Energy lower bytes
                    return CompactorTileEntity.this.getEnergyStored() & 0xFFFF;
                case 1:
                    // Energy upper bytes
                    return (CompactorTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2:
                    // Max energy lower bytes
                    return CompactorTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3:
                    // Max energy upper bytes
                    return (CompactorTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4:
                    // Redstone mode ordinal
                    return CompactorTileEntity.this.redstoneMode.ordinal();
                case 5:
                    // Crush time
                    return CompactorTileEntity.this.processTime;
                case 6:
                    // Total crush time
                    return CompactorTileEntity.this.processTimeTotal;
                case 7:
                    // Compactor mode index
                    return CompactorTileEntity.this.compactorMode.getIndex();
                default:
                    return 0;
            }
        }
    };

    public CompactorMode compactorMode = CompactorMode.PLATE;
    private int processTime, processTimeTotal;

    public CompactorTileEntity() {
        super(ModTileEntities.COMPACTOR, false);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (this.compactorMode != CompactorMode.PLATE) compound.putInt("Mode", this.compactorMode.getIndex());
        if (this.processTime > 0) compound.putInt("ProcessTime", this.processTime);
        if (this.processTimeTotal > 0) compound.putInt("ProcessTimeTotal", this.processTimeTotal);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        if (compound.contains("Mode", Constants.NBT.TAG_INT)) this.compactorMode = CompactorMode.byIndex(compound.getInt("Mode"));
        if (compound.contains("ProcessTime", Constants.NBT.TAG_INT)) this.processTime = compound.getInt("ProcessTime");
        if (compound.contains("ProcessTimeTotal", Constants.NBT.TAG_INT)) this.processTimeTotal = compound.getInt("ProcessTimeTotal");
        super.read(compound);
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
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return index != 1;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index == 1;
    }

    @Override
    public ITextComponent getDisplayName() {
        return TextUtils.translate("container", "compactor");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CompactorContainer(id, playerInventory, this, this.fields);
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
                CompactingRecipe recipe = this.world.getRecipeManager().getRecipe(ModRecipeTypes.COMPACTING, this, this.world).orElse(null);
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
        return this.world.getRecipeManager().getRecipe(ModRecipeTypes.COMPACTING, this, this.world)
                .map(CompactingRecipe::getProcessTime).orElse(200);
    }

    private boolean canProcess(@Nullable CompactingRecipe recipeIn) {
        if (!this.stacks.get(0).isEmpty() && recipeIn != null && this.redstoneMode.canRun(this) && recipeIn.getCompactorMode().equals(this.compactorMode)) {
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

    private void processItem(@Nullable CompactingRecipe recipe) {
        if (recipe != null && this.canProcess(recipe)) {
            ItemStack itemstack = this.stacks.get(0);
            ItemStack itemstack1 = recipe.getRecipeOutput();
            ItemStack itemstack2 = this.stacks.get(1);
            if (itemstack2.isEmpty()) {
                this.stacks.set(1, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            itemstack.shrink(recipe.getCount());
        }
    }

}
