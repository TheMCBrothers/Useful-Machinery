package themcbros.usefulmachinery.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.energy.IEnergyStorage;
import themcbros.usefulmachinery.blocks.MachineBlock;
import themcbros.usefulmachinery.container.CoalGeneratorContainer;
import themcbros.usefulmachinery.init.ModTileEntities;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.util.EnergyUtils;
import themcbros.usefulmachinery.util.TextUtils;

import javax.annotation.Nullable;

public class CoalGeneratorTileEntity extends MachineTileEntity {

    private final IIntArray fields = new IIntArray() {
        @Override
        public int size() {
            return 7;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4:
                    CoalGeneratorTileEntity.this.redstoneMode = RedstoneMode.byIndex(value);
                    break;
                case 5:
                    CoalGeneratorTileEntity.this.burnTime = value;
                    break;
                case 6:
                    CoalGeneratorTileEntity.this.burnTimeTotal = value;
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
                    return CoalGeneratorTileEntity.this.getEnergyStored() & 0xFFFF;
                case 1:
                    // Energy upper bytes
                    return (CoalGeneratorTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2:
                    // Max energy lower bytes
                    return CoalGeneratorTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3:
                    // Max energy upper bytes
                    return (CoalGeneratorTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4:
                    return CoalGeneratorTileEntity.this.redstoneMode.ordinal();
                case 5:
                    return CoalGeneratorTileEntity.this.burnTime;
                case 6:
                    return CoalGeneratorTileEntity.this.burnTimeTotal;
                default:
                    return 0;
            }
        }
    };

    private int burnTime, burnTimeTotal;

    public CoalGeneratorTileEntity() {
        super(ModTileEntities.COAL_GENERATOR, true);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("BurnTimeTotal", this.burnTimeTotal);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        this.burnTime = compound.getInt("BurnTime");
        this.burnTimeTotal = compound.getInt("BurnTimeTotal");
        super.read(compound);
    }

    @Override
    int[] getInputSlots() {
        return new int[] {0};
    }

    @Override
    int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    public ITextComponent getDisplayName() {
        return TextUtils.translate("container", "coal_generator");
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CoalGeneratorContainer(i, playerInventory, this, this.fields);
    }

    @Override
    public void tick() {

        boolean shouldLit = false;

        assert world != null;
        if (!world.isRemote) {
            if (this.burnTime > 0) {
                --this.burnTime;
                this.energyStorage.receiveEnergy(60, false);
                shouldLit = true;
            } else {
                ItemStack generatorStack = this.stacks.get(0);
                if (AbstractFurnaceTileEntity.isFuel(generatorStack)) {
                    int time = ForgeHooks.getBurnTime(generatorStack);
                    this.burnTime = time;
                    this.burnTimeTotal = time;
                    shouldLit = true;
                    generatorStack.shrink(1);
                }
            }

            if (this.getBlockState().get(MachineBlock.LIT) != shouldLit) {
                this.sendUpdate(shouldLit);
            }
        }

        this.sendEnergy();
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }

    public void sendEnergy() {
        // TODO implement side config
        for (Direction facing : Direction.values()) {
            assert this.world != null;
            IEnergyStorage energy = EnergyUtils.getEnergy(this.world, this.pos.offset(facing), facing.getOpposite());
            if (energy != null && energy.canReceive()) {
                int accepted = energy.receiveEnergy(Math.min(MAX_TRANSFER, this.getEnergyStored()), false);
                this.energyStorage.modifyEnergyStored(-accepted);
                if (this.getEnergyStored() <= 0) break;
            }
        }
    }

}
