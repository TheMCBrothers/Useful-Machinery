package net.themcbrothers.usefulmachinery.compat.jei.recipes;

import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class CoalGeneratingRecipeMaker {
    public static List<IJeiFuelingRecipe> getRecipes(IIngredientManager ingredientManager) {
        ClientLevel level = Objects.requireNonNull(Minecraft.getInstance().level);

        return ingredientManager.getAllItemStacks().stream()
                .<IJeiFuelingRecipe>mapMulti((stack, consumer) -> {
                    int burnTime = stack.getBurnTime(null, level.fuelValues());
                    try {
                        consumer.accept(new CoalGeneratingRecipe(List.of(stack), burnTime, stack.tags().toList()));
                    } catch (IllegalArgumentException _) {
                    }
                })
                .sorted(Comparator.comparingInt(IJeiFuelingRecipe::getBurnTime))
                .toList();
    }
}
