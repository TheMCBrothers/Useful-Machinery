package net.themcbrothers.usefulmachinery.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.component.ItemContainerContents;
import net.themcbrothers.usefulmachinery.machine.RedstoneMode;

import javax.annotation.Nonnull;

public record MachineContents(ItemContainerContents upgrades, int energyStored, RedstoneMode redstoneMode,
                              int processTime, int processTimeTotal, int burnTime, int burnTimeTotal) {
    public static final MachineContents EMPTY = new MachineContents(ItemContainerContents.EMPTY, 0, RedstoneMode.IGNORED, 0, 0, 0, 0);
    public static final Codec<MachineContents> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemContainerContents.CODEC.optionalFieldOf("upgrades", ItemContainerContents.EMPTY).forGetter(MachineContents::upgrades),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("energy_stored", 0).forGetter(MachineContents::energyStored),
                    RedstoneMode.CODEC.optionalFieldOf("redstone_mode", RedstoneMode.IGNORED).forGetter(MachineContents::redstoneMode),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("process_time", 0).forGetter(MachineContents::processTime),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("process_time_total", 0).forGetter(MachineContents::processTimeTotal),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("burn_time", 0).forGetter(MachineContents::burnTime),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("burn_time_total", 0).forGetter(MachineContents::burnTimeTotal)
            ).apply(instance, MachineContents::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, MachineContents> STREAM_CODEC = new StreamCodec<>() {
        @Override
        @Nonnull
        public MachineContents decode(@Nonnull RegistryFriendlyByteBuf buffer) {
            ItemContainerContents upgrades = ItemContainerContents.STREAM_CODEC.decode(buffer);
            Integer energyStored = ByteBufCodecs.VAR_INT.decode(buffer);
            RedstoneMode mode = RedstoneMode.STREAM_CODEC.decode(buffer);
            Integer processTime = ByteBufCodecs.VAR_INT.decode(buffer);
            Integer processTimeTotal = ByteBufCodecs.VAR_INT.decode(buffer);
            Integer burnTime = ByteBufCodecs.VAR_INT.decode(buffer);
            Integer burnTimeTotal = ByteBufCodecs.VAR_INT.decode(buffer);

            return new MachineContents(upgrades, energyStored, mode, processTime, processTimeTotal, burnTime, burnTimeTotal);
        }

        @Override
        public void encode(@Nonnull RegistryFriendlyByteBuf buffer, MachineContents contents) {
            ItemContainerContents.STREAM_CODEC.encode(buffer, contents.upgrades());
            ByteBufCodecs.VAR_INT.encode(buffer, contents.energyStored());
            RedstoneMode.STREAM_CODEC.encode(buffer, contents.redstoneMode());
            ByteBufCodecs.VAR_INT.encode(buffer, contents.processTime());
            ByteBufCodecs.VAR_INT.encode(buffer, contents.processTimeTotal());
            ByteBufCodecs.VAR_INT.encode(buffer, contents.burnTime());
            ByteBufCodecs.VAR_INT.encode(buffer, contents.burnTimeTotal());
        }
    };
}
