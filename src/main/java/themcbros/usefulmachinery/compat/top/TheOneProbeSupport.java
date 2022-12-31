package themcbros.usefulmachinery.compat.top;

import mcjty.theoneprobe.api.ITheOneProbe;
import themcbros.usefulmachinery.UsefulMachinery;

import java.util.function.Function;

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
