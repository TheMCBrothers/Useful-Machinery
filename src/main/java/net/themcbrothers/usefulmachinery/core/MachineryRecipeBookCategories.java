package net.themcbrothers.usefulmachinery.core;

import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.neoforged.neoforge.registries.DeferredHolder;

import static net.themcbrothers.usefulmachinery.core.Registration.RECIPE_BOOK_CATEGORIES;

public class MachineryRecipeBookCategories {
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> CRUSHING = RECIPE_BOOK_CATEGORIES.register("crushing", RecipeBookCategory::new);
    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> COMPACTING = RECIPE_BOOK_CATEGORIES.register("compacting", RecipeBookCategory::new);

    static void init() {
    }
}
