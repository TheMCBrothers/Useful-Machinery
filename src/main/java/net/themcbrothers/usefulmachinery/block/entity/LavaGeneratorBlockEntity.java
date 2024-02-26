package net.themcbrothers.usefulmachinery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.themcbrothers.usefulmachinery.core.MachineryBlockEntities;
import net.themcbrothers.usefulmachinery.core.MachineryItems;
import net.themcbrothers.usefulmachinery.machine.RedstoneMode;
import net.themcbrothers.usefulmachinery.menu.LavaGeneratorMenu;
import org.jetbrains.annotations.Nullable;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public class LavaGeneratorBlockEntity extends AbstractMachineBlockEntity {
    public static final int TANK_CAPACITY = 4000; // TODO config
    public static final int TICKS_PER_MB = 5; // TODO config
    public static final int MB_PER_USE = 20; // TODO config
    public static final int BASE_TICKING_ENERGY = 150; // TODO evaluate if should be in config
    private int burnTime;
    private int burnTimeTotal;
    private final FluidTank lavaTank;
    private final ContainerData fields = new ContainerData() {
        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 2 -> LavaGeneratorBlockEntity.this.redstoneMode = RedstoneMode.byOrdinal(value);
                case 4 -> LavaGeneratorBlockEntity.this.burnTime = value;
                case 5 -> LavaGeneratorBlockEntity.this.burnTimeTotal = value;
                default -> {
                }
            }
        }

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> LavaGeneratorBlockEntity.this.getEnergyStored();
                case 1 -> LavaGeneratorBlockEntity.this.getMaxEnergyStored();
                case 2 -> LavaGeneratorBlockEntity.this.redstoneMode.ordinal();
                case 3 -> LavaGeneratorBlockEntity.this.getUpgradeSlotSize();
                case 4 -> LavaGeneratorBlockEntity.this.burnTime;
                case 5 -> LavaGeneratorBlockEntity.this.burnTimeTotal;
                case 6 -> LavaGeneratorBlockEntity.this.lavaTank.getFluidAmount();
                case 7 -> LavaGeneratorBlockEntity.this.lavaTank.getCapacity();
                case 8 -> BuiltInRegistries.FLUID.getId(LavaGeneratorBlockEntity.this.lavaTank.getFluid().getFluid());
                default -> 0;
            };
        }
    };

    public LavaGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(MachineryBlockEntities.LAVA_GENERATOR.get(), pos, state, true);

        this.lavaTank = new FluidTank(TANK_CAPACITY, fluidStack -> fluidStack.getFluid().isSame(Fluids.LAVA));
    }

    @Override
    public int[] getInputSlots() {
        return new int[]{0};
    }

    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    protected boolean canRun() {
        boolean canRun = this.redstoneMode.canRun(this);
        boolean canGenerate = this.energyStorage.getEnergyStored() <= this.energyStorage.getMaxEnergyStored() - BASE_TICKING_ENERGY;

        return this.level != null && canRun && canGenerate;
    }

    @Override
    public ContainerData getContainerData() {
        return this.fields;
    }

    @Override
    public int getContainerSize() {
        return 3;
    }

    @Override
    public Component getDisplayName() {
        return TEXT_UTILS.translate("container", "lava_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player pPlayer) {
        return new LavaGeneratorMenu(id, playerInventory, this, this.getUpgradeContainer(), this.getContainerData());
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);

        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("BurnTimeTotal", this.burnTimeTotal);

        if (!this.lavaTank.getFluid().isEmpty()) {
            compound.put("Tank", this.lavaTank.writeToNBT(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag compound) {
        this.burnTime = compound.getInt("BurnTime");
        this.burnTimeTotal = compound.getInt("BurnTimeTotal");

        if (compound.contains("Tank", CompoundTag.TAG_COMPOUND)) {
            this.lavaTank.readFromNBT(compound.getCompound("Tank"));
        }

        super.load(compound);
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return index == 0 && FluidUtil.getFluidHandler(stack)
                .map(handler -> handler.getFluidInTank(0).getFluid().isSame(Fluids.LAVA)).orElse(false);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 1;
    }

    @Override
    public void tick() {
        super.tick();

        boolean shouldLit = false;
        boolean shouldSave = false;

        this.transferFluid();

        if (this.canRun() && this.hasFuel() && this.burnTime <= 0) {
            shouldLit = this.consumeFuel();
        }

        if (this.burnTime > 0) {
            --this.burnTime;

            int upgradeCount = this.getUpgradeCount(MachineryItems.SUSTAINED_UPGRADE);

            // Calc the multiplier for the generation
            double multiplier = upgradeCount * BASE_TICKING_ENERGY / (double) this.upgradeContainer.getMaxStackSize();

            // Generate the energy
            this.energyStorage.growEnergy((int) (BASE_TICKING_ENERGY + multiplier));

            shouldLit = true;
            shouldSave = true;
        }

        this.sendEnergyToSlot();
        this.sendUpdate(shouldLit);
        this.sendEnergy();

        if (shouldSave) {
            this.setChanged();
        }
    }

    private void transferFluid() {
        final ItemStack bucketStack = this.stacks.get(0);

        if (!bucketStack.isEmpty()) {
            FluidActionResult result = FluidUtil.tryEmptyContainer(bucketStack, this.lavaTank, FluidType.BUCKET_VOLUME, null, true);

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
    }

    private boolean hasFuel() {
        return this.lavaTank.getFluidAmount() >= MB_PER_USE;
    }

    private boolean consumeFuel() {
        FluidStack fluid = this.lavaTank.drain(MB_PER_USE, IFluidHandler.FluidAction.EXECUTE);

        this.burnTime = this.calcBurnTime(TICKS_PER_MB * fluid.getAmount());
        this.burnTimeTotal = LavaGeneratorBlockEntity.TICKS_PER_MB * LavaGeneratorBlockEntity.MB_PER_USE;

        return true;
    }

    public FluidTank getLavaTank() {
        return this.lavaTank;
    }
}
