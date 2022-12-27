package themcbros.usefulmachinery.machine;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public enum RedstoneMode implements StringRepresentable {
    IGNORED(0, new ResourceLocation("textures/item/gunpowder.png")),
    HIGH(1, new ResourceLocation("textures/block/redstone_torch.png")),
    LOW(2, new ResourceLocation("textures/block/redstone_torch_off.png"));

    private static final RedstoneMode[] VALUES = values();
    private static final Map<String, RedstoneMode> NAME_LOOKUP = Arrays.stream(VALUES).collect(Collectors.toMap(RedstoneMode::getSerializedName, (p_199787_0_) -> p_199787_0_));
    private static final RedstoneMode[] BY_INDEX = Arrays.stream(VALUES).sorted(Comparator.comparingInt((p_199790_0_) -> p_199790_0_.index)).toArray(RedstoneMode[]::new);

    private final int index;
    private final ResourceLocation icon;

    RedstoneMode(int index, ResourceLocation icon) {
        this.index = index;
        this.icon = icon;
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
    public static RedstoneMode byName(@Nullable String name) {
        return name == null ? null : NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
    }

    /**
     * Gets the RedstoneMode corresponding to the given index (0-2). Out of bounds values are wrapped around. The order is
     * IGNORED-ON-OFF
     */
    public static RedstoneMode byIndex(int index) {
        return BY_INDEX[Math.abs(index % BY_INDEX.length)];
    }

    public int getIndex() {
        return index;
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    public boolean canRun(BlockEntity blockEntity) {
        if (blockEntity == null || blockEntity.getLevel() == null) return false;
        boolean isPowered = blockEntity.getLevel().hasNeighborSignal(blockEntity.getBlockPos());
        if (this.getIndex() == 1) return isPowered;
        else if (this.getIndex() == 2) return !isPowered;
        return true;
    }

    public boolean canRun(boolean isPowered) {
        if (this.getIndex() == 1) return isPowered;
        else if (this.getIndex() == 2) return !isPowered;
        return true;
    }
}
