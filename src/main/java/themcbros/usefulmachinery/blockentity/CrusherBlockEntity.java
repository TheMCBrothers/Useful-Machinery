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
                case 4 -> CrusherBlockEntity.this.setRedstoneMode(RedstoneMode.byIndex(value));
                case 5 -> CrusherBlockEntity.this.setProcessTime(value);
                case 6 -> CrusherBlockEntity.this.setProcessTimeTotal(value);
            }
        }

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> CrusherBlockEntity.this.getEnergyStorage().getEnergyStored() & 0xFFFF;
                case 1 -> (CrusherBlockEntity.this.getEnergyStorage().getEnergyStored() >> 16) & 0xFFFF;
                case 2 -> CrusherBlockEntity.this.getEnergyStorage().getMaxEnergyStored() & 0xFFFF;
                case 3 -> (CrusherBlockEntity.this.getEnergyStorage().getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4 -> CrusherBlockEntity.this.getRedstoneMode().ordinal();
                case 5 -> CrusherBlockEntity.this.getProcessTime();
                case 6 -> CrusherBlockEntity.this.getProcessTimeTotal();
                default -> 0;
            };
        }
    };

    public CrusherBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(MachineryBlockEntities.CRUSHER.get(), blockPos, blockState, false);
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
        return this.getProcessTime() > 0 && this.getEnergyStorage().getEnergyStored() >= RF_PER_TICK;
    }

    @Override
    public void tick() {
        boolean shouldLit = this.isActive();
        boolean flag1 = false;

        assert this.level != null;
        if (!this.level.isClientSide) {

            this.receiveEnergyFromSlot(3);

            if (this.isActive() || this.getEnergyStorage().getEnergyStored() >= RF_PER_TICK && !this.stacks.get(0).isEmpty()) {
                CrushingRecipe crushingRecipe = this.level.getRecipeManager().getRecipeFor(MachineryRecipeTypes.CRUSHING.get(), this, this.level).orElse(null);

                if (!this.isActive() && this.canCrush(crushingRecipe)) {
                    this.getEnergyStorage().modifyEnergyStored(-RF_PER_TICK);
                    this.setProcessTime(this.getProcessTime() + 1);
                }

                if (this.isActive() && this.canCrush(crushingRecipe)) {
                    this.setProcessTime(this.getProcessTime() + 1);
                    this.getEnergyStorage().modifyEnergyStored(-RF_PER_TICK);

                    if (this.getProcessTime() == this.getProcessTimeTotal()) {
                        this.setProcessTime(0);
                        this.setProcessTimeTotal(this.getRecipeProcessTime());

                        this.crushItem(crushingRecipe);

                        flag1 = true;
                    }
                } else {
                    this.setProcessTime(0);
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

    private int getRecipeProcessTime() {
        if (level == null) {
            return 200;
        }
        return calcProcessTime(this.level.getRecipeManager().getRecipeFor(MachineryRecipeTypes.CRUSHING.get(), this, this.level).map(CrushingRecipe::getCrushTime).orElse(200));
    }

    private boolean canCrush(@Nullable CrushingRecipe recipe) {
        if (!this.stacks.get(0).isEmpty() && recipe != null && this.getRedstoneMode().canRun(this)) {
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
                    } else if (crusherOutputStack.sameItem(recipeOutputStack) && crusherOutputStack.getCount() + recipeOutputStack.getCount() <= recipeOutputStack.getMaxStackSize() && crusherSecondOutputStack.isEmpty()) {
                        return true;
                    } else if (crusherSecondOutputStack.sameItem(recipeSecondOutputStack) && crusherSecondOutputStack.getCount() + recipeSecondOutputStack.getCount() <= recipeSecondOutputStack.getMaxStackSize() && crusherOutputStack.isEmpty()) {
                        return true;
                    } else if (!crusherOutputStack.sameItem(recipeOutputStack) || !crusherSecondOutputStack.sameItem(recipeSecondOutputStack)) {
                        return false;
                    } else if (crusherOutputStack.getCount() + recipeOutputStack.getCount() <= this.getMaxStackSize() && crusherOutputStack.getCount() < crusherOutputStack.getMaxStackSize() && crusherSecondOutputStack.getCount() + recipeSecondOutputStack.getCount() <= this.getMaxStackSize() && crusherSecondOutputStack.getCount() < crusherSecondOutputStack.getMaxStackSize()) {
                        return true;
                    } else {
                        return crusherOutputStack.getCount() + recipeOutputStack.getCount() <= recipeOutputStack.getMaxStackSize() && crusherSecondOutputStack.getCount() + recipeSecondOutputStack.getCount() <= recipeSecondOutputStack.getMaxStackSize();
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
                    } else if (crusherOutputStack.getCount() + recipeOutputStack.getCount() <= this.getMaxStackSize() && crusherOutputStack.getCount() < crusherOutputStack.getMaxStackSize()) {
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
            ItemStack inputSlot = this.stacks.get(0);
            ItemStack recipeResultItem = recipe.getResultItem();
            ItemStack outputSlot = this.stacks.get(1);
            ItemStack secondaryRecipeResultItem = recipe.getSecondRecipeOutput();
            ItemStack secondaryOutputSlot = this.stacks.get(2);
            float secondaryChance = recipe.getSecondaryChance();

            if (outputSlot.isEmpty()) {
                this.stacks.set(1, recipeResultItem.copy());
            } else if (outputSlot.getItem() == recipeResultItem.getItem()) {
                outputSlot.grow(recipeResultItem.getCount());
            }

            if (!secondaryRecipeResultItem.isEmpty() && Math.random() <= secondaryChance) {
                if (secondaryOutputSlot.isEmpty()) {
                    this.stacks.set(2, secondaryRecipeResultItem.copy());
                } else if (secondaryOutputSlot.getItem() == secondaryRecipeResultItem.getItem()) {
                    secondaryOutputSlot.grow(secondaryRecipeResultItem.getCount());
                }
            }

            inputSlot.shrink(1);
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack itemstack = this.stacks.get(index);
        boolean flag = !stack.isEmpty() && stack.sameItem(itemstack) && ItemStack.isSameItemSameTags(stack, itemstack);

        this.stacks.set(index, stack);

        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getEnergyStorage().getMaxEnergyStored());
        }

        if (index == 0 && !flag) {
            this.setProcessTimeTotal(this.getRecipeProcessTime());
            this.setProcessTime(0);
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
