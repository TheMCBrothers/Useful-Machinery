package net.themcbrothers.usefulmachinery.compat.jei;

import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.compat.jei.recipes.CoalGeneratingRecipe;
import net.themcbrothers.usefulmachinery.compat.jei.recipes.LavaGeneratingRecipe;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeTypes;
import net.themcbrothers.usefulmachinery.recipe.CompactingRecipe;
import net.themcbrothers.usefulmachinery.recipe.CrushingRecipe;

public class MachineryJeiRecipeTypes {
    public static final RecipeType<RecipeHolder<CrushingRecipe>> CRUSHING = RecipeType.createFromVanilla(MachineryRecipeTypes.CRUSHING.get());
    public static final RecipeType<RecipeHolder<CompactingRecipe>> COMPACTING = RecipeType.createFromVanilla(MachineryRecipeTypes.COMPACTING.get());
    public static final RecipeType<IJeiFuelingRecipe> LAVA_GENERATING = RecipeType.create(UsefulMachinery.MOD_ID, "lava_generating", LavaGeneratingRecipe.class);
    public static final RecipeType<IJeiFuelingRecipe> COAL_GENERATING = RecipeType.create(UsefulMachinery.MOD_ID, "coal_generating", CoalGeneratingRecipe.class);
}
