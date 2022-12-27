package themcbros.usefulmachinery.recipes;

import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraftforge.common.ForgeHooks;

import java.util.Comparator;
import java.util.List;

public class LavaGeneratingRecipeMaker {
    public static List<IJeiFuelingRecipe> getLavaGeneratingRecipes(IIngredientManager ingredientManager) {
        return ingredientManager.getAllItemStacks().stream()
                .<IJeiFuelingRecipe>mapMulti((stack, consumer) -> {
                    int burnTime = ForgeHooks.getBurnTime(stack, null);
                    if (burnTime == 20000) {
                        consumer.accept(new LavaGeneratingRecipe(List.of(stack), burnTime));
                    }
                })
                .sorted(Comparator.comparingInt(IJeiFuelingRecipe::getBurnTime))
                .toList();
    }
}
