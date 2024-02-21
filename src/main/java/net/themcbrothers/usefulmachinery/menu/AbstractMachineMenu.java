package net.themcbrothers.usefulmachinery.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.themcbrothers.lib.energy.EnergyProvider;
import net.themcbrothers.lib.inventory.EnergySlot;
import net.themcbrothers.lib.inventory.PredicateSlot;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import net.themcbrothers.usefulmachinery.item.UpgradeItem;
import net.themcbrothers.usefulmachinery.machine.RedstoneMode;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMachineMenu extends AbstractContainerMenu implements EnergyProvider {
    protected final AbstractMachineBlockEntity blockEntity;
    protected final ContainerData fields;
    protected final int upgradeSlotCount;
    protected final List<RecipeHolder<? extends Recipe<Container>>> recipes;

    protected AbstractMachineMenu(@Nullable MenuType<?> type, int id, AbstractMachineBlockEntity blockEntity, ContainerData fields, int upgradeSlotCount) {
        super(type, id);

        this.blockEntity = blockEntity;
        this.fields = fields;
        this.upgradeSlotCount = upgradeSlotCount;
        this.recipes = new ArrayList<>();

        this.addDataSlots(fields);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.getBlockEntity().stillValid(player);
    }

    @Override
    public long getEnergyStored() {
        return this.fields.get(0);
    }

    @Override
    public long getMaxEnergyStored() {
        return this.fields.get(1);
    }

    public RedstoneMode getRedstoneMode() {
        return RedstoneMode.byOrdinal(this.fields.get(2));
    }

    public void setRedstoneMode(RedstoneMode mode) {
        this.fields.set(2, mode.ordinal());
    }

    public int getUpgradeSlotSize() {
        return this.fields.get(3);
    }

    public AbstractMachineBlockEntity getBlockEntity() {
        return this.blockEntity;
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
            this.addSlot(new PredicateSlot(upgradeContainer, i, 188, 9 + i * 18, this::supportsUpgrade));
        }
    }

    protected boolean isEnergyItem(ItemStack stack, EnergySlot.ItemMode mode) {
        return EnergySlot.isValid(stack, mode);
    }

    protected boolean supportsUpgrade(ItemStack stack) {
        if (this.upgradeSlotCount > 0) {
            if (stack.getItem() instanceof UpgradeItem upgradeItem) {
                return upgradeItem.isSupported(this.blockEntity.getBlockState());
            }
        }

        return false;
    }
}
