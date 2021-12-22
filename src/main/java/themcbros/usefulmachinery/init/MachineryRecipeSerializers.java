package themcbros.usefulmachinery.init;

import com.google.common.collect.Lists;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.recipes.CompactingRecipe;
import themcbros.usefulmachinery.recipes.CrushingRecipe;
import themcbros.usefulmachinery.recipes.ElectricSmeltingRecipe;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = UsefulMachinery.MOD_ID)
public class MachineryRecipeSerializers {
    private static final List<RecipeSerializer<?>> RECIPE_SERIALIZERS = Lists.newArrayList();

    public static final CrushingRecipe.Serializer CRUSHING = register("crushing", new CrushingRecipe.Serializer());
    public static final SimpleCookingSerializer<ElectricSmeltingRecipe> ELECTRIC_SMELTING = null; // register("electric_smelting", new CookingRecipeSerializer<>(ElectricSmeltingRecipe::new, 100));
    public static final CompactingRecipe.Serializer COMPACTING = register("compacting", new CompactingRecipe.Serializer());

    private static <T extends RecipeSerializer<? extends Recipe<?>>> T register(String registryName, T serializer) {
        serializer.setRegistryName(UsefulMachinery.getId(registryName));
        RECIPE_SERIALIZERS.add(serializer);
        return serializer;
    }

    @SubscribeEvent
    public static void onBlockRegistry(final RegistryEvent.Register<RecipeSerializer<?>> event) {
        RECIPE_SERIALIZERS.forEach(event.getRegistry()::register);
    }
}
