package net.themcbrothers.usefulmachinery.compat.jei.recipes;

import com.google.common.base.Preconditions;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Objects;

public class LavaGeneratingRecipe implements IJeiFuelingRecipe {
    private final List<ItemStack> inputs;
    private final int burnTime;

    public LavaGeneratingRecipe(List<ItemStack> inputs, int burnTime, List<TagKey<Item>> tags) {
        Preconditions.checkArgument(tags.contains(Tags.Items.BUCKETS_LAVA));

        this.inputs = Objects.requireNonNull(inputs);
        this.burnTime = burnTime;
    }

    @Override
    public @Unmodifiable List<ItemStack> getInputs() {
        return this.inputs;
    }

    @Override
    public int getBurnTime() {
        return this.burnTime;
    }
}
