package themcbros.usefulmachinery.blockentity;

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
import net.minecraftforge.common.ForgeHooks;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.blocks.AbstractMachineBlock;
import themcbros.usefulmachinery.container.CoalGeneratorContainer;
import themcbros.usefulmachinery.init.MachineryBlockEntities;
import themcbros.usefulmachinery.machine.RedstoneMode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CoalGeneratorBlockEntity extends AbstractMachineBlockEntity {
    private final ContainerData fields = new ContainerData() {
        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4 -> CoalGeneratorBlockEntity.this.setRedstoneMode(RedstoneMode.byIndex(value));
                case 5 -> CoalGeneratorBlockEntity.this.burnTime = value;
                case 6 -> CoalGeneratorBlockEntity.this.burnTimeTotal = value;
                default -> {
                }
            }
        }

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> CoalGeneratorBlockEntity.this.getEnergyStorage().getEnergyStored() & 0xFFFF;
                case 1 -> (CoalGeneratorBlockEntity.this.getEnergyStorage().getEnergyStored() >> 16) & 0xFFFF;
                case 2 -> CoalGeneratorBlockEntity.this.getEnergyStorage().getMaxEnergyStored() & 0xFFFF;
                case 3 -> (CoalGeneratorBlockEntity.this.getEnergyStorage().getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4 -> CoalGeneratorBlockEntity.this.getRedstoneMode().ordinal();
                case 5 -> CoalGeneratorBlockEntity.this.burnTime;
                case 6 -> CoalGeneratorBlockEntity.this.burnTimeTotal;
                default -> 0;
            };
        }
    };

    private int burnTime, burnTimeTotal;

    public CoalGeneratorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(MachineryBlockEntities.COAL_GENERATOR.get(), blockPos, blockState, true);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);

        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("BurnTimeTotal", this.burnTimeTotal);
    }

    @Override
    public void load(CompoundTag compound) {
        this.burnTime = compound.getInt("BurnTime");
        this.burnTimeTotal = compound.getInt("BurnTimeTotal");

        super.load(compound);
    }

    @Override
    int[] getInputSlots() {
        return new int[]{0};
    }

    @Override
    int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return UsefulMachinery.TEXT_UTILS.translate("container", "coal_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new CoalGeneratorContainer(id, playerInventory, this, this.fields);
    }

    @Override
    public void tick() {
        boolean shouldLit = false;

        assert this.level != null;
        if (!level.isClientSide) {
            if (this.burnTime > 0) {
                --this.burnTime;

                this.getEnergyStorage().modifyEnergyStored(60);

                shouldLit = true;
            } else if (this.getRedstoneMode().canRun(this)) {
                ItemStack generatorStack = this.stacks.get(0);
                if (ForgeHooks.getBurnTime(generatorStack, null) == 1600) {
                    int time = calcBurnTime(ForgeHooks.getBurnTime(generatorStack, null));

                    this.burnTime = time;
                    this.burnTimeTotal += time;

                    shouldLit = true;
                    generatorStack.shrink(1);
                }
            }

            this.sendEnergyToSlot();

            if (this.getBlockState().getValue(AbstractMachineBlock.LIT) != shouldLit) {
                this.sendUpdate(shouldLit);
            }
        }

        this.sendEnergy();
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return false;
    }
}
