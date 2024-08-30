package net.themcbrothers.usefulmachinery.compat.jei;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.crafting.*;

import java.util.List;

public class UsefulMachineryRecipeValidator {
    public static <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> getRecipes(RecipeType<T> type) {
        List<RecipeHolder<T>> results = Lists.newArrayList();
        ClientLevel world = Minecraft.getInstance().level;

        if (world != null) {
            RecipeManager recipeManager = world.getRecipeManager();

            results.addAll(recipeManager.getAllRecipesFor(type));
        }

        return results;
    }
}
