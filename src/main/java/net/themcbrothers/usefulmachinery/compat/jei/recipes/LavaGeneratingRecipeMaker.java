package net.themcbrothers.usefulmachinery.compat.jei.recipes;

import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import mezz.jei.api.runtime.IIngredientManager;
import net.neoforged.neoforge.common.CommonHooks;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class LavaGeneratingRecipeMaker {
    public static List<IJeiFuelingRecipe> getLavaGeneratingRecipes(IIngredientManager ingredientManager) {
        return Objects.requireNonNull(ingredientManager).getAllItemStacks().stream()
                .<IJeiFuelingRecipe>mapMulti((stack, consumer) -> {
                    int burnTime = CommonHooks.getBurnTime(stack, null);

                    if (burnTime == 20000) {
                        consumer.accept(new LavaGeneratingRecipe(List.of(stack), burnTime));
                    }
                })
                .sorted(Comparator.comparingInt(IJeiFuelingRecipe::getBurnTime))
                .toList();
    }
}
