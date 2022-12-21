package themcbros.usefulmachinery.init;

import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraftforge.registries.RegistryObject;
import themcbros.usefulmachinery.recipes.CompactingRecipe;
import themcbros.usefulmachinery.recipes.CrushingRecipe;
import themcbros.usefulmachinery.recipes.ElectricSmeltingRecipe;

import static themcbros.usefulmachinery.init.Registration.RECIPE_SERIALIZERS;

public class MachineryRecipeSerializers {
    public static final RegistryObject<CrushingRecipe.Serializer> CRUSHING = RECIPE_SERIALIZERS.register("crushing", CrushingRecipe.Serializer::new);
    public static final RegistryObject<SimpleCookingSerializer<ElectricSmeltingRecipe>> ELECTRIC_SMELTING =  null; //RECIPE_SERIALIZERS.register("electric_smelting", new SimpleCookingSerializer<>(ElectricSmeltingRecipe::new, 100));
    public static final RegistryObject<CompactingRecipe.Serializer> COMPACTING = RECIPE_SERIALIZERS.register("compacting", CompactingRecipe.Serializer::new);

    protected static void init() {
    }
}
