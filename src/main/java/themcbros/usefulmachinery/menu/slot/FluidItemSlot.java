package themcbros.usefulmachinery.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Predicate;

public class FluidItemSlot extends Slot {

    private final Predicate<FluidStack> validator;

    public FluidItemSlot(Container inventory, int id, int xPos, int yPos, Predicate<FluidStack> validator) {
        super(inventory, id, xPos, yPos);
        this.validator = validator;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !stack.isEmpty() && stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
                .map(fluidHandler -> FluidItemSlot.this.validator.test(fluidHandler.getFluidInTank(0)))
                .orElse(false);
    }
}
