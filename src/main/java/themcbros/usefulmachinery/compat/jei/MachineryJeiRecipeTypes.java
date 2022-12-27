package themcbros.usefulmachinery.compat.jei;

import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.recipes.CoalGeneratingRecipe;
import themcbros.usefulmachinery.recipes.CompactingRecipe;
import themcbros.usefulmachinery.recipes.CrushingRecipe;
import themcbros.usefulmachinery.recipes.LavaGeneratingRecipe;

public class MachineryJeiRecipeTypes {
    public static final RecipeType<CrushingRecipe> CRUSHING = RecipeType.create(UsefulMachinery.MOD_ID, "crushing", CrushingRecipe.class);
    public static final RecipeType<CompactingRecipe> COMPACTING = RecipeType.create(UsefulMachinery.MOD_ID, "compacting", CompactingRecipe.class);
    public static final RecipeType<IJeiFuelingRecipe> LAVA_GENERATING = RecipeType.create(UsefulMachinery.MOD_ID, "lava_generating", LavaGeneratingRecipe.class);
    public static final RecipeType<IJeiFuelingRecipe> COAL_GENERATING = RecipeType.create(UsefulMachinery.MOD_ID, "coal_generating", CoalGeneratingRecipe.class);
}
