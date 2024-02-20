package net.themcbrothers.usefulmachinery.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;

import java.util.function.Predicate;

public class FluidItemSlot extends Slot {
    private final Predicate<FluidStack> validator;

    public FluidItemSlot(Container container, int slot, int x, int y, Predicate<FluidStack> validator) {
        super(container, slot, x, y);

        this.validator = validator;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return FluidUtil.getFluidHandler(stack)
                .map(fluidHandler -> FluidItemSlot.this.validator.test(fluidHandler.getFluidInTank(0)))
                .orElse(false);
    }
}
