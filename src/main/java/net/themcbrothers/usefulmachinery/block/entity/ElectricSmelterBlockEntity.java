package net.themcbrothers.usefulmachinery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.state.BlockState;
import net.themcbrothers.usefulmachinery.core.MachineryBlockEntities;
import net.themcbrothers.usefulmachinery.machine.RedstoneMode;
import net.themcbrothers.usefulmachinery.menu.ElectricSmelterMenu;
import org.jetbrains.annotations.Nullable;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public class ElectricSmelterBlockEntity extends AbstractMachineBlockEntity {
    private static final int RF_PER_TICK = 10; //TODO evaluate if should be in config
    private final ContainerData fields = new ContainerData() {
        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 2 -> ElectricSmelterBlockEntity.this.redstoneMode = RedstoneMode.byOrdinal(value);
                case 4 -> ElectricSmelterBlockEntity.this.processTime = value;
                case 5 -> ElectricSmelterBlockEntity.this.processTimeTotal = value;
                default -> {
                }
            }
        }

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> ElectricSmelterBlockEntity.this.getEnergyStored();
                case 1 -> ElectricSmelterBlockEntity.this.getMaxEnergyStored();
                case 2 -> ElectricSmelterBlockEntity.this.redstoneMode.ordinal();
                case 3 -> ElectricSmelterBlockEntity.this.getUpgradeSlotSize();
                case 4 -> ElectricSmelterBlockEntity.this.processTime;
                case 5 -> ElectricSmelterBlockEntity.this.processTimeTotal;
                default -> 0;
            };
        }
    };
    private final RecipeManager.CachedCheck<SingleRecipeInput, BlastingRecipe> blastingQuickCheck;
    private final RecipeManager.CachedCheck<SingleRecipeInput, SmeltingRecipe> smeltingQuickCheck;

    public ElectricSmelterBlockEntity(BlockPos pos, BlockState state) {
        super(MachineryBlockEntities.ELECTRIC_SMELTER.get(), pos, state, false);

        this.blastingQuickCheck = RecipeManager.createCheck(RecipeType.BLASTING);
        this.smeltingQuickCheck = RecipeManager.createCheck(RecipeType.SMELTING);
    }

    @Override
    public int[] getInputSlots() {
        return new int[]{0};
    }

    @Override
    public int[] getOutputSlots() {
        return new int[]{1};
    }

    @Override
    protected boolean canRun() {
        boolean canRun = this.redstoneMode.canRun(this);
        boolean hasItem = !this.getItems().get(0).isEmpty();
        boolean hasEnergy = this.getEnergyStored() >= RF_PER_TICK;

        return this.level != null && canRun && hasEnergy && hasItem;
    }

    @Override
    protected int getRecipeProcessTime() {
        if (this.level == null) {
            return 200;
        }

        AbstractCookingRecipe currentRecipe = this.getCurrentRecipe();

        if (currentRecipe == null) {
            return 200;
        }

        return this.calcProcessTime(currentRecipe.cookingTime());
    }

    @Override
    public int getContainerSize() {
        return 3;
    }

    @Override
    public ContainerData getContainerData() {
        return this.fields;
    }

    @Override
    public Component getDefaultName() {
        return TEXT_UTILS.translate("container", "electric_smelter");
    }

    public AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
        return new ElectricSmelterMenu(id, playerInventory, this, this.getUpgradeContainer(), this.getContainerData());
    }

    @Override
    public void setItem(int index, ItemStack givenStack) {
        ItemStack stack = this.getItems().get(index);
        boolean sameItem = ItemStack.isSameItemSameComponents(givenStack, stack);

        this.getItems().set(index, givenStack);

        if (givenStack.getCount() > this.getMaxStackSize()) {
            givenStack.setCount(this.getMaxStackSize());
        }

        if (index == 0 && !sameItem) {
            this.processTimeTotal = this.getRecipeProcessTime();
            this.processTime = 0;

            this.setChanged();
        }
    }

    @Override
    public void tick() {
        super.tick();

        boolean shouldLit = this.isActive(RF_PER_TICK);
        boolean shouldSave = false;

        this.receiveEnergyFromSlot(2);

        if (this.canRun() && this.level != null) {
            AbstractCookingRecipe recipe = this.getCurrentRecipe();

            // Machine kickoff
            if (!this.isActive(RF_PER_TICK) && this.canProcess(recipe)) {
                this.energyStorage.consumeEnergy(RF_PER_TICK);
                this.processTime++;

                shouldSave = true;
            }
            // Machine is already running
            else if (this.isActive(RF_PER_TICK) && this.canProcess(recipe)) {
                this.energyStorage.consumeEnergy(RF_PER_TICK);
                this.processTime++;

                if (this.processTime == this.processTimeTotal) {
                    this.processTime = 0;
                    this.processTimeTotal = this.getRecipeProcessTime();

                    this.processItem(recipe);

                    shouldSave = true;
                }
            }
            // Machine is active but invalid recipe
            else {
                this.processTime = 0;
            }
        }

        this.sendUpdate(shouldLit);

        if (shouldSave) {
            this.setChanged();
        }
    }

    private SingleRecipeInput getRecipeInput() {
        return new SingleRecipeInput(this.getItem(0));
    }

    @Nullable
    private AbstractCookingRecipe getCurrentRecipe() {
        if (!(level instanceof ServerLevel serverLevel)) {
            return null;
        }

        RecipeHolder<BlastingRecipe> blastingRecipeHolder = this.blastingQuickCheck.getRecipeFor(this.getRecipeInput(), serverLevel).orElse(null);
        RecipeHolder<SmeltingRecipe> smeltingRecipeHolder = this.smeltingQuickCheck.getRecipeFor(this.getRecipeInput(), serverLevel).orElse(null);

        if (blastingRecipeHolder != null) {
            return blastingRecipeHolder.value();
        } else if (smeltingRecipeHolder != null) {
            return smeltingRecipeHolder.value();
        }

        return null;
    }

    private boolean canProcess(@Nullable AbstractCookingRecipe recipe) {
        if (recipe != null && this.level != null) {
            ItemStack recipeOutputStack = recipe.assemble(this.getRecipeInput());

            if (recipeOutputStack.isEmpty()) {
                return false;
            } else {
                ItemStack machineOutputStack = this.getItems().get(1);

                if (machineOutputStack.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItem(machineOutputStack, recipeOutputStack)) {
                    return false;
                } else {
                    boolean isMachineOutputSlotCountPossible = machineOutputStack.getCount() + recipeOutputStack.getCount() <= this.getMaxStackSize();
                    boolean isMachineOutputStackCountPossible = machineOutputStack.getCount() + recipeOutputStack.getCount() <= machineOutputStack.getMaxStackSize();
                    boolean isRecipeOutputStackCountPossible = machineOutputStack.getCount() + recipeOutputStack.getCount() <= recipeOutputStack.getMaxStackSize();

                    return (isMachineOutputSlotCountPossible && isMachineOutputStackCountPossible) || isRecipeOutputStackCountPossible;
                }
            }
        } else {
            return false;
        }
    }

    private void processItem(@Nullable AbstractCookingRecipe recipe) {
        if (recipe != null && this.level != null) {
            ItemStack resultStack = recipe.assemble(this.getRecipeInput());
            ItemStack inputSlot = this.getItems().get(0);
            ItemStack outputSlot = this.getItems().get(1);

            if (outputSlot.isEmpty()) {
                this.getItems().set(1, resultStack.copy());
            } else if (ItemStack.isSameItem(outputSlot, resultStack)) {
                outputSlot.grow(resultStack.getCount());
            }

            inputSlot.shrink(1);
        }
    }
}
