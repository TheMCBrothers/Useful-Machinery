package themcbros.usefulmachinery.compat.jei;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import themcbros.usefulmachinery.recipes.CrusherRecipe;
import themcbros.usefulmachinery.recipes.ModRecipeTypes;

import java.util.List;

public class UsefulMachineryRecipeValidator {

    public static List<CrusherRecipe> getCrusherRecipes() {
        List<CrusherRecipe> results = Lists.newArrayList();
        ClientWorld world = Minecraft.getInstance().world;
        RecipeManager recipeManager = world.getRecipeManager();
        for (IRecipe<?> recipe : recipeManager.getRecipes()) {
            if (recipe.getType() == ModRecipeTypes.CRUSHING) {
                CrusherRecipe crusherRecipe = (CrusherRecipe) recipe;
                results.add(crusherRecipe);
            }
        }
        return results;
    }

}
