package themcbros.usefulmachinery.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SingleItemRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import themcbros.usefulmachinery.init.ModItems;
import themcbros.usefulmachinery.init.ModRecipeSerializers;

public class CompactingRecipe extends SingleItemRecipe {
    public CompactingRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result) {
        super(ModRecipeTypes.COMPACTING, ModRecipeSerializers.COMPACTING, id, group, ingredient, result);
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return this.ingredient.test(inv.getStackInSlot(0));
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(ModItems.COMPACTOR);
    }
}
