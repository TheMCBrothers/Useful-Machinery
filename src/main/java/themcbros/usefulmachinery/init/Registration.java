package themcbros.usefulmachinery.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.themcbrothers.lib.registration.deferred.BlockDeferredRegister;
import net.themcbrothers.lib.registration.deferred.BlockEntityDeferredRegister;
import net.themcbrothers.lib.registration.deferred.ItemDeferredRegister;
import net.themcbrothers.lib.registration.deferred.MenuTypeDeferredRegister;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.recipes.MachineryRecipeTypes;

public class Registration {
    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(UsefulMachinery.MOD_ID);
    public static final BlockEntityDeferredRegister BLOCK_ENTITY_TYPES = new BlockEntityDeferredRegister(UsefulMachinery.MOD_ID);
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(UsefulMachinery.MOD_ID);
    public static final MenuTypeDeferredRegister MENUS = new MenuTypeDeferredRegister(UsefulMachinery.MOD_ID);
    public static final DeferredRegister<ResourceLocation> CUSTOM_STATS = DeferredRegister.create(Registry.CUSTOM_STAT_REGISTRY, UsefulMachinery.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, UsefulMachinery.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, UsefulMachinery.MOD_ID);

    public static void register(IEventBus bus) {
        MachineryBlocks.init();
        MachineryBlockEntities.init();
        MachineryItems.init();
        MachineryMenus.init();
        MachineryStats.init();
        MachineryRecipeTypes.init();
        MachineryRecipeSerializers.init();

        BLOCKS.register(bus);
        BLOCK_ENTITY_TYPES.register(bus);
        ITEMS.register(bus);
        MENUS.register(bus);
        CUSTOM_STATS.register(bus);
        RECIPE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
    }
}
