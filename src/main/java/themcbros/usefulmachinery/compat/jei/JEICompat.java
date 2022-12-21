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
import themcbros.usefulmachinery.compat.jei.categories.CompactingCategory;
import themcbros.usefulmachinery.compat.jei.categories.CrushingCategory;
import themcbros.usefulmachinery.container.CoalGeneratorContainer;
import themcbros.usefulmachinery.container.CompactorContainer;
import themcbros.usefulmachinery.container.CrusherContainer;
import themcbros.usefulmachinery.container.ElectricSmelterContainer;
import themcbros.usefulmachinery.recipes.CompactingRecipe;
import themcbros.usefulmachinery.recipes.CrushingRecipe;
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
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(MachineryJeiRecipeTypes.CRUSHING, UsefulMachineryRecipeValidator.getRecipes(MachineryRecipeTypes.CRUSHING.get(), CrushingRecipe.class));
        registration.addRecipes(MachineryJeiRecipeTypes.COMPACTING, UsefulMachineryRecipeValidator.getRecipes(MachineryRecipeTypes.COMPACTING.get(), CompactingRecipe.class));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(CoalGeneratorContainer.class, RecipeTypes.FUELING, 0, 1, 2, 36);
        registration.addRecipeTransferHandler(CrusherContainer.class, MachineryJeiRecipeTypes.CRUSHING, 0, 1, 4, 36);
        registration.addRecipeTransferHandler(ElectricSmelterContainer.class, RecipeTypes.SMELTING, 0, 1, 3, 36);
        registration.addRecipeTransferHandler(ElectricSmelterContainer.class, RecipeTypes.BLASTING, 0, 1, 3, 36);
        registration.addRecipeTransferHandler(CompactorContainer.class, MachineryJeiRecipeTypes.COMPACTING, 0, 1, 3, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(COAL_GENERATOR.get()), RecipeTypes.FUELING);
        registration.addRecipeCatalyst(new ItemStack(CRUSHER.get()), MachineryJeiRecipeTypes.CRUSHING);
        registration.addRecipeCatalyst(new ItemStack(ELECTRIC_SMELTER.get()), RecipeTypes.SMELTING, RecipeTypes.BLASTING);
        registration.addRecipeCatalyst(new ItemStack(COMPACTOR.get()), MachineryJeiRecipeTypes.COMPACTING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CoalGeneratorScreen.class, 54, 34, 14, 14, RecipeTypes.FUELING);
        registration.addRecipeClickArea(CrusherScreen.class, 58, 34, 28, 23, MachineryJeiRecipeTypes.CRUSHING);
        registration.addRecipeClickArea(ElectricSmelterScreen.class, 58, 32, 24, 17, RecipeTypes.SMELTING, RecipeTypes.BLASTING);
        registration.addRecipeClickArea(CompactorScreen.class, 58, 32, 24, 17, MachineryJeiRecipeTypes.COMPACTING);
    }
}
