package themcbros.usefulmachinery.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import themcbros.usefulmachinery.init.ModItems;
import themcbros.usefulmachinery.init.ModRecipeSerializers;

public class ElectricSmeltingRecipe extends AbstractCookingRecipe {
    public ElectricSmeltingRecipe(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, ItemStack resultIn, float experienceIn, int cookTimeIn) {
        super(ModRecipeTypes.ELECTRIC_SMELTING, idIn, groupIn, ingredientIn, resultIn, experienceIn, cookTimeIn);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModItems.ELECTRIC_SMELTER);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.ELECTRIC_SMELTING;
    }
}
