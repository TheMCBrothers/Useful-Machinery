package net.themcbrothers.usefulmachinery.compat.jei;

import net.minecraft.world.item.crafting.*;

import java.util.List;

public class UsefulMachineryRecipeValidator {
    public static <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> getRecipes(RecipeType<T> type, RecipeManager recipeManager) {
        return recipeManager.recipeMap().byType(type).stream().toList();
    }
}
