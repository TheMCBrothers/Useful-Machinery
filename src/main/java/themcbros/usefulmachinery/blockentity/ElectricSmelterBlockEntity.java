package themcbros.usefulmachinery.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.container.ElectricSmelterContainer;
import themcbros.usefulmachinery.init.MachineryBlockEntities;
import themcbros.usefulmachinery.machine.RedstoneMode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElectricSmelterBlockEntity extends AbstractMachineBlockEntity {
    private static final int RF_PER_TICK = 10;

    private final ContainerData fields = new ContainerData() {
        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4 -> ElectricSmelterBlockEntity.this.setRedstoneMode(RedstoneMode.byIndex(value));
                case 5 -> ElectricSmelterBlockEntity.this.setProcessTime(value);
                case 6 -> ElectricSmelterBlockEntity.this.setProcessTimeTotal(value);
            }
        }

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> ElectricSmelterBlockEntity.this.getEnergyStorage().getEnergyStored() & 0xFFFF;
                case 1 -> (ElectricSmelterBlockEntity.this.getEnergyStorage().getEnergyStored() >> 16) & 0xFFFF;
                case 2 -> ElectricSmelterBlockEntity.this.getEnergyStorage().getMaxEnergyStored() & 0xFFFF;
                case 3 -> (ElectricSmelterBlockEntity.this.getEnergyStorage().getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4 -> ElectricSmelterBlockEntity.this.getRedstoneMode().ordinal();
                case 5 -> ElectricSmelterBlockEntity.this.getProcessTime();
                case 6 -> ElectricSmelterBlockEntity.this.getProcessTimeTotal();
                default -> 0;
            };
        }
    };

    public ElectricSmelterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(MachineryBlockEntities.ELECTRIC_SMELTER.get(), blockPos, blockState, false);
    }

    @Override
    int[] getInputSlots() {
        return new int[]{0};
    }

    @Override
    int[] getOutputSlots() {
        return new int[]{1};
    }

    @Override
    public int getContainerSize() {
        return 3;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack itemstack = this.stacks.get(index);
        boolean flag = !stack.isEmpty() && stack.sameItem(itemstack) && ItemStack.isSameItemSameTags(stack, itemstack);
        this.stacks.set(index, stack);
        if (stack.getCount() > this.getEnergyStorage().getMaxEnergyStored()) {
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
        return UsefulMachinery.TEXT_UTILS.translate("container", "electric_smelter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new ElectricSmelterContainer(id, playerInventory, this, this.fields);
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
            this.receiveEnergyFromSlot(2);

            if (this.isActive() || this.getEnergyStorage().getEnergyStored() >= RF_PER_TICK && !this.stacks.get(0).isEmpty()) {
                AbstractCookingRecipe recipe = this.level.getRecipeManager().getRecipeFor(RecipeType.BLASTING, this, this.level).orElse(null);
                if (recipe == null)
                    recipe = this.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, this, this.level).orElse(null);

                if (!this.isActive() && this.canProcess(recipe)) {
                    this.getEnergyStorage().modifyEnergyStored(-RF_PER_TICK);
                    this.setProcessTime(this.getProcessTime() + 1);
                }

                if (this.isActive() && this.canProcess(recipe)) {
                    this.getEnergyStorage().modifyEnergyStored(-RF_PER_TICK);
                    this.setProcessTime(this.getProcessTime() + 1);

                    if (this.getProcessTime() == this.getProcessTimeTotal()) {
                        this.setProcessTime(0);
                        this.setProcessTimeTotal(this.getRecipeProcessTime());

                        this.processItem(recipe);

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
        if (this.level == null) {
            return 200;
        }
        return calcProcessTime(this.level.getRecipeManager().getRecipeFor(RecipeType.BLASTING, this, this.level)
                .map(AbstractCookingRecipe::getCookingTime).orElse(this.level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, this, this.level)
                        .map(AbstractCookingRecipe::getCookingTime).orElse(200)));
    }

    private boolean canProcess(@Nullable AbstractCookingRecipe recipeIn) {
        if (!this.stacks.get(0).isEmpty() && recipeIn != null && this.getRedstoneMode().canRun(this)) {
            ItemStack itemstack = recipeIn.getResultItem();

            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.stacks.get(1);

                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.sameItem(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getEnergyStorage().getMaxEnergyStored() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
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
}
