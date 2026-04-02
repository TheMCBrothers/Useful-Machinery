package net.themcbrothers.usefulmachinery.compat.jei;

import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.compat.jei.recipes.CoalGeneratingRecipe;
import net.themcbrothers.usefulmachinery.compat.jei.recipes.LavaGeneratingRecipe;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeTypes;
import net.themcbrothers.usefulmachinery.recipe.CompactingRecipe;
import net.themcbrothers.usefulmachinery.recipe.CrushingRecipe;

public class MachineryJeiRecipeTypes {
    public static final IRecipeType<RecipeHolder<CrushingRecipe>> CRUSHING = IRecipeHolderType.create(MachineryRecipeTypes.CRUSHING.get());
    public static final IRecipeType<RecipeHolder<CompactingRecipe>> COMPACTING = IRecipeHolderType.create(MachineryRecipeTypes.COMPACTING.get());
    public static final IRecipeType<IJeiFuelingRecipe> LAVA_GENERATING = IRecipeType.create(UsefulMachinery.MOD_ID, "lava_generating", LavaGeneratingRecipe.class);
    public static final IRecipeType<IJeiFuelingRecipe> COAL_GENERATING = IRecipeType.create(UsefulMachinery.MOD_ID, "coal_generating", CoalGeneratingRecipe.class);
}
