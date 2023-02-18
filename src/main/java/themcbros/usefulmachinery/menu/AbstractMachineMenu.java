package themcbros.usefulmachinery.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.themcbrothers.lib.energy.EnergyProvider;
import themcbros.usefulmachinery.MachineryTags;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;
import themcbros.usefulmachinery.machine.RedstoneMode;

import javax.annotation.Nullable;

public abstract class AbstractMachineMenu extends AbstractContainerMenu implements EnergyProvider {
    protected final AbstractMachineBlockEntity blockEntity;
    protected final ContainerData fields;
    protected final Level level;
    protected final int upgradeSlotCount;

    AbstractMachineMenu(@Nullable MenuType<?> type, int id, Inventory playerInventory, AbstractMachineBlockEntity blockEntity, ContainerData fields, int upgradeSlotCount) {
        super(type, id);
        this.level = playerInventory.player.getLevel();
        this.blockEntity = blockEntity;
        this.upgradeSlotCount = upgradeSlotCount;

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

    protected void addUpgradeSlots(Container upgradeContainer) {
        for (int i = 0; i < upgradeContainer.getContainerSize(); i++) {
            this.addSlot(new UpgradeSlot(upgradeContainer, i, 188, 9 + i * 18));
        }
    }

    protected boolean isEnergyItem(ItemStack stack, boolean canReceive) {
        return !stack.isEmpty() && stack.getCapability(ForgeCapabilities.ENERGY)
                .map(canReceive ? IEnergyStorage::canReceive : IEnergyStorage::canExtract).orElse(false);
    }

    protected boolean isUpgradeItem(ItemStack stack) {
        return this.upgradeSlotCount > 0 && stack.is(MachineryTags.Items.MACHINERY_UPGRADES);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.getBlockEntity().stillValid(playerIn);
    }

    public RedstoneMode getRedstoneMode() {
        return RedstoneMode.byIndex(this.fields.get(4));
    }

    public void setRedstoneMode(RedstoneMode mode) {
        this.fields.set(4, mode.ordinal());
    }

    public int getUpgradeSlotSize() {
        return this.fields.get(5);
    }

    public ContainerData getFields() {
        return fields;
    }

    public AbstractMachineBlockEntity getBlockEntity() {
        return this.blockEntity;
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

    private class UpgradeSlot extends Slot {
        public UpgradeSlot(Container inventory, int id, int xPos, int yPos) {
            super(inventory, id, xPos, yPos);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return isUpgradeItem(stack);
        }
    }
}
