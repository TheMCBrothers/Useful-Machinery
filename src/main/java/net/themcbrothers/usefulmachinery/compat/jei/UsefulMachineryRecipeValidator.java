package net.themcbrothers.usefulmachinery.compat.jei;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public class UsefulMachineryRecipeValidator {
    public static <T extends Recipe<?>> List<RecipeHolder<T>> getRecipes(RecipeType<T> type, Class<T> clazz) {
        List<RecipeHolder<T>> results = Lists.newArrayList();
        ClientLevel world = Minecraft.getInstance().level;

        if (world != null) {
            RecipeManager recipeManager = world.getRecipeManager();

            for (RecipeHolder<?> recipe : recipeManager.getRecipes()) {
                if (recipe.value().getType() == type) {
                    results.add(new RecipeHolder<>(recipe.id(), clazz.cast(recipe.value())));
                }
            }
        }

        return results;
    }
}
