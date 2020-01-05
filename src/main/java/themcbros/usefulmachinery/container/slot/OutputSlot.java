package themcbros.usefulmachinery.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class OutputSlot extends Slot {

    public OutputSlot(IInventory inventory, int id, int xPos, int yPos) {
        super(inventory, id, xPos, yPos);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }
}
