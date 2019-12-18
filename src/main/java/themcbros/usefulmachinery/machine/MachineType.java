package themcbros.usefulmachinery.machine;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum MachineType implements IStringSerializable {

    FURNACE,
    CRUSHER,
    COMPRESSOR;

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
