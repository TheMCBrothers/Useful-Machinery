package themcbros.usefulmachinery.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.themcbrothers.lib.energy.ExtendedEnergyStorage;
import net.themcbrothers.lib.util.EnergyUtils;
import org.jetbrains.annotations.NotNull;
import themcbros.usefulmachinery.blocks.AbstractMachineBlock;
import themcbros.usefulmachinery.machine.MachineTier;
import themcbros.usefulmachinery.machine.RedstoneMode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractMachineBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider {
    protected static final int ENERGY_CAPACITY = 20_000;
    protected static final int MAX_TRANSFER = 100;
    protected final NonNullList<ItemStack> stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    protected int processTime, processTimeTotal;
    protected ExtendedEnergyStorage energyStorage;
    protected MachineTier machineTier = MachineTier.SIMPLE;
    protected RedstoneMode redstoneMode = RedstoneMode.IGNORED;
    private final boolean isGenerator;
    private int cooldown = -1;

    AbstractMachineBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, boolean isGenerator) {
        super(blockEntityType, blockPos, blockState);
        this.isGenerator = isGenerator;
        this.energyStorage = new ExtendedEnergyStorage(ENERGY_CAPACITY * (this.machineTier.ordinal() + 1), !isGenerator ? MAX_TRANSFER : 0, isGenerator ? MAX_TRANSFER : 0);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        if (this.processTime > 0) {
            compound.putInt("ProcessTime", this.processTime);
        }
        if (this.processTimeTotal > 0) {
            compound.putInt("ProcessTimeTotal", this.processTimeTotal);
        }
        if (machineTier != MachineTier.SIMPLE) {
            compound.putInt("Tier", machineTier.ordinal());
        }
        if (redstoneMode != RedstoneMode.IGNORED) {
            compound.putInt("RedstoneMode", redstoneMode.getIndex());
        }
        if (this.energyStorage.getEnergyStored() > 0) {
            compound.putInt("EnergyStored", this.energyStorage.getEnergyStored());
        }

        ContainerHelper.saveAllItems(compound, this.stacks, false);
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("ProcessTime", Tag.TAG_INT)) {
            this.processTime = compound.getInt("ProcessTime");
        }
        if (compound.contains("ProcessTimeTotal", Tag.TAG_INT)) {
            this.processTimeTotal = compound.getInt("ProcessTimeTotal");
        }
        if (compound.contains("Tier", Tag.TAG_INT)) {
            this.machineTier = MachineTier.byOrdinal(compound.getInt("Tier"));
        }
        if (compound.contains("RedstoneMode", Tag.TAG_INT)) {
            this.redstoneMode = RedstoneMode.byIndex(compound.getInt("RedstoneMode"));
        }
        if (compound.contains("EnergyStored", Tag.TAG_INT)) {
            this.energyStorage = new ExtendedEnergyStorage(ENERGY_CAPACITY * (this.machineTier.ordinal() + 1), !isGenerator ? MAX_TRANSFER : 0, isGenerator ? MAX_TRANSFER : 0, compound.getInt("EnergyStored"));
        }
        ContainerHelper.loadAllItems(compound, this.stacks);

        super.load(compound);
    }

    abstract int[] getInputSlots();

    abstract int[] getOutputSlots();

    @Override
    public int[] getSlotsForFace(Direction side) {
        // TODO: Implement side config
        return side == Direction.DOWN ? this.getOutputSlots() : this.getInputSlots();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        // TODO: Implement side config
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        // TODO: Implement side config
        return true;
    }

    @Override
    public abstract int getContainerSize();

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.stacks) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.stacks.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(this.stacks, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.stacks, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        //TODO
        this.stacks.set(index, stack);
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        assert this.level != null;

        if (this.level.getBlockEntity(this.getBlockPos()) != this) {
            return false;
        } else {
            return !(player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void clearContent() {
        this.stacks.clear();
    }

    public void tick() {
        if (this.cooldown > 0) {
            this.cooldown--;
        }
        if (this.cooldown < 0) {
            sendUpdate(false);
        }
    }

    public int calcProcessTime(int processTime) {
        return switch (this.machineTier) {
            case SIMPLE -> processTime;
            case BASIC -> processTime / 2;
            case REINFORCED -> processTime / 4;
            case FACTORY -> processTime / 8;
            case OVERKILL -> processTime / 16;
        };
    }

    public int calcBurnTime(int burnTime) {
        return switch (this.machineTier) {
            case SIMPLE -> burnTime;
            case BASIC -> burnTime * 2;
            case REINFORCED -> burnTime * 4;
            case FACTORY -> burnTime * 8;
            case OVERKILL -> burnTime * 16;
        };
    }

    public void sendUpdate(boolean lit) {
        if (lit) {
            this.cooldown = 15;
        }
        boolean flag = this.getBlockState().getValue(AbstractMachineBlock.LIT) != lit;

        if (flag && this.level != null) {
            this.level.setBlock(this.worldPosition, this.getBlockState().setValue(AbstractMachineBlock.LIT, lit), 3);
        }
    }

    private final LazyOptional<IItemHandlerModifiable>[] itemHandlers = SidedInvWrapper.create(this, Direction.values());
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> this.energyStorage);

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && side != null && cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandlers[side.get3DDataValue()].cast();
        } else if (cap == ForgeCapabilities.ENERGY) {
            return energyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    public int getEnergyStored() {
        return this.energyStorage.getEnergyStored();
    }

    public ExtendedEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    public int getMaxEnergyStored() {
        return this.energyStorage.getMaxEnergyStored();
    }

    // Util methods
    protected void sendEnergyToSlot() {
        final ItemStack energyStack = this.stacks.get(1);

        if (!energyStack.isEmpty()) {
            IEnergyStorage energy = energyStack.getCapability(ForgeCapabilities.ENERGY).orElseThrow(NullPointerException::new);

            if (energy.canReceive()) {
                int accepted = energy.receiveEnergy(Math.min(MAX_TRANSFER, this.getEnergyStored()), false);
                this.energyStorage.consumeEnergy(accepted);
            }
        }
    }

    protected void receiveEnergyFromSlot(int slotIndex) {
        final ItemStack energyStack = this.stacks.get(slotIndex);

        if (!energyStack.isEmpty()) {
            IEnergyStorage energy = energyStack.getCapability(ForgeCapabilities.ENERGY).orElseThrow(NullPointerException::new);
            if (energy.canExtract()) {
                int accept = energy.extractEnergy(Math.min(this.getMaxEnergyStored() - this.getEnergyStored(), MAX_TRANSFER), true);

                if (this.getEnergyStored() <= this.getMaxEnergyStored() - accept)
                    this.energyStorage.growEnergy(energy.extractEnergy(accept, false));
            }
        }
    }

    public void sendEnergy() {
        // TODO implement side config
        for (Direction facing : Direction.values()) {
            assert this.level != null;

            IEnergyStorage energy = EnergyUtils.getEnergy(this.level, this.worldPosition.relative(facing), facing.getOpposite());

            if (energy != null && energy.canReceive()) {
                int accepted = energy.receiveEnergy(Math.min(MAX_TRANSFER, this.getEnergyStored()), false);
                this.energyStorage.consumeEnergy(accepted);

                if (this.getEnergyStored() <= 0) {
                    break;
                }
            }
        }
    }

    public int getProcessTime() {
        return processTime;
    }

    public int getProcessTimeTotal() {
        return processTimeTotal;
    }

    public MachineTier getMachineTier() {
        return machineTier;
    }

    public void setMachineTier(MachineTier machineTier) {
        this.machineTier = machineTier;
    }

    public RedstoneMode getRedstoneMode() {
        return redstoneMode;
    }

    public void setRedstoneMode(RedstoneMode redstoneMode) {
        this.redstoneMode = redstoneMode;
    }
}
