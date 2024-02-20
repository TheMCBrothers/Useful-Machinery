package net.themcbrothers.usefulmachinery.compat.jei.recipes;

import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import mezz.jei.api.runtime.IIngredientManager;
import net.neoforged.neoforge.common.CommonHooks;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class CoalGeneratingRecipeMaker {
    public static List<IJeiFuelingRecipe> getCoalGeneratingRecipes(IIngredientManager ingredientManager) {
        return Objects.requireNonNull(ingredientManager).getAllItemStacks().stream()
                .<IJeiFuelingRecipe>mapMulti((stack, consumer) -> {
                    int burnTime = CommonHooks.getBurnTime(stack, null);

                    if (burnTime == 1600) {
                        consumer.accept(new CoalGeneratingRecipe(List.of(stack), burnTime));
                    }
                })
                .sorted(Comparator.comparingInt(IJeiFuelingRecipe::getBurnTime))
                .toList();
    }
}
