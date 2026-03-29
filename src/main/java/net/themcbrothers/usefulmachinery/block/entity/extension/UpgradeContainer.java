package net.themcbrothers.usefulmachinery.block.entity.extension;

import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class UpgradeContainer extends SimpleContainer implements ValueIOSerializable {
    private static final String SERIALIZATION_KEY = "Upgrades";

    public UpgradeContainer(int size) {
        super(size);
    }

    @Override
    public int getMaxStackSize() {
        return 4;
    }

    /**
     * Handles the serialization of the container's content.
     * <p>
     * TODO: Candidate to move to TMCB lib
     *
     * @param output ValueOutput
     */
    @Override
    public void serialize(ValueOutput output) {
        ValueOutput.TypedOutputList<ItemStackWithSlot> upgradeStacksWithSlot = output.list(SERIALIZATION_KEY, ItemStackWithSlot.CODEC);

        for (int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack itemstack = this.getItem(i);
            if (!itemstack.isEmpty()) {
                upgradeStacksWithSlot.add(new ItemStackWithSlot(i, itemstack));
            }
        }
    }

    /**
     * Handles the deserialization of the container's content.
     * <p>
     * TODO: Candidate to move to TMCB lib
     *
     * @param input ValueInput
     */
    @Override
    public void deserialize(ValueInput input) {
        ValueInput.TypedInputList<ItemStackWithSlot> upgradeStacksWithSlot = input.listOrEmpty(SERIALIZATION_KEY, ItemStackWithSlot.CODEC);

        // Recreating slots with empty stacks
        for (int i = 0; i < this.getContainerSize(); ++i) {
            this.setItem(i, ItemStack.EMPTY);
        }

        // Filling in actual item stacks
        for (final ItemStackWithSlot upgradeStackWithSlot : upgradeStacksWithSlot) {
            if (upgradeStackWithSlot.isValidInContainer(this.getContainerSize())) {
                this.setItem(upgradeStackWithSlot.slot(), upgradeStackWithSlot.stack());
            }
        }
    }
}


