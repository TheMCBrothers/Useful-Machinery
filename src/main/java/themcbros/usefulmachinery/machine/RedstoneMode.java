package themcbros.usefulmachinery.machine;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public enum RedstoneMode implements IStringSerializable {

    IGNORED(0, new ResourceLocation("textures/item/gunpowder")),
    ON(1, new ResourceLocation("textures/block/redstone_torch")),
    OFF(2, new ResourceLocation("textures/block/redstone_torch_off"));

    private static final RedstoneMode[] VALUES = values();
    private static final Map<String, RedstoneMode> NAME_LOOKUP = Arrays.stream(VALUES).collect(Collectors.toMap(RedstoneMode::getName, (p_199787_0_) -> p_199787_0_));
    private static final RedstoneMode[] BY_INDEX = Arrays.stream(VALUES).sorted(Comparator.comparingInt((p_199790_0_) -> p_199790_0_.index)).toArray(RedstoneMode[]::new);

    private final int index;
    private final ResourceLocation icon;

    RedstoneMode(int index, ResourceLocation icon) {
        this.index = index;
        this.icon = icon;
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    /**
     * Get the mode specified by the given name
     */
    @Nullable
    public static RedstoneMode byName(@Nullable String name) {
        return name == null ? null : NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
    }

    /**
     * Gets the RedstoneMode corresponding to the given index (0-2). Out of bounds values are wrapped around. The order is
     * IGNORED-ON-OFF
     */
    public static RedstoneMode byIndex(int index) {
        return BY_INDEX[MathHelper.abs(index % BY_INDEX.length)];
    }

    public ResourceLocation getIcon() {
        return icon;
    }
}
