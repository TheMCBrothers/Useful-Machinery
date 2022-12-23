package themcbros.usefulmachinery.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import themcbros.usefulmachinery.UsefulMachinery;

public class TextUtils {
    private static final String ENERGY_FORMAT = "%,d";

    public static Component translate(String prefix, String suffix, Object... params) {
        return Component.translatable(prefix + "." + UsefulMachinery.MOD_ID + "." + suffix, params);
    }

    // Energy

    public static Component energy(int amount) {
        String s1 = String.format(ENERGY_FORMAT, amount);
        return translate("misc", "energy", s1);
    }

    public static Component energyWithMax(int amount, int max) {
        String s1 = String.format(ENERGY_FORMAT, amount);
        String s2 = String.format(ENERGY_FORMAT, max);
        return translate("misc", "energyWithMax", s1, s2);
    }

    // Fluids

    public static Component fluidWithMaxName(IFluidHandler tank) {
        FluidStack fluid = tank.getFluidInTank(0);
        Component fluidName = fluid.getDisplayName();
        String s1 = String.format(ENERGY_FORMAT, fluid.getAmount());
        String s2 = String.format(ENERGY_FORMAT, tank.getTankCapacity(0));
        return translate("misc", fluid.getAmount() > 0 ? "fluidWithMaxName" : "empty", fluidName, s1, s2);
    }

    public static Component fluidWithMaxName(FluidStack fluid, int max) {
        Component fluidName = fluid.getDisplayName();
        String s1 = String.format(ENERGY_FORMAT, fluid.getAmount());
        String s2 = String.format(ENERGY_FORMAT, max);
        return translate("misc", fluid.getAmount() > 0 ? "fluidWithMaxName" : "empty", fluidName, s1, s2);
    }

    public static Component fluidWithMaxName(String fluid, int amount, int max) {
        String s1 = String.format(ENERGY_FORMAT, amount);
        String s2 = String.format(ENERGY_FORMAT, max);
        return translate("misc", amount > 0 ? "fluidWithMaxName" : "empty", fluid, s1, s2);
    }

    public static Component fluidWithMax(IFluidHandler tank) {
        FluidStack fluid = tank.getFluidInTank(0);
        String s1 = String.format(ENERGY_FORMAT, fluid.getAmount());
        String s2 = String.format(ENERGY_FORMAT, tank.getTankCapacity(0));
        return translate("misc", "fluidWithMax", s1, s2);
    }

    public static Component fluidWithMax(int amount, int max) {
        String s1 = String.format(ENERGY_FORMAT, amount);
        String s2 = String.format(ENERGY_FORMAT, max);
        return translate("misc", "fluidWithMax", s1, s2);
    }

    public static Component fluidName(FluidStack stack) {
        if (stack.isEmpty()) return translate("misc", "empty");
        return stack.getDisplayName();
    }

    public static Component fluidName(Fluid fluid) {
        if (fluid == Fluids.EMPTY) return translate("misc", "empty");
        return fluid.getFluidType().getDescription(FluidStack.EMPTY);
    }
}
