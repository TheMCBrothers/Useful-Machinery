package themcbros.usefulmachinery.recipes;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.RegistryObject;

import static themcbros.usefulmachinery.init.Registration.RECIPE_TYPES;

public class MachineryRecipeTypes {
    public static final RegistryObject<RecipeType<CrushingRecipe>> CRUSHING = RECIPE_TYPES.register("crushing", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "usefulmachinery:crushing";
        }
    });
    public static final RegistryObject<RecipeType<ElectricSmeltingRecipe>> ELECTRIC_SMELTING = RECIPE_TYPES.register("electric_smelting", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "usefulmachinery:electric_smelting";
        }
    });
    public static final RegistryObject<RecipeType<CompactingRecipe>> COMPACTING = RECIPE_TYPES.register("compacting", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "usefulmachinery:compacting";
        }
    });

    public static void init() {
    }
}
