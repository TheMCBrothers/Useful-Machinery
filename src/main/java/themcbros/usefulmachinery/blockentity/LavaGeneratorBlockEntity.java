package themcbros.usefulmachinery.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import themcbros.usefulmachinery.init.MachineryBlockEntities;
import themcbros.usefulmachinery.init.MachineryItems;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.menu.LavaGeneratorMenu;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static themcbros.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public class LavaGeneratorBlockEntity extends AbstractMachineBlockEntity {
    public static final int TANK_CAPACITY = 4000; // TODO config
    public static final int TICKS_PER_MB = 10; // TODO config
    public static final int MB_PER_USE = 10; // TODO config
    public static final int BASE_TICKING_ENERGY = 150; // TODO config

    private final ContainerData fields = new ContainerData() {
        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4 -> LavaGeneratorBlockEntity.this.redstoneMode = RedstoneMode.byIndex(value);
                case 6 -> LavaGeneratorBlockEntity.this.burnTime = value;
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
                case 5 -> LavaGeneratorBlockEntity.this.getUpgradeSlotSize();
                case 6 -> LavaGeneratorBlockEntity.this.burnTime;
                case 7 -> LavaGeneratorBlockEntity.this.LAVA_TANK.getFluidAmount();
                case 8 -> LavaGeneratorBlockEntity.this.LAVA_TANK.getCapacity();
                case 9 -> BuiltInRegistries.FLUID.getId(LavaGeneratorBlockEntity.this.LAVA_TANK.getFluid().getFluid());
                default -> 0;
            };
        }
    };

    private int burnTime;
    private final FluidTank LAVA_TANK;

    public LavaGeneratorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(MachineryBlockEntities.LAVA_GENERATOR.get(), blockPos, blockState, true);
        this.LAVA_TANK = new FluidTank(TANK_CAPACITY, fluidStack -> fluidStack.getFluid().isSame(Fluids.LAVA));
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);

        compound.putInt("BurnTime", this.burnTime);

        if (!this.LAVA_TANK.getFluid().isEmpty())
            compound.put("Tank", this.LAVA_TANK.writeToNBT(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag compound) {
        this.burnTime = compound.getInt("BurnTime");

        if (compound.contains("Tank", Tag.TAG_COMPOUND)) {
            this.LAVA_TANK.readFromNBT(compound.getCompound("Tank"));
        }
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
        return !stack.isEmpty() && index == 0 && stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
                .map(handler -> handler.getFluidInTank(0).getFluid().isSame(Fluids.LAVA)).orElse(false);
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
        return TEXT_UTILS.translate("container", "lava_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new LavaGeneratorMenu(id, playerInventory, this, this.getUpgradeContainer(), this.getContainerData());
    }

    @Override
    public void tick() {
        final ItemStack bucketStack = this.stacks.get(0);

        if (!bucketStack.isEmpty()) {
            FluidActionResult result = FluidUtil.tryEmptyContainer(bucketStack, this.LAVA_TANK, FluidType.BUCKET_VOLUME, null, true);

            if (result.isSuccess()) {
                ItemStack outputSlotStack = this.stacks.get(1);
                ItemStack resultStack = result.getResult();

                if (ItemStack.isSameItem(resultStack, outputSlotStack) && resultStack.getMaxStackSize() > 1 && outputSlotStack.getCount() <= outputSlotStack.getMaxStackSize() - resultStack.getCount()) {
                    outputSlotStack.grow(resultStack.getCount());
                    bucketStack.shrink(1);
                } else if (outputSlotStack.isEmpty()) {
                    this.stacks.set(1, resultStack);

                    bucketStack.shrink(1);
                }
            }
        }

        if (this.burnTime <= 0 && this.hasFuel() && this.canRun()) {
            consumeFuel();
        }

        if (this.burnTime > 0) {
            --this.burnTime;

            int upgradeCount = 0;

            for (int i = 0; i < this.upgradeContainer.getContainerSize(); i++) {
                ItemStack stack = this.upgradeContainer.getItem(i);

                if (stack.is(MachineryItems.SUSTAINED_UPGRADE.get())) {
                    upgradeCount += stack.getCount();
                }
            }
            double multiplier = upgradeCount * BASE_TICKING_ENERGY / (double) this.upgradeContainer.getMaxStackSize();

            // generate energy
            this.energyStorage.growEnergy((int) (BASE_TICKING_ENERGY + multiplier));
        }

        super.tick();
        this.sendEnergy();
    }

    private boolean hasFuel() {
        return this.LAVA_TANK.getFluidAmount() >= MB_PER_USE;
    }

    private boolean canRun() {
        boolean canRun = this.redstoneMode.canRun(this);
        sendUpdate(canRun);
        return this.level != null && canRun && this.energyStorage.getEnergyStored() <= this.energyStorage.getMaxEnergyStored() - BASE_TICKING_ENERGY;
    }

    private void consumeFuel() {
        FluidStack fluid = this.LAVA_TANK.drain(MB_PER_USE, IFluidHandler.FluidAction.EXECUTE);
        this.burnTime = calcBurnTime(TICKS_PER_MB * fluid.getAmount());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return LazyOptional.of(() -> this.LAVA_TANK).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public ContainerData getContainerData() {
        return this.fields;
    }
}
