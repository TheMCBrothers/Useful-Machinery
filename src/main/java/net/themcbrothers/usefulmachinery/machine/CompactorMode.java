package net.themcbrothers.usefulmachinery.machine;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import static net.themcbrothers.usefulfoundation.core.FoundationBlocks.BRONZE_BLOCK;
import static net.themcbrothers.usefulfoundation.core.FoundationItems.GOLD_GEAR;
import static net.themcbrothers.usefulfoundation.core.FoundationItems.LEAD_PLATE;

public enum CompactorMode implements StringRepresentable {
    PLATE(LEAD_PLATE),
    GEAR(GOLD_GEAR),
    BLOCK(BRONZE_BLOCK);

    public static final StringRepresentable.EnumCodec<CompactorMode> CODEC = StringRepresentable.fromEnum(CompactorMode::values);
    private static final CompactorMode[] VALUES = values();
    private static final CompactorMode[] BY_ORDINAL = Arrays.stream(VALUES).sorted(Comparator.comparingInt(Enum::ordinal)).toArray(CompactorMode[]::new);

    private final ItemLike displayItem;

    CompactorMode(ItemLike itemProvider) {
        this.displayItem = itemProvider;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    /**
     * Gets the CompactorMode corresponding to the given index (0-2). Out of bounds values are wrapped around. The order is
     * PLATE-GEAR-BLOCK
     *
     * @param ordinal Ordinal index
     */
    public static CompactorMode byOrdinal(int ordinal) {
        return BY_ORDINAL[Math.abs(ordinal % BY_ORDINAL.length)];
    }

    public ItemLike getItemProvider() {
        return this.displayItem;
    }
}
