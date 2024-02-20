package net.themcbrothers.usefulmachinery.core;

import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.themcbrothers.usefulmachinery.recipe.CompactingRecipe;
import net.themcbrothers.usefulmachinery.recipe.CrushingRecipe;

import static net.themcbrothers.usefulmachinery.core.Registration.RECIPE_TYPES;

public class MachineryRecipeTypes {
    public static final DeferredHolder<RecipeType<?>, RecipeType<CrushingRecipe>> CRUSHING = RECIPE_TYPES.register("crushing", () -> new RecipeType<>() {
    });
    public static final DeferredHolder<RecipeType<?>, RecipeType<CompactingRecipe>> COMPACTING = RECIPE_TYPES.register("compacting", () -> new RecipeType<>() {
    });

    static void init() {
    }
}
