package net.themcbrothers.usefulmachinery.core;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.themcbrothers.usefulmachinery.component.MachineContents;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;
import net.themcbrothers.usefulmachinery.machine.MachineTier;

import static net.themcbrothers.usefulmachinery.core.Registration.DATA_COMPONENT_TYPES;

public class MachineryDataComponentTypes {
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MachineTier>> TIER = DATA_COMPONENT_TYPES.registerComponentType("tier", builder -> builder.persistent(MachineTier.CODEC).networkSynchronized(MachineTier.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompactorMode>> MODE = DATA_COMPONENT_TYPES.registerComponentType("mode", builder -> builder.persistent(CompactorMode.CODEC).networkSynchronized(CompactorMode.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MachineContents>> CONTENTS = DATA_COMPONENT_TYPES.registerComponentType("contents", builder -> builder.persistent(MachineContents.CODEC).networkSynchronized(MachineContents.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> TANK = DATA_COMPONENT_TYPES.registerComponentType("tank", builder -> builder.persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC));

    static void init() {
    }
}
