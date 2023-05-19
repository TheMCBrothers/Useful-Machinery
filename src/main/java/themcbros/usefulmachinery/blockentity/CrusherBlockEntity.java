package themcbros.usefulmachinery.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import themcbros.usefulmachinery.init.MachineryBlockEntities;
import themcbros.usefulmachinery.init.MachineryItems;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.menu.CrusherMenu;
import themcbros.usefulmachinery.recipes.CrushingRecipe;
import themcbros.usefulmachinery.recipes.MachineryRecipeTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CrusherBlockEntity extends AbstractMachineBlockEntity {
    private static final int RF_PER_TICK = 10;
    private double efficiencyAdditionalChance;
    private double precisionAdditionalChance;
    private final ContainerData fields = new ContainerData() {
        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4 -> CrusherBlockEntity.this.redstoneMode = RedstoneMode.byIndex(value);
                case 6 -> CrusherBlockEntity.this.processTime = value;
                case 7 -> CrusherBlockEntity.this.processTimeTotal = value;
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
                case 5 -> CrusherBlockEntity.this.getUpgradeSlotSize();
                case 6 -> CrusherBlockEntity.this.processTime;
                case 7 -> CrusherBlockEntity.this.processTimeTotal;
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
                CrushingRecipe crushingRecipe = this.level.getRecipeManager().getRecipeFor(MachineryRecipeTypes.CRUSHING.get(), this, this.level).orElse(null);

                if (crushingRecipe != null) {
                    int efficiencyUpgradeCount = 0;
                    int precisionUpgradeCount = 0;

                    for (int i = 0; i < this.upgradeContainer.getContainerSize(); i++) {
                        ItemStack stack = this.upgradeContainer.getItem(i);
                        if (stack.is(MachineryItems.EFFICIENCY_UPGRADE.get()) && crushingRecipe.supportsUpgrade(stack)) {
                            efficiencyUpgradeCount += stack.getCount();
                        }
                        if (stack.is(MachineryItems.PRECISION_UPGRADE.get()) && crushingRecipe.supportsUpgrade(stack)) {
                            precisionUpgradeCount += stack.getCount();
                        }
                    }

                    this.efficiencyAdditionalChance = 0.0625 * efficiencyUpgradeCount;
                    this.precisionAdditionalChance = 0.0625 * precisionUpgradeCount;

                }

                if (!this.isActive() && this.canCrush(crushingRecipe)) {
                    this.energyStorage.consumeEnergy(RF_PER_TICK);
                    processTime++;
                }

                if (this.isActive() && this.canCrush(crushingRecipe)) {
                    this.processTime++;
                    this.energyStorage.consumeEnergy(RF_PER_TICK);

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
        if (level == null) {
            return 200;
        }
        return calcProcessTime(this.level.getRecipeManager().getRecipeFor(MachineryRecipeTypes.CRUSHING.get(), this, this.level).map(CrushingRecipe::getCrushTime).orElse(200));
    }

    private boolean canCrush(@Nullable CrushingRecipe recipe) {
        if (!this.stacks.get(0).isEmpty() && recipe != null && this.redstoneMode.canRun(this)) {
            ItemStack recipeOutputStack = recipe.getResultItem(this.level.registryAccess());
            ItemStack recipeSecondOutputStack = recipe.getSecondRecipeOutput();
            int efficiencyAdditionalCount = 0;
            int precisionAdditionalCount = 0;

            if (this.efficiencyAdditionalChance > 0.5) {
                efficiencyAdditionalCount += 2;
            } else if (this.efficiencyAdditionalChance <= 0.5 && this.efficiencyAdditionalChance != 0) {
                efficiencyAdditionalCount += 1;
            }

            if (this.precisionAdditionalChance > 0) {
                precisionAdditionalCount += 1;
            }

            if (!recipeSecondOutputStack.isEmpty()) {
                if (recipeOutputStack.isEmpty()) {
                    return false;
                } else {
                    ItemStack crusherOutputStack = this.stacks.get(1);
                    ItemStack crusherSecondOutputStack = this.stacks.get(2);

                    if (this.precisionAdditionalChance == 1) {
                        recipeOutputStack = ItemStack.EMPTY;
                    }

                    if (crusherOutputStack.isEmpty() && crusherSecondOutputStack.isEmpty()) {
                        return true;
                    } else if (crusherOutputStack.sameItem(recipeOutputStack) && crusherOutputStack.getCount() + recipeOutputStack.getCount() + efficiencyAdditionalCount <= recipeOutputStack.getMaxStackSize() && crusherSecondOutputStack.isEmpty()) {
                        return true;
                    } else if (crusherSecondOutputStack.sameItem(recipeSecondOutputStack) && crusherSecondOutputStack.getCount() + recipeSecondOutputStack.getCount() + precisionAdditionalCount <= recipeSecondOutputStack.getMaxStackSize() && crusherOutputStack.isEmpty()) {
                        return true;
                    } else if (!crusherOutputStack.is(recipeOutputStack.getItem()) || !crusherSecondOutputStack.is(recipeSecondOutputStack.getItem())) {
                        return false;
                    } else if (crusherOutputStack.getCount() + recipeOutputStack.getCount() + efficiencyAdditionalCount <= this.getMaxStackSize() && crusherOutputStack.getCount() < crusherOutputStack.getMaxStackSize() && crusherSecondOutputStack.getCount() + recipeSecondOutputStack.getCount() + precisionAdditionalCount <= this.getMaxStackSize() && crusherSecondOutputStack.getCount() < crusherSecondOutputStack.getMaxStackSize()) {
                        return true;
                    } else {
                        return crusherOutputStack.getCount() + recipeOutputStack.getCount() + efficiencyAdditionalCount <= recipeOutputStack.getMaxStackSize() && crusherSecondOutputStack.getCount() + recipeSecondOutputStack.getCount() + precisionAdditionalCount <= recipeSecondOutputStack.getMaxStackSize();
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
                    } else if (crusherOutputStack.getCount() + recipeOutputStack.getCount() + efficiencyAdditionalCount <= this.getMaxStackSize() && crusherOutputStack.getCount() < crusherOutputStack.getMaxStackSize()) {
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
            ItemStack recipeResultItem = recipe.getResultItem(this.level.registryAccess());
            ItemStack outputSlot = this.stacks.get(1);
            ItemStack secondaryRecipeResultItem = recipe.getSecondRecipeOutput();
            ItemStack secondaryOutputSlot = this.stacks.get(2);
            float secondaryChance = recipe.getSecondaryChance();


            if (this.precisionAdditionalChance != 1) {
                if (outputSlot.isEmpty()) {
                    this.stacks.set(1, recipeResultItem.copy());
                    outputSlot = this.stacks.get(1);
                } else if (outputSlot.getItem() == recipeResultItem.getItem()) {
                    outputSlot.grow(recipeResultItem.getCount());
                }

                if (this.efficiencyAdditionalChance == 1) {
                    outputSlot.grow(recipeResultItem.getCount());
                } else if (this.efficiencyAdditionalChance < 0.5) {
                    if (Math.random() <= this.efficiencyAdditionalChance) {
                        outputSlot.grow(1);
                    }
                } else if (this.efficiencyAdditionalChance == 0.5) {
                    outputSlot.grow(1);
                } else {
                    outputSlot.grow(1);
                    if (Math.random() <= (this.efficiencyAdditionalChance - 0.5)) {
                        outputSlot.grow(1);
                    }
                }
            }

            if (!secondaryRecipeResultItem.isEmpty() && (Math.random() <= secondaryChance || this.precisionAdditionalChance == 1) && this.efficiencyAdditionalChance != 1) {
                if (secondaryOutputSlot.isEmpty()) {
                    this.stacks.set(2, secondaryRecipeResultItem.copy());
                    secondaryOutputSlot = this.stacks.get(2);
                } else if (secondaryOutputSlot.getItem() == secondaryRecipeResultItem.getItem()) {
                    secondaryOutputSlot.grow(secondaryRecipeResultItem.getCount());
                }

                if (this.precisionAdditionalChance == 1) {
                    secondaryOutputSlot.grow(secondaryRecipeResultItem.getCount());
                } else if (this.precisionAdditionalChance < 0.5) {
                    if (Math.random() <= this.precisionAdditionalChance) {
                        secondaryOutputSlot.grow(secondaryRecipeResultItem.getCount());
                    }
                } else if (this.precisionAdditionalChance == 0.5) {
                    secondaryOutputSlot.grow(secondaryRecipeResultItem.getCount());
                } else {
                    secondaryOutputSlot.grow(secondaryRecipeResultItem.getCount());
                    if (Math.random() <= this.precisionAdditionalChance - 0.5) {
                        secondaryOutputSlot.grow(secondaryRecipeResultItem.getCount());
                    }
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
        return Component.translatable("container.usefulmachinery.crusher");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new CrusherMenu(id, playerInventory, this, this.getUpgradeContainer(), this.getContainerData());
    }

    @Override
    public ContainerData getContainerData() {
        return this.fields;
    }
}
