package themcbros.usefulmachinery.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import themcbros.usefulmachinery.container.CrusherContainer;
import themcbros.usefulmachinery.init.ModTileEntities;
import themcbros.usefulmachinery.machine.RedstoneMode;

import javax.annotation.Nullable;

public class CrusherTileEntity extends MachineTileEntity {

    private static final int RF_PER_TICK = 10;

    private IIntArray fields = new IIntArray() {
        @Override
        public int size() {
            return 5;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4:
                    CrusherTileEntity.this.redstoneMode = RedstoneMode.byIndex(value);
                    break;
            }
        }

        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    // Energy lower bytes
                    return CrusherTileEntity.this.getEnergyStored() & 0xFFFF;
                case 1:
                    // Energy upper bytes
                    return (CrusherTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2:
                    // Max energy lower bytes
                    return CrusherTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3:
                    // Max energy upper bytes
                    return (CrusherTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4:
                    return CrusherTileEntity.this.redstoneMode.ordinal();
                default:
                    return 0;
            }
        }
    };

    public CrusherTileEntity() {
        super(ModTileEntities.CRUSHER, false);
    }

    @Override
    int[] getInputSlots() {
        return new int[] {0};
    }

    @Override
    int[] getOutputSlots() {
        return new int[] {1, 2};
    }

    @Override
    public int getSizeInventory() {
        return 3;
    }

    @Override
    public void tick() {

        if (this.getEnergyStored() >= RF_PER_TICK) {

            ItemStack stack = this.stacks.get(0);
            if (!stack.isEmpty()) {
                ItemStack stack1 = this.stacks.get(2);
                if (stack1.isEmpty()) {
                    this.stacks.set(2, stack.copy());
                    stack1.shrink(stack1.getCount());
                } else if (ItemStack.areItemsEqual(stack, stack1)) {
                    this.stacks.get(2).grow(stack.getCount());
                    stack1.shrink(stack1.getCount());
                }
            }

        }

    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.usefulmachinery.crusher");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        return new CrusherContainer(id, playerInventory, this, this.fields);
    }

}
