package net.themcbrothers.usefulmachinery.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class OutputSlot extends Slot {
    public OutputSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return false;
    }
}
