package themcbros.usefulmachinery.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import themcbros.usefulmachinery.container.ElectricSmelterContainer;
import themcbros.usefulmachinery.init.ModTileEntities;
import themcbros.usefulmachinery.machine.RedstoneMode;

import javax.annotation.Nullable;

public class ElectricSmelterTileEntity extends MachineTileEntity {

    private int cookTime, cookTimeTotal;

    private IIntArray fields = new IIntArray() {
        @Override
        public int size() {
            return 7;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4:
                    ElectricSmelterTileEntity.this.redstoneMode = RedstoneMode.byIndex(value);
                    break;
                case 5:
                    ElectricSmelterTileEntity.this.cookTime = value;
                    break;
                case 6:
                    ElectricSmelterTileEntity.this.cookTimeTotal = value;
                    break;
            }
        }

        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    // Energy lower bytes
                    return ElectricSmelterTileEntity.this.getEnergyStored() & 0xFFFF;
                case 1:
                    // Energy upper bytes
                    return (ElectricSmelterTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2:
                    // Max energy lower bytes
                    return ElectricSmelterTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3:
                    // Max energy upper bytes
                    return (ElectricSmelterTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4:
                    // Redstone mode ordinal
                    return ElectricSmelterTileEntity.this.redstoneMode.ordinal();
                case 5:
                    // Crush time
                    return ElectricSmelterTileEntity.this.cookTime;
                case 6:
                    // Total crush time
                    return ElectricSmelterTileEntity.this.cookTimeTotal;
                default:
                    return 0;
            }
        }
    };

    public ElectricSmelterTileEntity() {
        super(ModTileEntities.ELECTRIC_SMELTER, false);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        this.cookTime = compound.getInt("CookTime");
        this.cookTimeTotal = compound.getInt("CookTimeTotal");
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
    public int getSizeInventory() {
        return 3;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.usefulmachinery.electric_smelter");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ElectricSmelterContainer(id, playerInventory, this, this.fields);
    }

    @Override
    public void tick() {

    }
}
