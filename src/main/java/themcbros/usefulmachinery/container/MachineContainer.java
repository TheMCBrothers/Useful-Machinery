package themcbros.usefulmachinery.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.themcbrothers.lib.energy.EnergyProvider;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;
import themcbros.usefulmachinery.machine.RedstoneMode;

import javax.annotation.Nullable;

public class MachineContainer extends AbstractContainerMenu implements EnergyProvider {
    public AbstractMachineBlockEntity abstractMachineBlockEntity;
    protected ContainerData fields;

    MachineContainer(@Nullable MenuType<?> type, int id, Inventory playerInventory, AbstractMachineBlockEntity tileEntity, ContainerData fields) {
        super(type, id);
        this.abstractMachineBlockEntity = tileEntity;

        this.fields = fields;
        this.addDataSlots(fields);

    }

    protected void addPlayerSlots(Inventory playerInventory) {
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
    public boolean stillValid(Player playerIn) {
        return this.abstractMachineBlockEntity.stillValid(playerIn);
    }

    public RedstoneMode getRedstoneMode() {
        return RedstoneMode.byIndex(this.fields.get(4));
    }

    public void setRedstoneMode(RedstoneMode mode) {
        this.fields.set(4, mode.ordinal());
    }

    public ContainerData getFields() {
        return fields;
    }

    @Override
    public long getEnergyStored() {
        long lower = this.fields.get(0) & 0xFFFF;
        long upper = this.fields.get(1) & 0xFFFF;
        return (upper << 16L) + lower;
    }

    @Override
    public long getMaxEnergyStored() {
        long lower = this.fields.get(2) & 0xFFFF;
        long upper = this.fields.get(3) & 0xFFFF;
        return (upper << 16L) + lower;
    }
}
