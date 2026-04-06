package net.themcbrothers.usefulmachinery.util;

import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.themcbrothers.usefulmachinery.core.MachineryRecipePropertySet;
import net.themcbrothers.usefulmachinery.recipe.CompactingRecipe;
import net.themcbrothers.usefulmachinery.recipe.CrushingRecipe;

import java.util.Optional;

public class RecipeHelper {
    private static RecipeManager recipeManager;

    @SubscribeEvent
    public void onAddReloadListeners(AddServerReloadListenersEvent event) {
        recipeManager = event.getServerResources().getRecipeManager();

        net.themcbrothers.lib.util.RecipeHelper.addPropertySet(MachineryRecipePropertySet.CRUSHER_INPUT,
                recipe -> recipe instanceof CrushingRecipe crushingRecipe ? Optional.of(crushingRecipe.ingredient()) : Optional.empty());
        net.themcbrothers.lib.util.RecipeHelper.addPropertySet(MachineryRecipePropertySet.COMPACTOR_INPUT,
                recipe -> recipe instanceof CompactingRecipe compactingRecipe ? Optional.of(compactingRecipe.sizedIngredient().ingredient()) : Optional.empty());
    }

    public static RecipeManager getRecipeManager() {
        return recipeManager;
    }

    public static void addRecipe(RecipeHolder<?> recipe) {
        // TODO: fix recipes
    }
}
