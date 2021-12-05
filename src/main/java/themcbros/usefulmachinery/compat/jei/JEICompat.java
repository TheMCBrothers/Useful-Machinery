package themcbros.usefulmachinery.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.*;
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
import themcbros.usefulmachinery.init.ModItems;
import themcbros.usefulmachinery.recipes.CompactingRecipe;
import themcbros.usefulmachinery.recipes.CrushingRecipe;
import themcbros.usefulmachinery.recipes.ModRecipeTypes;

@JeiPlugin
public class JEICompat implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return UsefulMachinery.getId("jeicompat");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers helpers = registration.getJeiHelpers();
        registration.addRecipeCategories(new CrushingCategory(helpers.getGuiHelper()));
        registration.addRecipeCategories(new CompactingCategory(helpers.getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(UsefulMachineryRecipeValidator.getRecipes(ModRecipeTypes.CRUSHING, CrushingRecipe.class), ModRecipeCategoryUid.CRUSHING);
        registration.addRecipes(UsefulMachineryRecipeValidator.getRecipes(ModRecipeTypes.COMPACTING, CompactingRecipe.class), ModRecipeCategoryUid.COMPACTING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(CoalGeneratorContainer.class, VanillaRecipeCategoryUid.FUEL, 0, 1, 2, 36);
        registration.addRecipeTransferHandler(CrusherContainer.class, ModRecipeCategoryUid.CRUSHING, 0, 1, 4, 36);
        registration.addRecipeTransferHandler(ElectricSmelterContainer.class, VanillaRecipeCategoryUid.FURNACE, 0, 1, 3, 36);
        registration.addRecipeTransferHandler(ElectricSmelterContainer.class, VanillaRecipeCategoryUid.BLASTING, 0, 1, 3, 36);
        registration.addRecipeTransferHandler(CompactorContainer.class, ModRecipeCategoryUid.COMPACTING, 0, 1, 3, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.COAL_GENERATOR), VanillaRecipeCategoryUid.FUEL);
        registration.addRecipeCatalyst(new ItemStack(ModItems.CRUSHER), ModRecipeCategoryUid.CRUSHING);
        registration.addRecipeCatalyst(new ItemStack(ModItems.ELECTRIC_SMELTER), VanillaRecipeCategoryUid.FURNACE, VanillaRecipeCategoryUid.BLASTING);
        registration.addRecipeCatalyst(new ItemStack(ModItems.COMPACTOR), ModRecipeCategoryUid.COMPACTING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CoalGeneratorScreen.class, 54, 34, 14, 14, VanillaRecipeCategoryUid.FUEL);
        registration.addRecipeClickArea(CrusherScreen.class, 58, 34, 28, 23, ModRecipeCategoryUid.CRUSHING);
        registration.addRecipeClickArea(ElectricSmelterScreen.class, 58, 32, 24, 17, VanillaRecipeCategoryUid.FURNACE, VanillaRecipeCategoryUid.BLASTING);
        registration.addRecipeClickArea(CompactorScreen.class, 58, 32, 24, 17, ModRecipeCategoryUid.COMPACTING);
    }
}
