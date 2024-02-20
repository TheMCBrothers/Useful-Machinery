package net.themcbrothers.usefulmachinery.core;

import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.themcbrothers.usefulmachinery.recipe.ingredient.CountIngredient;

import static net.themcbrothers.usefulmachinery.core.Registration.INGREDIENT_TYPES;

/**
 * @deprecated Please use the themcbroslib version
 */
public class MachineryIngredientTypes {
    public static final DeferredHolder<IngredientType<?>, IngredientType<CountIngredient>> COUNT_INGREDIENT = INGREDIENT_TYPES.register("count", () -> new IngredientType<>(CountIngredient.CODEC));

    static void init() {
    }
}
