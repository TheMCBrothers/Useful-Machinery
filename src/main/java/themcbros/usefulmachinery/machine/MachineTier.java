package themcbros.usefulmachinery.machine;

import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public enum MachineTier implements StringRepresentable {
    SIMPLE(0x39516d),
    BASIC(0xa6a6a6),
    REINFORCED(0x908928),
    FACTORY(0xe53600),
    OVERKILL(0x005554);

    private static final MachineTier[] VALUES = values();
    private static final MachineTier[] BY_INDEX = Arrays.stream(VALUES).sorted(Comparator.comparingInt(Enum::ordinal)).toArray(MachineTier[]::new);

    private final int color;

    MachineTier(int color) {
        this.color = color;
    }

    @Nonnull
    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    /**
     * Gets the RedstoneMode corresponding to the given ordinal (0-4). Out of bounds values are wrapped around. The order is
     * LEADSTONE-HARDENED-REINFORCED-SIGNALUM-RESONANT
     */
    public static MachineTier byOrdinal(int index) {
        return BY_INDEX[Math.abs(index % BY_INDEX.length)];
    }

    public int getColor() {
        return this.color;
    }
}
