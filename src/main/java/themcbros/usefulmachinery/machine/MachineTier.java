package themcbros.usefulmachinery.machine;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public enum MachineTier implements IStringSerializable {

    LEADSTONE(0x39516d),
    HARDENED(0xa6a6a6),
    REINFORCED(0x908928),
    SIGNALUM(0xff5900),
    RESONANT(0x008571);

    private static final MachineTier[] VALUES = values();
    private static final MachineTier[] BY_INDEX = Arrays.stream(VALUES).sorted(Comparator.comparingInt(Enum::ordinal)).toArray(MachineTier[]::new);

    private final int color;

    MachineTier(int color) {
        this.color = color;
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    /**
     * Gets the RedstoneMode corresponding to the given ordinal (0-4). Out of bounds values are wrapped around. The order is
     * LEADSTONE-HARDENED-REINFORCED-SIGNALUM-RESONANT
     */
    public static MachineTier byOrdinal(int index) {
        return BY_INDEX[MathHelper.abs(index % BY_INDEX.length)];
    }

    public int getColor() {
        return this.color;
    }
}
