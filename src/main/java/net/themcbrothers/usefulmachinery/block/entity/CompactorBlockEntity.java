package net.themcbrothers.usefulmachinery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.themcbrothers.usefulmachinery.block.entity.extension.Compactor;
import net.themcbrothers.usefulmachinery.core.MachineryBlockEntities;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeTypes;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;
import net.themcbrothers.usefulmachinery.machine.RedstoneMode;
import net.themcbrothers.usefulmachinery.menu.CompactorMenu;
import net.themcbrothers.usefulmachinery.recipe.CompactingRecipe;
import org.jetbrains.annotations.Nullable;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;
import static net.themcbrothers.usefulmachinery.core.MachineryDataComponentTypes.MODE;

public class CompactorBlockEntity extends AbstractMachineBlockEntity implements Compactor {
    private static final int RF_PER_TICK = 15;
    private final ContainerData fields = new ContainerData() {
        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 2 -> CompactorBlockEntity.this.redstoneMode = RedstoneMode.byOrdinal(value);
                case 4 -> CompactorBlockEntity.this.processTime = value;
                case 5 -> CompactorBlockEntity.this.processTimeTotal = value;
                case 6 -> CompactorBlockEntity.this.compactorMode = CompactorMode.byOrdinal(value);
                default -> {
                }
            }
        }

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> CompactorBlockEntity.this.getEnergyStored();
                case 1 -> CompactorBlockEntity.this.getMaxEnergyStored();
                case 2 -> CompactorBlockEntity.this.redstoneMode.ordinal();
                case 3 -> CompactorBlockEntity.this.getUpgradeSlotSize();
                case 4 -> CompactorBlockEntity.this.processTime;
                case 5 -> CompactorBlockEntity.this.processTimeTotal;
                case 6 -> CompactorBlockEntity.this.compactorMode.ordinal();
                default -> 0;
            };
        }
    };
    public CompactorMode compactorMode = CompactorMode.PLATE;

    public CompactorBlockEntity(BlockPos pos, BlockState state) {
        super(MachineryBlockEntities.COMPACTOR.get(), pos, state, false);
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

        return this.calcProcessTime(this.level.getRecipeManager()
                .getRecipeFor(MachineryRecipeTypes.COMPACTING.get(), this, this.level)
                .map(RecipeHolder::value)
                .map(CompactingRecipe::compactTime)
                .orElse(200));
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput input) {
        super.applyImplicitComponents(input);

        CompactorMode mode = input.get(MODE.get());

        if (mode != null) {
            this.compactorMode = mode;
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);

        builder.set(MODE.get(), this.compactorMode);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        super.removeComponentsFromTag(tag);

        tag.remove("Mode");
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
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);

        if (this.compactorMode != CompactorMode.PLATE) {
            compound.putInt("Mode", this.compactorMode.ordinal());
        }
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);

        if (compound.contains("Mode")) {
            this.compactorMode = CompactorMode.byOrdinal(compound.getInt("Mode"));
        }
    }

    @Override
    public CompactorMode getMode() {
        return this.compactorMode;
    }

    @Override
    public void setMode(CompactorMode mode) {
        this.compactorMode = mode;

        this.setChanged();
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
        return TEXT_UTILS.translate("container", "compactor");
    }

    public AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
        return new CompactorMenu(id, playerInventory, this, this.upgradeContainer, this.fields);
    }

    @Override
    public void tick() {
        super.tick();

        boolean shouldLit = this.isActive(RF_PER_TICK);
        boolean shouldSave = false;

        this.receiveEnergyFromSlot(2);

        if (this.canRun() && this.level != null) {
            RecipeHolder<CompactingRecipe> recipe = this.level.getRecipeManager()
                    .getRecipeFor(MachineryRecipeTypes.COMPACTING.get(), this, this.level)
                    .orElse(null);

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

    private boolean canProcess(@Nullable RecipeHolder<CompactingRecipe> recipe) {
        if (recipe != null && recipe.value().mode().equals(this.compactorMode) && this.level != null) {
            ItemStack recipeOutputStack = recipe.value().getResultItem(this.level.registryAccess());

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

    private void processItem(@Nullable RecipeHolder<CompactingRecipe> recipe) {
        if (recipe != null && this.level != null) {
            ItemStack machineInputStack = this.getItems().get(0);
            ItemStack machineOutputStack = this.getItems().get(1);
            ItemStack recipeResultStack = recipe.value().getResultItem(this.level.registryAccess());

            if (machineOutputStack.isEmpty()) {
                this.getItems().set(1, recipeResultStack.copy());
            } else if (ItemStack.isSameItem(machineOutputStack, recipeResultStack)) {
                machineOutputStack.grow(recipeResultStack.getCount());
            }

            machineInputStack.shrink(recipe.value().sizedIngredient().count());
        }
    }
}
