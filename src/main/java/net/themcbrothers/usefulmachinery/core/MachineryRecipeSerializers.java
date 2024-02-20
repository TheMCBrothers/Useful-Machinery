package net.themcbrothers.usefulmachinery.core;

import com.google.gson.JsonArray;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.themcbrothers.usefulmachinery.recipe.CompactingRecipe;
import net.themcbrothers.usefulmachinery.recipe.CrushingRecipe;

import static net.themcbrothers.usefulmachinery.core.Registration.RECIPE_SERIALIZERS;


public class MachineryRecipeSerializers {
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CrushingRecipe>> CRUSHING = RECIPE_SERIALIZERS.register("crushing", CrushingRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CompactingRecipe>> COMPACTING = RECIPE_SERIALIZERS.register("compacting", CompactingRecipe.Serializer::new);

    static void init() {
    }
}
