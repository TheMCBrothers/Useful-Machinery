package themcbros.usefulmachinery.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import themcbros.usefulmachinery.container.LavaGeneratorContainer;
import themcbros.usefulmachinery.init.MachineryBlockEntities;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.util.TextUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LavaGeneratorBlockEntity extends AbstractMachineBlockEntity {
    public static final int TANK_CAPACITY = 4000; // TODO config
    public static final int TICKS_PER_MB = 10; // TODO config
    public static final int MB_PER_USE = 10; // TODO config
    public static final int RF_PER_TICK = 120; // TODO config

    private final ContainerData fields = new ContainerData() {
        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4 -> LavaGeneratorBlockEntity.this.redstoneMode = RedstoneMode.byIndex(value);
                case 5 -> LavaGeneratorBlockEntity.this.burnTime = value;
                default -> {
                }
            }
        }

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> LavaGeneratorBlockEntity.this.getEnergyStored() & 0xFFFF;
                case 1 -> (LavaGeneratorBlockEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2 -> LavaGeneratorBlockEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3 -> (LavaGeneratorBlockEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4 -> LavaGeneratorBlockEntity.this.redstoneMode.ordinal();
                case 5 -> LavaGeneratorBlockEntity.this.burnTime;
                case 6 -> LavaGeneratorBlockEntity.this.lavaTank.getFluidAmount();
                case 7 -> LavaGeneratorBlockEntity.this.lavaTank.getCapacity();
                case 8 -> Registry.FLUID.getId(LavaGeneratorBlockEntity.this.lavaTank.getFluid().getFluid());
                default -> 0;
            };
        }
    };

    private int burnTime;
    private final FluidTank lavaTank;

    public LavaGeneratorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(MachineryBlockEntities.LAVA_GENERATOR, blockPos, blockState, true);
        this.lavaTank = new FluidTank(TANK_CAPACITY, fluidStack -> fluidStack.getFluid().is(FluidTags.LAVA));
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);

        compound.putInt("BurnTime", this.burnTime);

        if (!this.lavaTank.getFluid().isEmpty())
            compound.put("Tank", this.lavaTank.writeToNBT(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag compound) {
        this.burnTime = compound.getInt("BurnTime");

        if (compound.contains("Tank", Tag.TAG_COMPOUND))
            this.lavaTank.readFromNBT(compound.getCompound("Tank"));

        super.load(compound);
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
    public boolean canPlaceItem(int index, ItemStack stack) {
        return !stack.isEmpty() && index == 0 && stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                .map(handler -> handler.getFluidInTank(0).getFluid().is(FluidTags.LAVA)).orElse(false);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 1;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.canPlaceItem(index, itemStackIn);
    }

    @Override
    public int getContainerSize() {
        return 3;
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return TextUtils.translate("container", "lava_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new LavaGeneratorContainer(id, playerInventory, this, this.fields);
    }

    @Override
    public void tick() {
        final ItemStack bucketStack = this.stacks.get(0);

        if (!bucketStack.isEmpty()) {
            FluidActionResult result = FluidUtil.tryEmptyContainer(bucketStack, this.lavaTank, FluidAttributes.BUCKET_VOLUME, null, true);

            if (result.isSuccess()) {
                ItemStack outputSlotStack = this.stacks.get(1);
                ItemStack resultStack = result.getResult();

                if (resultStack.sameItem(outputSlotStack) && resultStack.getMaxStackSize() > 1 && outputSlotStack.getCount() <= outputSlotStack.getMaxStackSize() - resultStack.getCount()) {
                    outputSlotStack.grow(resultStack.getCount());
                    bucketStack.shrink(1);
                } else if (outputSlotStack.isEmpty()) {
                    this.stacks.set(1, resultStack);

                    bucketStack.shrink(1);
                }
            }
        }

        if (this.canRun()) {
            if (this.burnTime <= 0 && this.hasFuel()) {
                consumeFuel();
                sendUpdate(true);
            }
        }

        if (this.burnTime > 0) {
            --this.burnTime;
            this.energyStorage.modifyEnergyStored(RF_PER_TICK);
        }

        super.tick();
        this.sendEnergy();
    }

    private boolean hasFuel() {
        return this.lavaTank.getFluidAmount() >= MB_PER_USE;
    }

    private boolean canRun() {
        return this.level != null && this.redstoneMode.canRun(this)
                && this.energyStorage.getEnergyStored() <= this.energyStorage.getMaxEnergyStored() - RF_PER_TICK;
    }

    private void consumeFuel() {
        FluidStack fluid = this.lavaTank.drain(MB_PER_USE, IFluidHandler.FluidAction.EXECUTE);
        this.burnTime = TICKS_PER_MB * fluid.getAmount();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> this.lavaTank).cast();
        }
        return super.getCapability(cap, side);
    }
}
