package net.themcbrothers.usefulmachinery.machine;

import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public enum MachineTier implements StringRepresentable {
    SIMPLE(0x39516d),
    BASIC(0xa6a6a6),
    REINFORCED(0x908928),
    FACTORY(0xe53600),
    OVERKILL(0x005554);

    public static final StringRepresentable.EnumCodec<MachineTier> CODEC = StringRepresentable.fromEnum(MachineTier::values);
    private static final MachineTier[] VALUES = values();
    private static final MachineTier[] BY_ORDINAL = Arrays.stream(VALUES).sorted(Comparator.comparingInt(Enum::ordinal)).toArray(MachineTier[]::new);
    private final int color;

    MachineTier(int color) {
        this.color = color;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    /**
     * Gets the Machine Tier corresponding to the given ordinal (0-4). Out of bounds values are wrapped around.
     * The order is SIMPLE-BASIC-REINFORCED-FACTORY-OVERKILL
     *
     * @param ordinal Ordinal index
     */
    public static MachineTier byOrdinal(int ordinal) {
        return BY_ORDINAL[Math.abs(ordinal % BY_ORDINAL.length)];
    }

    public int getColor() {
        return this.color;
    }
}
