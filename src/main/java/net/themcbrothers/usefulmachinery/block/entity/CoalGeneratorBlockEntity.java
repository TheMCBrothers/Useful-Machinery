package net.themcbrothers.usefulmachinery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.themcbrothers.usefulmachinery.core.MachineryBlockEntities;
import net.themcbrothers.usefulmachinery.core.MachineryItems;
import net.themcbrothers.usefulmachinery.machine.RedstoneMode;
import net.themcbrothers.usefulmachinery.menu.CoalGeneratorMenu;
import org.jetbrains.annotations.Nullable;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public class CoalGeneratorBlockEntity extends AbstractMachineBlockEntity {
    private int burnTime;
    private int burnTimeTotal;
    private final ContainerData fields = new ContainerData() {
        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 2 -> CoalGeneratorBlockEntity.this.redstoneMode = RedstoneMode.byOrdinal(value);
                case 4 -> CoalGeneratorBlockEntity.this.burnTime = value;
                case 5 -> CoalGeneratorBlockEntity.this.burnTimeTotal = value;
                default -> {
                }
            }
        }

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> CoalGeneratorBlockEntity.this.getEnergyStored();
                case 1 -> CoalGeneratorBlockEntity.this.getMaxEnergyStored();
                case 2 -> CoalGeneratorBlockEntity.this.redstoneMode.ordinal();
                case 3 -> CoalGeneratorBlockEntity.this.getUpgradeSlotSize();
                case 4 -> CoalGeneratorBlockEntity.this.burnTime;
                case 5 -> CoalGeneratorBlockEntity.this.burnTimeTotal;
                default -> 0;
            };
        }
    };

    public CoalGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(MachineryBlockEntities.COAL_GENERATOR.get(), pos, state, true);
    }

    @Override
    protected int[] getInputSlots() {
        return new int[]{0};
    }

    @Override
    protected int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    protected boolean canRun() {
        boolean canRun = this.redstoneMode.canRun(this);
        boolean canGenerate = this.energyStorage.getEnergyStored() <= this.energyStorage.getMaxEnergyStored();

        return this.level != null && canRun && canGenerate;
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public ContainerData getContainerData() {
        return this.fields;
    }

    @Override
    public Component getDisplayName() {
        return TEXT_UTILS.translate("container", "coal_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new CoalGeneratorMenu(id, playerInventory, this, this.getUpgradeContainer(), this.getContainerData());
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);

        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("BurnTimeTotal", this.burnTimeTotal);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);

        this.burnTime = compound.getInt("BurnTime");
        this.burnTimeTotal = compound.getInt("BurnTimeTotal");
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        //TODO evaluate if should be in config
        final int BASE_TICKING_ENERGY = 3;
        boolean shouldLit = false;
        boolean shouldSave = false;

        if (this.canRun() && this.burnTime <= 0) {
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

    private boolean consumeFuel() {
        ItemStack generatorStack = this.stacks.get(0);
        int burnTime = CommonHooks.getBurnTime(generatorStack, null);

        if (burnTime == 1600) {
            int time = this.calcBurnTime(burnTime);

            this.burnTime = time;
            this.burnTimeTotal = time;

            generatorStack.shrink(1);

            return true;
        }

        return false;
    }
}