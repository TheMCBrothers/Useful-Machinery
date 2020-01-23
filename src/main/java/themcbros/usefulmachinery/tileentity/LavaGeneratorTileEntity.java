package themcbros.usefulmachinery.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import themcbros.usefulmachinery.container.LavaGeneratorContainer;
import themcbros.usefulmachinery.init.ModTileEntities;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.util.TextUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LavaGeneratorTileEntity extends MachineTileEntity {

    public static final int TANK_CAPACITY = 4000; // TODO config
    public static final int TICKS_PER_MB = 10; // TODO config
    public static final int MB_PER_USE = 10; // TODO config
    public static final int RF_PER_TICK = 120; // TODO config

    private final IIntArray fields = new IIntArray() {
        @Override
        public int size() {
            return 9;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4:
                    LavaGeneratorTileEntity.this.redstoneMode = RedstoneMode.byIndex(value);
                    break;
                case 5:
                    LavaGeneratorTileEntity.this.burnTime = value;
                    break;
                default:
                    break;
            }
        }

        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    // Energy lower bytes
                    return LavaGeneratorTileEntity.this.getEnergyStored() & 0xFFFF;
                case 1:
                    // Energy upper bytes
                    return (LavaGeneratorTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2:
                    // Max energy lower bytes
                    return LavaGeneratorTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3:
                    // Max energy upper bytes
                    return (LavaGeneratorTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4:
                    return LavaGeneratorTileEntity.this.redstoneMode.ordinal();
                case 5:
                    return LavaGeneratorTileEntity.this.burnTime;
                case 6:
                    return LavaGeneratorTileEntity.this.lavaTank.getFluidAmount();
                case 7:
                    return LavaGeneratorTileEntity.this.lavaTank.getCapacity();
                case 8:
                    return Registry.FLUID.getId(LavaGeneratorTileEntity.this.lavaTank.getFluid().getFluid());
                default:
                    return 0;
            }
        }
    };

    private int burnTime;
    private FluidTank lavaTank;

    public LavaGeneratorTileEntity() {
        super(ModTileEntities.LAVA_GENERATOR, true);
        this.lavaTank = new FluidTank(TANK_CAPACITY, fluidStack -> fluidStack.getFluid().isIn(FluidTags.LAVA));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("BurnTime", this.burnTime);
        if (!this.lavaTank.getFluid().isEmpty())
            compound.put("Tank", this.lavaTank.writeToNBT(new CompoundNBT()));
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        this.burnTime = compound.getInt("BurnTime");
        if (compound.contains("Tank", Constants.NBT.TAG_COMPOUND))
            this.lavaTank.readFromNBT(compound.getCompound("Tank"));
        super.read(compound);
    }

    @Override
    int[] getInputSlots() {
        return new int[] {0};
    }

    @Override
    int[] getOutputSlots() {
        return new int[] {1};
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return !stack.isEmpty() && index == 0 && stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                .map(handler -> handler.getFluidInTank(0).getFluid().isIn(FluidTags.LAVA)).orElse(false);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index == 1;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public int getSizeInventory() {
        return 3;
    }

    @Override
    public ITextComponent getDisplayName() {
        return TextUtils.translate("container", "lava_generator");
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new LavaGeneratorContainer(i, playerInventory, this, this.fields);
    }

    @Override
    public void tick() {

        final ItemStack bucketStack = this.stacks.get(0);
        if (!bucketStack.isEmpty()) {
            FluidActionResult result = FluidUtil.tryEmptyContainer(bucketStack, this.lavaTank, FluidAttributes.BUCKET_VOLUME, null, true);
            if (result.isSuccess()) {
                ItemStack outputSlotStack = this.stacks.get(1);
                ItemStack resultStack = result.getResult();
                if (resultStack.isItemEqual(outputSlotStack) && resultStack.getMaxStackSize() > 1 &&
                        outputSlotStack.getCount() <= outputSlotStack.getMaxStackSize() - resultStack.getCount()) {
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
        return this.world != null && this.redstoneMode.canRun(this)
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
