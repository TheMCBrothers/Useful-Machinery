package net.themcbrothers.usefulmachinery.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

import java.util.function.Predicate;

public class FluidItemSlot extends Slot {
    private final Predicate<FluidStack> validator;

    public FluidItemSlot(Container container, int slot, int x, int y, Predicate<FluidStack> validator) {
        super(container, slot, x, y);

        this.validator = validator;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        ItemAccess itemAccess = ItemAccess.forStack(stack);
        ResourceHandler<FluidResource> itemAccessCapability = itemAccess.getCapability(Capabilities.Fluid.ITEM);

        if (itemAccessCapability == null) {
            return false;
        }

        FluidResource resource = itemAccessCapability.getResource(0);

        return this.validator.test(resource.toStack(itemAccessCapability.getAmountAsInt(0)));
    }
}
