package net.themcbrothers.usefulmachinery.compat.jei.recipes;

import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class LavaGeneratingRecipeMaker {
    public static List<IJeiFuelingRecipe> getRecipes(IIngredientManager ingredientManager) {
        ClientLevel level = Objects.requireNonNull(Minecraft.getInstance().level);

        return ingredientManager.getAllItemStacks().stream()
                .<IJeiFuelingRecipe>mapMulti((stack, consumer) -> {
                    int burnTime = stack.getBurnTime(null, level.fuelValues());
                    List<TagKey<Item>> tags = stack.tags().toList();

                    try {
                        consumer.accept(new LavaGeneratingRecipe(List.of(stack), burnTime, tags));
                    } catch (IllegalArgumentException _) {
                    }
                })
                .sorted(Comparator.comparingInt(IJeiFuelingRecipe::getBurnTime))
                .toList();
    }
}
