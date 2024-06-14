package net.themcbrothers.usefulmachinery.core;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.themcbrothers.usefulmachinery.component.MachineContents;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;
import net.themcbrothers.usefulmachinery.machine.MachineTier;

import static net.themcbrothers.usefulmachinery.core.Registration.DATA_COMPONENT_TYPES;

public class MachineryDataComponentTypes {
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MachineTier>> TIER = DATA_COMPONENT_TYPES.registerComponentType("tier", builder -> builder.persistent(MachineTier.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompactorMode>> MODE = DATA_COMPONENT_TYPES.registerComponentType("mode", builder -> builder.persistent(CompactorMode.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MachineContents>> CONTENTS = DATA_COMPONENT_TYPES.registerComponentType("contents", builder -> builder.persistent(MachineContents.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FluidStack>> TANK = DATA_COMPONENT_TYPES.registerComponentType("tank", builder -> builder.persistent(FluidStack.CODEC));

    static void init() {
    }
}
