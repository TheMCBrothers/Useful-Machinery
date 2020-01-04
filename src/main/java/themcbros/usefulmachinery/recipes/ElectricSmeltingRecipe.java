package themcbros.usefulmachinery.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import themcbros.usefulmachinery.init.ModItems;
import themcbros.usefulmachinery.init.ModRecipeSerializers;

public class ElectricSmeltingRecipe extends AbstractCookingRecipe {

    public ElectricSmeltingRecipe(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, ItemStack resultIn, float experienceIn, int cookTimeIn) {
        super(ModRecipeTypes.ELECTRIC_SMELTING, idIn, groupIn, ingredientIn, resultIn, experienceIn, cookTimeIn);
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(ModItems.ELECTRIC_SMELTER);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.ELECTRIC_SMELTING;
    }
}
