package net.themcbrothers.usefulmachinery.machine;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.function.IntFunction;

public enum RedstoneMode implements StringRepresentable {
    IGNORED(Identifier.withDefaultNamespace("textures/item/gunpowder.png")),

    HIGH(Identifier.withDefaultNamespace("textures/block/redstone_torch.png")),

    LOW(Identifier.withDefaultNamespace("textures/block/redstone_torch_off.png"));

    private static final RedstoneMode[] VALUES = values();
    private static final RedstoneMode[] BY_ORDINAL = Arrays.stream(VALUES).sorted(Comparator.comparingInt(Enum::ordinal)).toArray(RedstoneMode[]::new);
    private final Identifier icon;
    private static final IntFunction<RedstoneMode> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    public static final StringRepresentable.EnumCodec<RedstoneMode> CODEC = StringRepresentable.fromEnum(RedstoneMode::values);
    public static final StreamCodec<ByteBuf, RedstoneMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);

    RedstoneMode(Identifier icon) {
        this.icon = icon;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    /**
     * Gets the Redstone Mode corresponding to the given index (0-2). Out of bounds values are wrapped around.
     * The order is IGNORED-ON-OFF
     *
     * @param ordinal Ordinal index
     */
    public static RedstoneMode byOrdinal(int ordinal) {
        return BY_ORDINAL[Math.abs(ordinal % BY_ORDINAL.length)];
    }

    public Identifier getIcon() {
        return icon;
    }

    public boolean canRun(BlockEntity blockEntity) {
        if (blockEntity.getLevel() == null) {
            return false;
        }

        boolean isPowered = blockEntity.getLevel().hasNeighborSignal(blockEntity.getBlockPos());

        return this.canRun(isPowered);
    }

    public boolean canRun(boolean isPowered) {
        if (this == HIGH) {
            return isPowered;
        } else if (this == LOW) {
            return !isPowered;
        }

        return true;
    }
}
