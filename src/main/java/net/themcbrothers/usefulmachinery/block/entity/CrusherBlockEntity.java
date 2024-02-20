package net.themcbrothers.usefulmachinery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.themcbrothers.usefulmachinery.core.MachineryBlockEntities;
import net.themcbrothers.usefulmachinery.core.MachineryItems;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeTypes;
import net.themcbrothers.usefulmachinery.machine.RedstoneMode;
import net.themcbrothers.usefulmachinery.menu.CrusherMenu;
import net.themcbrothers.usefulmachinery.recipe.CrushingRecipe;
import org.jetbrains.annotations.Nullable;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public class CrusherBlockEntity extends AbstractMachineBlockEntity {
    private static final int RF_PER_TICK = 10; //TODO evaluate if should be in config
    private double efficiencyAdditionalChance;
    private double precisionAdditionalChance;
    private final ContainerData fields = new ContainerData() {
        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 2 -> CrusherBlockEntity.this.redstoneMode = RedstoneMode.byOrdinal(value);
                case 4 -> CrusherBlockEntity.this.processTime = value;
                case 5 -> CrusherBlockEntity.this.processTimeTotal = value;
                default -> {
                }
            }
        }

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> CrusherBlockEntity.this.getEnergyStored();
                case 1 -> CrusherBlockEntity.this.getMaxEnergyStored();
                case 2 -> CrusherBlockEntity.this.redstoneMode.ordinal();
                case 3 -> CrusherBlockEntity.this.getUpgradeSlotSize();
                case 4 -> CrusherBlockEntity.this.processTime;
                case 5 -> CrusherBlockEntity.this.processTimeTotal;
                default -> 0;
            };
        }
    };

    public CrusherBlockEntity(BlockPos pos, BlockState state) {
        super(MachineryBlockEntities.CRUSHER.get(), pos, state, false);
    }

    @Override
    protected int[] getInputSlots() {
        return new int[]{0};
    }

    @Override
    protected int[] getOutputSlots() {
        return new int[]{1, 2};
    }

    @Override
    protected boolean canRun() {
        boolean canRun = this.redstoneMode.canRun(this);
        boolean hasItem = !this.stacks.get(0).isEmpty();
        boolean hasEnergy = this.getEnergyStored() >= RF_PER_TICK;

        return this.level != null && canRun && hasEnergy && hasItem;
    }

    @Override
    protected int getRecipeProcessTime() {
        if (this.level == null) {
            return 200;
        }

        return this.calcProcessTime(this.level.getRecipeManager()
                .getRecipeFor(MachineryRecipeTypes.CRUSHING.get(), this, this.level)
                .map(RecipeHolder::value)
                .map(CrushingRecipe::crushTime)
                .orElse(200)
        );
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    public ContainerData getContainerData() {
        return this.fields;
    }

    @Override
    public Component getDisplayName() {
        return TEXT_UTILS.translate("container", "crusher");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new CrusherMenu(id, playerInventory, this, this.getUpgradeContainer(), this.getContainerData());
    }

    @Override
    public void tick() {
        boolean shouldLit = this.isActive(RF_PER_TICK);
        boolean shouldSave = false;

        this.receiveEnergyFromSlot(3);

        if (this.canRun() && this.level != null) {
            RecipeHolder<CrushingRecipe> recipe = this.level.getRecipeManager()
                    .getRecipeFor(MachineryRecipeTypes.CRUSHING.get(), this, this.level)
                    .orElse(null);

            if (recipe != null) {
                int efficiencyUpgradeCount = this.getUpgradeCount(MachineryItems.EFFICIENCY_UPGRADE, stack -> recipe.value().supportUpgrade(stack));
                int precisionUpgradeCount = this.getUpgradeCount(MachineryItems.PRECISION_UPGRADE, stack -> recipe.value().supportUpgrade(stack));

                this.efficiencyAdditionalChance = 0.0625 * efficiencyUpgradeCount;
                this.precisionAdditionalChance = 0.0625 * precisionUpgradeCount;
            }

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

        super.tick();
    }

    @Override
    public void setItem(int index, ItemStack givenStack) {
        ItemStack stack = this.stacks.get(index);
        boolean sameItem = ItemStack.isSameItemSameTags(givenStack, stack);

        this.stacks.set(index, givenStack);

        if (givenStack.getCount() > this.getMaxStackSize()) {
            givenStack.setCount(this.getMaxStackSize());
        }

        if (index == 0 && !sameItem) {
            this.processTimeTotal = this.getRecipeProcessTime();
            this.processTime = 0;

            this.setChanged();
        }
    }

    private boolean canProcess(@Nullable RecipeHolder<CrushingRecipe> recipe) {
        if (recipe != null && this.level != null) {
            ItemStack recipePrimaryOutputStack = recipe.value().getResultItem(this.level.registryAccess());
            ItemStack recipeSecondOutputStack = recipe.value().secondaryResult();

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
                if (recipePrimaryOutputStack.isEmpty()) {
                    return false;
                } else {
                    ItemStack machinePrimaryOutputStack = this.stacks.get(1);
                    ItemStack machineSecondOutputStack = this.stacks.get(2);

                    if (this.precisionAdditionalChance == 1) {
                        recipePrimaryOutputStack = ItemStack.EMPTY;
                    }

                    boolean areMachineOutputStacksEmpty = machinePrimaryOutputStack.isEmpty() && machineSecondOutputStack.isEmpty();

                    if (areMachineOutputStacksEmpty) {
                        return true;
                    } else {
                        boolean isRecipePrimaryOutputStackCountPossible = machinePrimaryOutputStack.getCount() + recipePrimaryOutputStack.getCount() + efficiencyAdditionalCount <= recipePrimaryOutputStack.getMaxStackSize();
                        boolean isRecipeSecondaryOutputStackCountPossible = machineSecondOutputStack.getCount() + recipeSecondOutputStack.getCount() + precisionAdditionalCount <= recipeSecondOutputStack.getMaxStackSize();

                        if (ItemStack.isSameItem(machinePrimaryOutputStack, recipePrimaryOutputStack) && isRecipePrimaryOutputStackCountPossible && machineSecondOutputStack.isEmpty()) {
                            return true;
                        } else if (ItemStack.isSameItem(machineSecondOutputStack, recipeSecondOutputStack) && isRecipeSecondaryOutputStackCountPossible && machinePrimaryOutputStack.isEmpty()) {
                            return true;
                        } else if (!machinePrimaryOutputStack.is(recipePrimaryOutputStack.getItem()) || !machineSecondOutputStack.is(recipeSecondOutputStack.getItem())) {
                            return false;
                        } else {
                            boolean isMachinePrimaryOutputSlotCountPossible = machinePrimaryOutputStack.getCount() + recipePrimaryOutputStack.getCount() + efficiencyAdditionalCount <= this.getMaxStackSize();
                            boolean isMachinePrimaryOutputStackCountPossible = machinePrimaryOutputStack.getCount() < machinePrimaryOutputStack.getMaxStackSize();
                            boolean isMachineSecondaryOutputSlotCountPossible = machineSecondOutputStack.getCount() + recipeSecondOutputStack.getCount() + precisionAdditionalCount <= this.getMaxStackSize();
                            boolean isMachineSecondaryOutputStackCountPossible = machineSecondOutputStack.getCount() < machineSecondOutputStack.getMaxStackSize();

                            boolean areMachineSlotsAndStacksCountPossible = isMachinePrimaryOutputSlotCountPossible && isMachinePrimaryOutputStackCountPossible && isMachineSecondaryOutputSlotCountPossible && isMachineSecondaryOutputStackCountPossible;
                            boolean areRecipeOutputsCountPossible = isRecipePrimaryOutputStackCountPossible && isRecipeSecondaryOutputStackCountPossible;

                            return areMachineSlotsAndStacksCountPossible || areRecipeOutputsCountPossible;
                        }
                    }
                }
            } else {
                if (recipePrimaryOutputStack.isEmpty()) {
                    return false;
                } else {
                    ItemStack machinePrimaryOutputStack = this.stacks.get(1);

                    if (machinePrimaryOutputStack.isEmpty()) {
                        return true;
                    } else if (!ItemStack.isSameItem(machinePrimaryOutputStack, recipePrimaryOutputStack)) {
                        return false;
                    } else {
                        boolean isMachinePrimaryOutputSlotCountPossible = machinePrimaryOutputStack.getCount() + recipePrimaryOutputStack.getCount() + efficiencyAdditionalCount <= this.getMaxStackSize();
                        boolean isMachinePrimaryOutputStackCountPossible = machinePrimaryOutputStack.getCount() < machinePrimaryOutputStack.getMaxStackSize();
                        boolean isRecipePrimaryOutputStackCountPossible = machinePrimaryOutputStack.getCount() + recipePrimaryOutputStack.getCount() <= recipePrimaryOutputStack.getMaxStackSize();

                        return (isMachinePrimaryOutputSlotCountPossible && isMachinePrimaryOutputStackCountPossible) || isRecipePrimaryOutputStackCountPossible;
                    }
                }
            }
        } else {
            return false;
        }
    }

    private void processItem(@Nullable RecipeHolder<CrushingRecipe> recipe) {
        if (recipe != null && this.level != null) {
            ItemStack primaryResultStack = recipe.value().getResultItem(this.level.registryAccess());
            ItemStack secondaryResultStack = recipe.value().secondaryResult();
            ItemStack inputSlot = this.stacks.get(0);
            ItemStack primaryOutputSlot = this.stacks.get(1);
            ItemStack secondaryOutputSlot = this.stacks.get(2);
            float secondaryChance = recipe.value().secondaryChance();

            // Checking if machine not in precision mode
            if (this.precisionAdditionalChance != 1) {
                if (primaryOutputSlot.isEmpty()) {
                    this.stacks.set(1, primaryResultStack.copy());

                    primaryOutputSlot = this.stacks.get(1);
                } else if (ItemStack.isSameItem(primaryOutputSlot, primaryResultStack)) {
                    primaryOutputSlot.grow(primaryResultStack.getCount());
                }

                if (this.efficiencyAdditionalChance == 1) {
                    primaryOutputSlot.grow(primaryResultStack.getCount());
                } else if (this.efficiencyAdditionalChance < 0.5) {
                    if (Math.random() <= this.efficiencyAdditionalChance) {
                        primaryOutputSlot.grow(1);
                    }
                } else if (this.efficiencyAdditionalChance == 0.5) {
                    primaryOutputSlot.grow(1);
                } else {
                    primaryOutputSlot.grow(1);

                    if (Math.random() <= (this.efficiencyAdditionalChance - 0.5)) {
                        primaryOutputSlot.grow(1);
                    }
                }
            }

            boolean isSecondaryOutputPossible = (Math.random() <= secondaryChance || this.precisionAdditionalChance == 1);

            // Checking if machine not in efficiency mode
            if (this.efficiencyAdditionalChance != 1 && !secondaryResultStack.isEmpty() && isSecondaryOutputPossible) {
                if (secondaryOutputSlot.isEmpty()) {
                    this.stacks.set(2, secondaryResultStack.copy());

                    secondaryOutputSlot = this.stacks.get(2);
                } else if (ItemStack.isSameItem(secondaryOutputSlot, secondaryResultStack)) {
                    secondaryOutputSlot.grow(secondaryResultStack.getCount());
                }

                if (this.precisionAdditionalChance == 1) {
                    secondaryOutputSlot.grow(secondaryResultStack.getCount());
                } else if (this.precisionAdditionalChance < 0.5) {
                    if (Math.random() <= this.precisionAdditionalChance) {
                        secondaryOutputSlot.grow(secondaryResultStack.getCount());
                    }
                } else if (this.precisionAdditionalChance == 0.5) {
                    secondaryOutputSlot.grow(secondaryResultStack.getCount());
                    if (Math.random() <= (this.precisionAdditionalChance - 0.5)) {
                        secondaryOutputSlot.grow(secondaryResultStack.getCount());
                    }
                }
            }

            inputSlot.shrink(1);
        }
    }
}
