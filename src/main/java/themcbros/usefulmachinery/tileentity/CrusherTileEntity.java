package themcbros.usefulmachinery.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import themcbros.usefulmachinery.container.CrusherContainer;
import themcbros.usefulmachinery.init.ModTileEntities;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.recipes.CrusherRecipe;
import themcbros.usefulmachinery.recipes.ModRecipeTypes;

import javax.annotation.Nullable;

public class CrusherTileEntity extends MachineTileEntity {

    private static final int RF_PER_TICK = 10;

    private int crushTime, crushTimeTotal;

    private IIntArray fields = new IIntArray() {
        @Override
        public int size() {
            return 7;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4:
                    CrusherTileEntity.this.redstoneMode = RedstoneMode.byIndex(value);
                    break;
                case 5:
                    CrusherTileEntity.this.crushTime = value;
                    break;
                case 6:
                    CrusherTileEntity.this.crushTimeTotal = value;
                    break;
            }
        }

        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    // Energy lower bytes
                    return CrusherTileEntity.this.getEnergyStored() & 0xFFFF;
                case 1:
                    // Energy upper bytes
                    return (CrusherTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2:
                    // Max energy lower bytes
                    return CrusherTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3:
                    // Max energy upper bytes
                    return (CrusherTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4:
                    // Redstone mode ordinal
                    return CrusherTileEntity.this.redstoneMode.ordinal();
                case 5:
                    // Crush time
                    return CrusherTileEntity.this.crushTime;
                case 6:
                    // Total crush time
                    return CrusherTileEntity.this.crushTimeTotal;
                default:
                    return 0;
            }
        }
    };

    public CrusherTileEntity() {
        super(ModTileEntities.CRUSHER, false);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("CrushTime", this.crushTime);
        compound.putInt("CrushTimeTotal", this.crushTimeTotal);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        this.crushTime = compound.getInt("CrushTime");
        this.crushTimeTotal = compound.getInt("CrushTimeTotal");
        super.read(compound);
    }

    @Override
    int[] getInputSlots() {
        return new int[] {0};
    }

    @Override
    int[] getOutputSlots() {
        return new int[] {1, 2};
    }

    @Override
    public int getSizeInventory() {
        return 4;
    }

    private boolean isActive() {
        return this.crushTime > 0 && this.energyStorage.getEnergyStored() >= RF_PER_TICK;
    }

    @Override
    public void tick() {

        boolean shouldLit = this.isActive();
        boolean flag1 = false;

        assert this.world != null;
        if (!this.world.isRemote) {

            this.receiveEnergyFromSlot(3);

            if (this.isActive() || this.energyStorage.getEnergyStored() >= RF_PER_TICK && !this.stacks.get(0).isEmpty()) {
                CrusherRecipe crusherRecipe = this.world.getRecipeManager().getRecipe(ModRecipeTypes.CRUSHING, this, this.world).orElse(null);
                if (!this.isActive() && this.canCrush(crusherRecipe)) {
                    this.energyStorage.modifyEnergyStored(-RF_PER_TICK);
                    crushTime++;
                }

                if (this.isActive() && this.canCrush(crusherRecipe)) {
                    this.crushTime++;
                    this.energyStorage.modifyEnergyStored(-RF_PER_TICK);
                    if (this.crushTime == this.crushTimeTotal) {
                        this.crushTime = 0;
                        this.crushTimeTotal = this.getCrushTime();
                        this.crushItem(crusherRecipe);
                        flag1 = true;
                    }
                } else {
                    this.crushTime = 0;
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

    private int getCrushTime() {
        if (world == null) return 200;
        return this.world.getRecipeManager().getRecipe(ModRecipeTypes.CRUSHING, this, this.world)
                .map(CrusherRecipe::getCrushTime).orElse(200);
    }

    private boolean canCrush(@Nullable CrusherRecipe recipe) {
        if (!this.stacks.get(0).isEmpty() && recipe != null && this.redstoneMode.canRun(this)) {
            ItemStack recipeOutputStack = recipe.getRecipeOutput();
            ItemStack recipeSecondOutputStack = recipe.getSecondRecipeOutput();
            if(!recipeSecondOutputStack.isEmpty()) {
                if (recipeOutputStack.isEmpty()) {
                    return false;
                } else {
                    ItemStack crusherOutputStack = this.stacks.get(1);
                    ItemStack crusherSecondOutputStack = this.stacks.get(2);
                    if (crusherOutputStack.isEmpty() && crusherSecondOutputStack.isEmpty()) {
                        return true;
                    } else if (!crusherOutputStack.isItemEqual(recipeOutputStack) || !crusherSecondOutputStack.isItemEqual(recipeSecondOutputStack)) {
                        return false;
                    } else if (crusherOutputStack.getCount() + recipeOutputStack.getCount() <= this.getInventoryStackLimit()
                            && crusherOutputStack.getCount() < crusherOutputStack.getMaxStackSize()
                            && crusherSecondOutputStack.getCount() + recipeSecondOutputStack.getCount() <= this.getInventoryStackLimit()
                            && crusherSecondOutputStack.getCount() < crusherSecondOutputStack.getMaxStackSize()) {
                        return true;
                    } else {
                        return crusherOutputStack.getCount() + recipeOutputStack.getCount() <= recipeOutputStack.getMaxStackSize()
                                && crusherSecondOutputStack.getCount() + recipeSecondOutputStack.getCount() <= recipeSecondOutputStack.getMaxStackSize();
                    }
                }
            } else {
                if (recipeOutputStack.isEmpty()) {
                    return false;
                } else {
                    ItemStack crusherOutputStack = this.stacks.get(1);
                    if (crusherOutputStack.isEmpty()) {
                        return true;
                    } else if (!crusherOutputStack.isItemEqual(recipeOutputStack)) {
                        return false;
                    } else if (crusherOutputStack.getCount() + recipeOutputStack.getCount() <= this.getInventoryStackLimit()
                            && crusherOutputStack.getCount() < crusherOutputStack.getMaxStackSize()) {
                        return true;
                    } else {
                        return crusherOutputStack.getCount() + recipeOutputStack.getCount() <= recipeOutputStack.getMaxStackSize();
                    }
                }
            }
        } else {
            return false;
        }
    }

    private void crushItem(@Nullable CrusherRecipe recipe) {
        if (recipe != null && this.canCrush(recipe)) {
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

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.stacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.stacks.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.crushTimeTotal = this.getCrushTime();
            this.crushTime = 0;
            this.markDirty();
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.usefulmachinery.crusher");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        return new CrusherContainer(id, playerInventory, this, this.fields);
    }

}
