package net.themcbrothers.usefulmachinery.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.themcbrothers.usefulmachinery.core.MachineryTags;

public class UpgradeSlot extends Slot {

    public UpgradeSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(MachineryTags.Items.MACHINERY_UPGRADES);
    }
}
