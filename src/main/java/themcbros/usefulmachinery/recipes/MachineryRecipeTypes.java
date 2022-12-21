package themcbros.usefulmachinery.recipes;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.RegistryObject;

import static themcbros.usefulmachinery.init.Registration.RECIPE_TYPES;

public class MachineryRecipeTypes {
    public static final RegistryObject<RecipeType<CrushingRecipe>> CRUSHING = RECIPE_TYPES.register("crushing", () -> RecipeType.register("usefulmachinery:crushing"));
    public static final RegistryObject<RecipeType<ElectricSmeltingRecipe>> ELECTRIC_SMELTING = RECIPE_TYPES.register("electric_smelting", () -> RecipeType.register("usefulmachinery:electric_smelting"));
    public static final RegistryObject<RecipeType<CompactingRecipe>> COMPACTING = RECIPE_TYPES.register("compacting", () -> RecipeType.register("usefulmachinery:compacting"));

    public static void init() {
    }
}
