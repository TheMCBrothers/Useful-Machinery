package themcbros.usefulmachinery.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.function.Predicate;

public class FluidItemSlot extends Slot {

    private final Predicate<FluidStack> validator;

    public FluidItemSlot(Container inventory, int id, int xPos, int yPos, Predicate<FluidStack> validator) {
        super(inventory, id, xPos, yPos);
        this.validator = validator;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !stack.isEmpty() && stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                .map(fluidHandler -> FluidItemSlot.this.validator.test(fluidHandler.getFluidInTank(0))).orElse(false);
    }
}
