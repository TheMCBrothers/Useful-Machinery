package themcbros.usefulmachinery.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.client.gui.CoalGeneratorScreen;
import themcbros.usefulmachinery.client.gui.CompactorScreen;
import themcbros.usefulmachinery.client.gui.CrusherScreen;
import themcbros.usefulmachinery.client.gui.ElectricSmelterScreen;
import themcbros.usefulmachinery.client.gui.LavaGeneratorScreen;
import themcbros.usefulmachinery.compat.jei.categories.CoalGeneratingCategory;
import themcbros.usefulmachinery.compat.jei.categories.CompactingCategory;
import themcbros.usefulmachinery.compat.jei.categories.CrushingCategory;
import themcbros.usefulmachinery.compat.jei.categories.LavaGeneratingCategory;
import themcbros.usefulmachinery.init.MachineryMenus;
import themcbros.usefulmachinery.menu.CoalGeneratorMenu;
import themcbros.usefulmachinery.menu.CompactorMenu;
import themcbros.usefulmachinery.menu.CrusherMenu;
import themcbros.usefulmachinery.menu.ElectricSmelterMenu;
import themcbros.usefulmachinery.menu.LavaGeneratorMenu;
import themcbros.usefulmachinery.recipes.CoalGeneratingRecipeMaker;
import themcbros.usefulmachinery.recipes.CompactingRecipe;
import themcbros.usefulmachinery.recipes.CrushingRecipe;
import themcbros.usefulmachinery.recipes.LavaGeneratingRecipeMaker;
import themcbros.usefulmachinery.recipes.MachineryRecipeTypes;

import static themcbros.usefulmachinery.init.MachineryBlocks.*;
import static themcbros.usefulmachinery.init.MachineryItems.TIER_UPGRADE;

@JeiPlugin
public class JEICompat implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return UsefulMachinery.getId("jeicompat");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.useNbtForSubtypes(TIER_UPGRADE.get());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers helpers = registration.getJeiHelpers();
        registration.addRecipeCategories(new CrushingCategory(helpers.getGuiHelper()));
        registration.addRecipeCategories(new CompactingCategory(helpers.getGuiHelper()));
        registration.addRecipeCategories(new LavaGeneratingCategory(helpers.getGuiHelper()));
        registration.addRecipeCategories(new CoalGeneratingCategory(helpers.getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(MachineryJeiRecipeTypes.CRUSHING, UsefulMachineryRecipeValidator.getRecipes(MachineryRecipeTypes.CRUSHING.get(), CrushingRecipe.class));
        registration.addRecipes(MachineryJeiRecipeTypes.COMPACTING, UsefulMachineryRecipeValidator.getRecipes(MachineryRecipeTypes.COMPACTING.get(), CompactingRecipe.class));
        registration.addRecipes(MachineryJeiRecipeTypes.LAVA_GENERATING, LavaGeneratingRecipeMaker.getLavaGeneratingRecipes(registration.getIngredientManager()));
        registration.addRecipes(MachineryJeiRecipeTypes.COAL_GENERATING, CoalGeneratingRecipeMaker.getCoalGeneratingRecipes(registration.getIngredientManager()));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(CoalGeneratorMenu.class, MachineryMenus.COAL_GENERATOR.get(), MachineryJeiRecipeTypes.COAL_GENERATING, 0, 1, 2, 36);
        registration.addRecipeTransferHandler(CrusherMenu.class, MachineryMenus.CRUSHER.get(), MachineryJeiRecipeTypes.CRUSHING, 0, 1, 4, 36);
        registration.addRecipeTransferHandler(ElectricSmelterMenu.class, MachineryMenus.ELECTRIC_SMELTER.get(), RecipeTypes.SMELTING, 0, 1, 3, 36);
        registration.addRecipeTransferHandler(ElectricSmelterMenu.class, MachineryMenus.ELECTRIC_SMELTER.get(), RecipeTypes.BLASTING, 0, 1, 3, 36);
        registration.addRecipeTransferHandler(CompactorMenu.class, MachineryMenus.COMPACTOR.get(), MachineryJeiRecipeTypes.COMPACTING, 0, 1, 3, 36);
        registration.addRecipeTransferHandler(LavaGeneratorMenu.class, MachineryMenus.LAVA_GENERATOR.get(), MachineryJeiRecipeTypes.LAVA_GENERATING, 0, 1, 2, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(COAL_GENERATOR.get()), MachineryJeiRecipeTypes.COAL_GENERATING);
        registration.addRecipeCatalyst(new ItemStack(CRUSHER.get()), MachineryJeiRecipeTypes.CRUSHING);
        registration.addRecipeCatalyst(new ItemStack(ELECTRIC_SMELTER.get()), RecipeTypes.SMELTING, RecipeTypes.BLASTING);
        registration.addRecipeCatalyst(new ItemStack(COMPACTOR.get()), MachineryJeiRecipeTypes.COMPACTING);
        registration.addRecipeCatalyst(new ItemStack(LAVA_GENERATOR.get()), MachineryJeiRecipeTypes.LAVA_GENERATING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CoalGeneratorScreen.class, 54, 34, 14, 14, MachineryJeiRecipeTypes.COAL_GENERATING);
        registration.addRecipeClickArea(CrusherScreen.class, 58, 34, 28, 23, MachineryJeiRecipeTypes.CRUSHING);
        registration.addRecipeClickArea(ElectricSmelterScreen.class, 58, 32, 24, 17, RecipeTypes.SMELTING, RecipeTypes.BLASTING);
        registration.addRecipeClickArea(CompactorScreen.class, 58, 32, 24, 17, MachineryJeiRecipeTypes.COMPACTING);
        registration.addRecipeClickArea(LavaGeneratorScreen.class, 81, 34, 14, 14, MachineryJeiRecipeTypes.LAVA_GENERATING);
    }
}
