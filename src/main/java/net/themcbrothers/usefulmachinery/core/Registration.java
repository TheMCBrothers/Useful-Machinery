package net.themcbrothers.usefulmachinery.core;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.themcbrothers.lib.registries.BlockDeferredRegister;
import net.themcbrothers.lib.registries.BlockEntityTypeDeferredRegister;
import net.themcbrothers.lib.registries.ItemDeferredRegister;
import net.themcbrothers.lib.registries.MenuTypeDeferredRegister;
import net.themcbrothers.usefulmachinery.UsefulMachinery;

public final class Registration {
    public static final BlockEntityTypeDeferredRegister BLOCK_ENTITY_TYPES = BlockEntityTypeDeferredRegister.create(UsefulMachinery.MOD_ID);
    public static final MenuTypeDeferredRegister MENUS = MenuTypeDeferredRegister.create(UsefulMachinery.MOD_ID);
    public static final ItemDeferredRegister ITEMS = ItemDeferredRegister.create(UsefulMachinery.MOD_ID);
    public static final BlockDeferredRegister BLOCKS = BlockDeferredRegister.create(UsefulMachinery.MOD_ID, ITEMS);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UsefulMachinery.MOD_ID);
    public static final DeferredRegister<ResourceLocation> CUSTOM_STATS = DeferredRegister.create(Registries.CUSTOM_STAT, UsefulMachinery.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, UsefulMachinery.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, UsefulMachinery.MOD_ID);
    /**
     * @deprecated Please remove me soon! ;-)
     */
    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, UsefulMachinery.MOD_ID);

    public static void register(IEventBus modEventBus) {
        MachineryBlockEntities.init();
        MachineryMenus.init();
        MachineryItems.init();
        MachineryBlocks.init();
        MachineryTabs.init();
        MachineryStats.init();
        MachineryRecipeTypes.init();
        MachineryRecipeSerializers.init();
        MachineryIngredientTypes.init();

        BLOCK_ENTITY_TYPES.register(modEventBus);
        MENUS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        CUSTOM_STATS.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        INGREDIENT_TYPES.register(modEventBus);
    }
}
