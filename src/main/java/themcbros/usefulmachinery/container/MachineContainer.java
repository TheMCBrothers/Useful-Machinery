package themcbros.usefulmachinery.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.tileentity.MachineTileEntity;

import javax.annotation.Nullable;

public class MachineContainer extends Container {

    public MachineTileEntity machineTileEntity;
    private RedstoneMode mode = RedstoneMode.IGNORED;
    protected IIntArray fields;

    MachineContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory, MachineTileEntity tileEntity, IIntArray fields) {
        super(type, id);
        this.machineTileEntity = tileEntity;

        this.fields = fields;
        this.trackIntArray(fields);

    }

    protected void addPlayerSlots(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.machineTileEntity.isUsableByPlayer(playerIn);
    }

    public RedstoneMode getRedstoneMode() {
        return RedstoneMode.byIndex(this.fields.get(4));
    }

    public void setRedstoneMode(RedstoneMode mode) {
        this.fields.set(4, mode.ordinal());
    }

    public IIntArray getFields() {
        return fields;
    }

    public int getEnergyStored() {
        int lower = this.fields.get(0) & 0xFFFF;
        int upper = this.fields.get(1) & 0xFFFF;
        return (upper << 16) + lower;
    }

    public int getMaxEnergyStored() {
        int lower = this.fields.get(2) & 0xFFFF;
        int upper = this.fields.get(3) & 0xFFFF;
        return (upper << 16) + lower;
    }

    public int getEnergyScaled(int height) {
        int i = this.getEnergyStored();
        int j = this.getMaxEnergyStored();
        return i != 0 && j != 0 ? height * i / j : 0;
    }
}
