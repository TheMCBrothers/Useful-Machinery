package themcbros.usefulmachinery.blockentity.extension;

import net.minecraft.world.Container;
import themcbros.usefulmachinery.machine.CompactorMode;

public interface Compactor extends Container {
    CompactorMode getMode();
}
