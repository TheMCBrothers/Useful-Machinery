package themcbros.usefulmachinery.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class OutputSlot extends Slot {
    public OutputSlot(Container inventory, int id, int xPos, int yPos) {
        super(inventory, id, xPos, yPos);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemStack) {
        return false;
    }
}
