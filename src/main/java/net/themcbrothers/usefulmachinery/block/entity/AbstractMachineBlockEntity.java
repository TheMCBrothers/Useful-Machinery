package net.themcbrothers.usefulmachinery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.themcbrothers.lib.energy.ExtendedEnergyStorage;
import net.themcbrothers.lib.util.EnergyUtils;
import net.themcbrothers.usefulmachinery.block.AbstractMachineBlock;
import net.themcbrothers.usefulmachinery.block.entity.extension.UpgradeContainer;
import net.themcbrothers.usefulmachinery.component.MachineContents;
import net.themcbrothers.usefulmachinery.item.UpgradeItem;
import net.themcbrothers.usefulmachinery.machine.MachineTier;
import net.themcbrothers.usefulmachinery.machine.RedstoneMode;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.function.Function;

import static net.themcbrothers.usefulmachinery.core.MachineryDataComponentTypes.CONTENTS;
import static net.themcbrothers.usefulmachinery.core.MachineryDataComponentTypes.TIER;

public abstract class AbstractMachineBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeInput {
    protected static final int ENERGY_CAPACITY = 20_000;
    protected static final int MAX_TRANSFER = 100;
    private final boolean isGenerator;
    private int cooldown = -1;
    protected final NonNullList<ItemStack> stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    protected UpgradeContainer upgradeContainer;
    protected int processTime;
    protected int processTimeTotal;
    protected ExtendedEnergyStorage energyStorage;
    protected RedstoneMode redstoneMode = RedstoneMode.IGNORED;
    protected MachineTier tier = MachineTier.SIMPLE;

