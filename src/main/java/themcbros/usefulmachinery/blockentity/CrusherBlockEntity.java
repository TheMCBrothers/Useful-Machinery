package themcbros.usefulmachinery.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import themcbros.usefulmachinery.container.CrusherContainer;
import themcbros.usefulmachinery.init.MachineryBlockEntities;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.recipes.CrushingRecipe;
import themcbros.usefulmachinery.recipes.MachineryRecipeTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CrusherBlockEntity extends AbstractMachineBlockEntity {
    private static final int RF_PER_TICK = 10;

    private final ContainerData fields = new ContainerData() {
        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4 -> CrusherBlockEntity.this.redstoneMode = RedstoneMode.byIndex(value);
                case 5 -> CrusherBlockEntity.this.processTime = value;
                case 6 -> CrusherBlockEntity.this.processTimeTotal = value;
            }
        }

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> CrusherBlockEntity.this.getEnergyStored() & 0xFFFF;
                case 1 -> (CrusherBlockEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2 -> CrusherBlockEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3 -> (CrusherBlockEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4 -> CrusherBlockEntity.this.redstoneMode.ordinal();
                case 5 -> CrusherBlockEntity.this.processTime;
                case 6 -> CrusherBlockEntity.this.processTimeTotal;
                default -> 0;
            };
        }
    };

    public CrusherBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(MachineryBlockEntities.CRUSHER, blockPos, blockState, false);
    }

    @Override
    int[] getInputSlots() {
        return new int[]{0};
    }

    @Override
    int[] getOutputSlots() {
        return new int[]{1, 2};
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    private boolean isActive() {
        return this.processTime > 0 && this.energyStorage.getEnergyStored() >= RF_PER_TICK;
    }

    @Override
    public void tick() {
        boolean shouldLit = this.isActive();
        boolean flag1 = false;

        assert this.level != null;
        if (!this.level.isClientSide) {

            this.receiveEnergyFromSlot(3);

            if (this.isActive() || this.energyStorage.getEnergyStored() >= RF_PER_TICK && !this.stacks.get(0).isEmpty()) {
                CrushingRecipe crushingRecipe = this.level.getRecipeManager().getRecipeFor(MachineryRecipeTypes.CRUSHING, this, this.level).orElse(null);

                if (!this.isActive() && this.canCrush(crushingRecipe)) {
                    this.energyStorage.modifyEnergyStored(-RF_PER_TICK);
                    processTime++;
                }

                if (this.isActive() && this.canCrush(crushingRecipe)) {
                    this.processTime++;
                    this.energyStorage.modifyEnergyStored(-RF_PER_TICK);

                    if (this.processTime == this.processTimeTotal) {
                        this.processTime = 0;
                        this.processTimeTotal = this.getCrushTime();

                        this.crushItem(crushingRecipe);

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
            this.setChanged();
        }
    }

    private int getCrushTime() {
        if (level == null) return 200;
        return this.level.getRecipeManager().getRecipeFor(MachineryRecipeTypes.CRUSHING, this, this.level)
                .map(CrushingRecipe::getCrushTime).orElse(200);
    }

    private boolean canCrush(@Nullable CrushingRecipe recipe) {
        if (!this.stacks.get(0).isEmpty() && recipe != null && this.redstoneMode.canRun(this)) {
            ItemStack recipeOutputStack = recipe.getResultItem();
            ItemStack recipeSecondOutputStack = recipe.getSecondRecipeOutput();

            if (!recipeSecondOutputStack.isEmpty()) {
                if (recipeOutputStack.isEmpty()) {
                    return false;
                } else {
                    ItemStack crusherOutputStack = this.stacks.get(1);
                    ItemStack crusherSecondOutputStack = this.stacks.get(2);

                    if (crusherOutputStack.isEmpty() && crusherSecondOutputStack.isEmpty()) {
                        return true;
                    } else if (!crusherOutputStack.sameItem(recipeOutputStack) || !crusherSecondOutputStack.sameItem(recipeSecondOutputStack)) {
                        return false;
                    } else if (crusherOutputStack.getCount() + recipeOutputStack.getCount() <= this.getMaxStackSize()
                            && crusherOutputStack.getCount() < crusherOutputStack.getMaxStackSize()
                            && crusherSecondOutputStack.getCount() + recipeSecondOutputStack.getCount() <= this.getMaxStackSize()
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
                    } else if (!crusherOutputStack.sameItem(recipeOutputStack)) {
                        return false;
                    } else if (crusherOutputStack.getCount() + recipeOutputStack.getCount() <= this.getMaxStackSize()
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

    private void crushItem(@Nullable CrushingRecipe recipe) {
        if (recipe != null && this.canCrush(recipe)) {
            ItemStack itemstack = this.stacks.get(0);
            ItemStack itemstack1 = recipe.getResultItem();
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
    public void setItem(int index, ItemStack stack) {
        ItemStack itemstack = this.stacks.get(index);
        boolean flag = !stack.isEmpty() && stack.sameItem(itemstack) && ItemStack.isSameItemSameTags(stack, itemstack);

        this.stacks.set(index, stack);

        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxEnergyStored());
        }

        if (index == 0 && !flag) {
            this.processTimeTotal = this.getCrushTime();
            this.processTime = 0;
            this.setChanged();
        }
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.usefulmachinery.crusher");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new CrusherContainer(id, playerInventory, this, this.fields);
    }
}
