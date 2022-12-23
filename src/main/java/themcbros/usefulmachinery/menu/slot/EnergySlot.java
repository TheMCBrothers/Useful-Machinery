package themcbros.usefulmachinery.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergySlot extends Slot {
    public EnergySlot(Container inventory, int id, int xPos, int yPos) {
        super(inventory, id, xPos, yPos);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !stack.isEmpty() && stack.getCapability(ForgeCapabilities.ENERGY).isPresent() &&
                stack.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::canReceive).orElse(false);
    }
}
