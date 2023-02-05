package themcbros.usefulmachinery.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import themcbros.usefulmachinery.MachineryTags;

public class UpgradeSlot extends Slot {
    public UpgradeSlot(Container inventory, int id, int xPos, int yPos) {
        super(inventory, id, xPos, yPos);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(MachineryTags.Items.MACHINERY_UPGRADES);
    }
}
