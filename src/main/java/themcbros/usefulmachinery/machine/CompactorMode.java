package themcbros.usefulmachinery.machine;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import themcbros.usefulfoundation.init.FoundationItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public enum CompactorMode implements StringRepresentable {
    PLATE(0, FoundationItems.LEAD_PLATE),
    GEAR(1, FoundationItems.GOLD_GEAR),
    BLOCK(2, FoundationItems.BRONZE_BLOCK);

    private static final CompactorMode[] VALUES = values();
    private static final Map<String, CompactorMode> NAME_LOOKUP = Arrays.stream(VALUES).collect(Collectors.toMap(CompactorMode::getSerializedName, (p_199787_0_) -> p_199787_0_));
    private static final CompactorMode[] BY_INDEX = Arrays.stream(VALUES).sorted(Comparator.comparingInt((p_199790_0_) -> p_199790_0_.index)).toArray(CompactorMode[]::new);

    private final int index;
    private final ItemLike itemProvider;

    CompactorMode(int index, ItemLike itemProvider) {
        this.index = index;
        this.itemProvider = itemProvider;
    }

    public int getIndex() {
        return index;
    }

    @Nonnull
    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    /**
     * Get the mode specified by the given name
     */
    @Nullable
    public static CompactorMode byName(@Nullable String name) {
        return name == null ? null : NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
    }

    /**
     * Gets the CompactorMode corresponding to the given index (0-2). Out of bounds values are wrapped around. The order is
     * PLATE-GEAR-BLOCK
     */
    public static CompactorMode byIndex(int index) {
        return BY_INDEX[Math.abs(index % BY_INDEX.length)];
    }

    public ItemStack getIconStack() {
        return new ItemStack(itemProvider);
    }
}
