package themcbros.usefulmachinery.compat.jei;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public class UsefulMachineryRecipeValidator {
    public static <T extends Recipe<?>> List<T> getRecipes(RecipeType<T> type, Class<? extends T> clazz) {
        List<T> results = Lists.newArrayList();
        ClientLevel world = Minecraft.getInstance().level;

        assert world != null;
        RecipeManager recipeManager = world.getRecipeManager();

        for (Recipe<?> recipe : recipeManager.getRecipes()) {
            if (recipe.getType() == type) {
                results.add(clazz.cast(recipe));
            }
        }

        return results;
    }
}
