package themcbros.usefulmachinery.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergySlot extends Slot {
    public EnergySlot(Container inventory, int id, int xPos, int yPos) {
        super(inventory, id, xPos, yPos);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !stack.isEmpty() && stack.getCapability(CapabilityEnergy.ENERGY).isPresent() &&
                stack.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::canReceive).orElse(false);
    }
}
