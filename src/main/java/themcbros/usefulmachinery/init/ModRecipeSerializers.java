package themcbros.usefulmachinery.init;

import com.google.common.collect.Lists;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.recipes.CrusherRecipe;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = UsefulMachinery.MOD_ID)
public class ModRecipeSerializers {

    private static final List<IRecipeSerializer<?>> RECIPE_SERIALIZERS = Lists.newArrayList();

    public static final IRecipeSerializer<?> CRUSHING = register("crushing", new CrusherRecipe.Serializer());

    private static <T extends IRecipeSerializer<? extends IRecipe<?>>> T register(String registryName, T serializer) {
        serializer.setRegistryName(UsefulMachinery.getId(registryName));
        RECIPE_SERIALIZERS.add(serializer);
        return serializer;
    }

    @SubscribeEvent
    public static void onBlockRegistry(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
        RECIPE_SERIALIZERS.forEach(event.getRegistry()::register);
    }

}
