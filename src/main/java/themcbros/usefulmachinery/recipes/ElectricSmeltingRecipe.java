package themcbros.usefulmachinery.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import themcbros.usefulmachinery.init.MachineryBlocks;
import themcbros.usefulmachinery.init.MachineryRecipeSerializers;

public class ElectricSmeltingRecipe extends AbstractCookingRecipe {
    public ElectricSmeltingRecipe(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, ItemStack resultIn, float experienceIn, int cookTimeIn) {
        super(MachineryRecipeTypes.ELECTRIC_SMELTING.get(), idIn, groupIn, CookingBookCategory.MISC, ingredientIn, resultIn, experienceIn, cookTimeIn);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(MachineryBlocks.ELECTRIC_SMELTER.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MachineryRecipeSerializers.ELECTRIC_SMELTING.get();
    }
}
