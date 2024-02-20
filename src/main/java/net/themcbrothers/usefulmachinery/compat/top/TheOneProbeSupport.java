package net.themcbrothers.usefulmachinery.compat.top;

import mcjty.theoneprobe.api.ITheOneProbe;
import net.themcbrothers.usefulmachinery.UsefulMachinery;

import java.util.function.Function;

/**
 * The One Probe Compatibility
 */
public class TheOneProbeSupport implements Function<ITheOneProbe, Void> {
    public TheOneProbeSupport() {
    }

    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        UsefulMachinery.LOGGER.info("Enabling support for TheOneProbe");
        theOneProbe.registerProvider(MachineProbeProvider.INSTANCE);
        return null;
    }
}
