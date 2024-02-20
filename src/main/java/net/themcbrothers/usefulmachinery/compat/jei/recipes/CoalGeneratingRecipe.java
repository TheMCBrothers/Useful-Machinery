package net.themcbrothers.usefulmachinery.compat.jei.recipes;

import com.google.common.base.Preconditions;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class CoalGeneratingRecipe implements IJeiFuelingRecipe {
    private final List<ItemStack> inputs;
    private final int burnTime;

    public CoalGeneratingRecipe(List<ItemStack> inputs, int burnTime) {
        Preconditions.checkArgument(burnTime == 1600);

        this.inputs = Objects.requireNonNull(inputs);
        this.burnTime = burnTime;
    }

    @Override
    public @Unmodifiable List<ItemStack> getInputs() {
        return inputs;
    }

    @Override
    public int getBurnTime() {
        return burnTime;
    }
}