    public AbstractMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, boolean isGenerator) {
        super(type, pos, state);

        this.isGenerator = isGenerator;
        this.upgradeContainer = new UpgradeContainer(this.getUpgradeSlotSize());

        this.initEnergyStorage(0);
    }

    @Override
    public abstract int getContainerSize();

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        this.energyStorage.serialize(output);

        if (this.processTime > 0) {
            output.putInt("ProcessTime", this.processTime);
        }
        if (this.processTimeTotal > 0) {
            output.putInt("ProcessTimeTotal", this.processTimeTotal);
        }
        if (this.redstoneMode != RedstoneMode.IGNORED) {
            output.putInt("RedstoneMode", this.redstoneMode.ordinal());
        }
        if (!this.upgradeContainer.isEmpty()) {
            this.upgradeContainer.serialize(output);
        }
        if (this.tier != MachineTier.SIMPLE) {
            output.putInt("Tier", this.tier.ordinal());
        }

        ContainerHelper.saveAllItems(output, this.stacks, false);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.stacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> stacks) {
        this.stacks.clear();
        this.stacks.addAll(stacks);

        this.setChanged();
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);

        MachineContents contents = components.get(CONTENTS.get());

        if (contents != null) {
            contents.upgrades().copyInto(this.upgradeContainer.getItems());

            this.energyStorage.set(contents.energyStored());
            this.redstoneMode = contents.redstoneMode();
            this.processTime = contents.processTime();
            this.processTimeTotal = contents.processTimeTotal();
        }

        this.setMachineTier(components.getOrDefault(TIER.get(), MachineTier.SIMPLE));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);

        builder.set(CONTENTS.get(), new MachineContents(ItemContainerContents.fromItems(
                this.upgradeContainer.getItems()),
                this.energyStorage.getAmountAsInt(),
                this.redstoneMode,
                this.processTime,
                this.processTimeTotal,
                0,
                0
        ));
        builder.set(TIER.get(), this.tier);
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        super.removeComponentsFromTag(output);

        output.discard("Upgrades");
        output.discard("EnergyStored");
        output.discard("RedstoneMode");
        output.discard("ProcessTime");
        output.discard("ProcessTimeTotal");
        output.discard("BurnTime");
        output.discard("BurnTimeTotal");
        output.discard("Tier");
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        this.processTime = input.getIntOr("ProcessTime", 0);
        this.processTimeTotal = input.getIntOr("ProcessTimeTotal", 0);
        this.redstoneMode = RedstoneMode.byOrdinal(input.getIntOr("RedstoneMode", 0));
        this.tier = MachineTier.byOrdinal(input.getIntOr("Tier", 0));
        this.upgradeContainer = new UpgradeContainer(this.getUpgradeSlotSize());
        this.upgradeContainer.deserialize(input);

        this.energyStorage.deserialize(input);
        this.initEnergyStorage(this.energyStorage.getAmountAsInt());

        ContainerHelper.loadAllItems(input, this.getItems());
    }

    @Override
    public int[] getSlotsForFace(@Nullable Direction side) {
        if (side == null) {
            return ArrayUtils.addAll(this.getInputSlots(), this.getOutputSlots());
        }

        return side == Direction.DOWN ? this.getOutputSlots() : this.getInputSlots();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public int size() {
        return this.getContainerSize();
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(this.getItems(), index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.getItems(), index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.getItems().set(index, stack);
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.getItems().clear();
    }

    public abstract int[] getInputSlots();

    public abstract int[] getOutputSlots();

    protected abstract boolean canRun();

    public abstract ContainerData getContainerData();

    protected int getRecipeProcessTime() {
        return 200;
    }

    protected void initEnergyStorage(int energyStored) {
        int capacity = ENERGY_CAPACITY * (this.getUpgradeSlotSize() + 1);
        int maxExtract = this.isGenerator ? MAX_TRANSFER : 0;
        int maxReceive = this.isGenerator ? 0 : MAX_TRANSFER;

        this.energyStorage = new ExtendedEnergyStorage(capacity, maxReceive, maxExtract, energyStored);
    }

    protected int calcProcessTime(int processTime) {
        return switch (this.getMachineTier()) {
            case SIMPLE -> processTime;
            case BASIC -> processTime / 2;
            case REINFORCED -> processTime / 4;
            case FACTORY -> processTime / 8;
            case OVERKILL -> processTime / 16;
        };
    }

    protected int calcBurnTime(int burnTime) {
        return (int) switch (this.getMachineTier()) {
            case SIMPLE -> burnTime;
            case BASIC -> burnTime * 1.2;
            case REINFORCED -> burnTime * 1.4;
            case FACTORY -> burnTime * 1.6;
            case OVERKILL -> burnTime * 1.8;
        };
    }

    protected void sendEnergyToSlot() {
        final ItemStack energyStack = this.getItems().get(this.getContainerSize() - 1);

        if (!energyStack.isEmpty()) {
            EnergyUtils.getEnergy(energyStack).ifPresent(energyHandler -> {
                try (Transaction transaction = Transaction.openRoot()) {
                    int maxReceive = this.energyStorage.extract(Integer.MAX_VALUE, transaction);
                    int accepted = energyHandler.insert(maxReceive, transaction);

                    this.energyStorage.extract(accepted, transaction);

                    transaction.commit();
                }
            });
        }
    }

    protected void receiveEnergyFromSlot(int slotIndex) {
        final ItemStack energyStack = this.getItems().get(slotIndex);

        if (!energyStack.isEmpty()) {
            EnergyUtils.getEnergy(energyStack).ifPresent(energyHandler -> {
                try (Transaction transaction = Transaction.openRoot()) {
                    int maxExtract = this.energyStorage.insert(Integer.MAX_VALUE, transaction);
                    int accepted = energyHandler.extract(maxExtract, transaction);

                    this.energyStorage.insert(accepted, transaction);

                    transaction.commit();
                }
            });
        }
    }

    protected int getUpgradeCount(DeferredItem<UpgradeItem> upgrade) {
        return this.getUpgradeCount(upgrade, (stack) -> true);
    }

    protected int getUpgradeCount(DeferredItem<UpgradeItem> upgrade, Function<ItemStack, Boolean> supportsUpgrade) {
        int upgradeCount = 0;

        for (int i = 0; i < this.upgradeContainer.getContainerSize(); i++) {
            ItemStack stack = this.upgradeContainer.getItem(i);

            if (stack.is(upgrade.asItem()) && supportsUpgrade.apply(stack)) {
                upgradeCount += stack.getCount();
            }
        }

        return upgradeCount;
    }

    protected boolean isActive(int RFPerTick) {
        return this.processTime > 0 && this.energyStorage.getAmountAsInt() >= RFPerTick;
    }

    public int getUpgradeSlotSize() {
        return this.getMachineTier().ordinal();
    }

    public void tick() {
        if (this.cooldown >= 0) {
            this.cooldown--;
        }
        if (this.cooldown < 0) {
            this.sendUpdate(false);
        }
        if (!this.canRun() && this.redstoneMode.canRun(this) && this.processTime > 0) {
            this.processTime = 0;

            this.setChanged();
        }
    }

    public void sendUpdate(boolean lit) {
        if (lit) {
            this.cooldown = 15;
        }

        if (lit || this.cooldown < 0) {
            boolean notSameState = this.getBlockState().getValue(AbstractMachineBlock.LIT) != lit;

            if (this.level != null && notSameState) {
                this.level.setBlock(this.worldPosition, this.getBlockState().setValue(AbstractMachineBlock.LIT, lit), 3);
            }
        }
    }

    public int getEnergyStored() {
        return this.energyStorage.getAmountAsInt();
    }

    public ExtendedEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    public int getMaxEnergyStored() {
        return this.energyStorage.getCapacityAsInt();
    }

    public void sendEnergy() {
        if (this.level != null) {
            for (Direction facing : Direction.values()) {
                if (this.getEnergyStored() <= 0) {
                    break;
                }

                EnergyUtils.getEnergy(this.level, this.worldPosition.relative(facing), facing.getOpposite())
                        .ifPresent(energyHandler -> {
                            boolean canReceive = energyHandler.getAmountAsInt() < energyHandler.getCapacityAsInt();

                            if (canReceive) {
                                try (Transaction transaction = Transaction.openRoot()) {
                                    int maxReceive = Math.min(MAX_TRANSFER, this.getEnergyStored());
                                    int accepted = energyHandler.insert(maxReceive, transaction);

                                    this.energyStorage.extract(accepted, transaction);

                                    transaction.commit();
                                }
                            }
                        });
            }
        }
    }

    public int getProcessTime() {
        return this.processTime;
    }

    public int getProcessTimeTotal() {
        return this.processTimeTotal;
    }

    public MachineTier getMachineTier() {
        return this.tier;
    }

    public void setMachineTier(MachineTier tier) {
        if (this.level == null) {
            return;
        }

        this.tier = tier;
        this.upgradeContainer = new UpgradeContainer(this.getUpgradeSlotSize());

        this.initEnergyStorage(this.getEnergyStored());

        this.setChanged();

        this.level.invalidateCapabilities(this.worldPosition);
    }

    public void setRedstoneMode(RedstoneMode redstoneMode) {
        this.redstoneMode = redstoneMode;
        this.setChanged();
    }

    public Container getUpgradeContainer() {
        return this.upgradeContainer;
    }
}
