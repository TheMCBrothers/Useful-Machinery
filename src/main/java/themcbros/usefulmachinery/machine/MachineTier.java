package themcbros.usefulmachinery.machine;

import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
     * Gets the Machine Tier corresponding to the given ordinal (0-4). Out of bounds values are wrapped around.
     * The order is SIMPLE-BASIC-REINFORCED-FACTORY-OVERKILL
     *
     * @param index Ordinal index
     */
    public static MachineTier byOrdinal(int index) {
        return BY_INDEX[Math.abs(index % BY_INDEX.length)];
    }

    /**
     * Gets the Machine Tier corresponding to the given name.
     *
     * @param name Tier as String
     * @return Machine Tier
     */
    @Nullable
    public static MachineTier byName(@Nullable String name) {
        return CODEC.byName(name);
    }


    public int getColor() {
        return this.color;
    }
}
