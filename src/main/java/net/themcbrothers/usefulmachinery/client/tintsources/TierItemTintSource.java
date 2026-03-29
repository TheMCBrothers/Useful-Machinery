package net.themcbrothers.usefulmachinery.client.tintsources;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.themcbrothers.usefulmachinery.core.MachineryDataComponentTypes;
import net.themcbrothers.usefulmachinery.machine.MachineTier;
import org.jspecify.annotations.Nullable;

public record TierItemTintSource() implements ItemTintSource {
    public static final TierItemTintSource INSTANCE = new TierItemTintSource();
    public static final MapCodec<TierItemTintSource> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        return stack.getOrDefault(MachineryDataComponentTypes.TIER, MachineTier.SIMPLE).getColor();
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
