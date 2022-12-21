package themcbros.usefulmachinery.compat.jei;

import mezz.jei.api.recipe.RecipeType;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.recipes.CompactingRecipe;
import themcbros.usefulmachinery.recipes.CrushingRecipe;

public class MachineryJeiRecipeTypes {
    public static final RecipeType<CrushingRecipe> CRUSHING = RecipeType.create(UsefulMachinery.MOD_ID, "crushing", CrushingRecipe.class);
    public static final RecipeType<CompactingRecipe> COMPACTING = RecipeType.create(UsefulMachinery.MOD_ID, "compacting", CompactingRecipe.class);
}
