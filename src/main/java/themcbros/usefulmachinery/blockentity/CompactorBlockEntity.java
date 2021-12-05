package themcbros.usefulmachinery.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import themcbros.usefulmachinery.container.CompactorContainer;
import themcbros.usefulmachinery.init.ModTileEntities;
import themcbros.usefulmachinery.machine.CompactorMode;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.recipes.CompactingRecipe;
import themcbros.usefulmachinery.recipes.ModRecipeTypes;
import themcbros.usefulmachinery.util.TextUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CompactorBlockEntity extends AbstractMachineBlockEntity {
    private static final int RF_PER_TICK = 15;

    private final ContainerData fields = new ContainerData() {
        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4 -> CompactorBlockEntity.this.redstoneMode = RedstoneMode.byIndex(value);
                case 5 -> CompactorBlockEntity.this.processTime = value;
                case 6 -> CompactorBlockEntity.this.processTimeTotal = value;
                case 7 -> CompactorBlockEntity.this.compactorMode = CompactorMode.byIndex(value);
            }
        }

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> CompactorBlockEntity.this.getEnergyStored() & 0xFFFF;
                case 1 -> (CompactorBlockEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2 -> CompactorBlockEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3 -> (CompactorBlockEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4 -> CompactorBlockEntity.this.redstoneMode.ordinal();
                case 5 -> CompactorBlockEntity.this.processTime;
                case 6 -> CompactorBlockEntity.this.processTimeTotal;
                case 7 -> CompactorBlockEntity.this.compactorMode.getIndex();
                default -> 0;
            };
        }
    };

    public CompactorMode compactorMode = CompactorMode.PLATE;

    public CompactorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModTileEntities.COMPACTOR, blockPos, blockState, false);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);

        if (this.compactorMode != CompactorMode.PLATE) compound.putInt("Mode", this.compactorMode.getIndex());
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("Mode", Tag.TAG_INT))
            this.compactorMode = CompactorMode.byIndex(compound.getInt("Mode"));

        super.load(compound);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack itemstack = this.stacks.get(index);
        boolean flag = !stack.isEmpty() && stack.sameItem(itemstack) && ItemStack.isSameItemSameTags(stack, itemstack);
        this.stacks.set(index, stack);
        if (stack.getCount() > this.getMaxEnergyStored()) {
            stack.setCount(this.getMaxEnergyStored());
        }

        if (index == 0 && !flag) {
            this.processTimeTotal = this.getProcessTime();
            this.processTime = 0;
            this.setChanged();
        }
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
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return index != 1;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 1;
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return TextUtils.translate("container", "compactor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new CompactorContainer(id, playerInventory, this, this.fields);
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
            this.receiveEnergyFromSlot(2);

            if (this.isActive() || this.getEnergyStored() >= RF_PER_TICK && !this.stacks.get(0).isEmpty()) {
                CompactingRecipe recipe = this.level.getRecipeManager().getRecipeFor(ModRecipeTypes.COMPACTING, this, this.level).orElse(null);

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
            this.setChanged();
        }
    }

    private int getProcessTime() {
        if (level == null) return 200;
        return this.level.getRecipeManager().getRecipeFor(ModRecipeTypes.COMPACTING, this, this.level)
                .map(CompactingRecipe::getProcessTime).orElse(200);
    }

    private boolean canProcess(@Nullable CompactingRecipe recipeIn) {
        if (!this.stacks.get(0).isEmpty() && recipeIn != null && this.redstoneMode.canRun(this) && recipeIn.getCompactorMode().equals(this.compactorMode)) {
            ItemStack itemstack = recipeIn.getResultItem();

            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.stacks.get(1);

                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.sameItem(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getMaxEnergyStored() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
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
            ItemStack itemstack1 = recipe.getResultItem();
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