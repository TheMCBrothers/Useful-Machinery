package themcbros.usefulmachinery.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.client.gui.CoalGeneratorScreen;
import themcbros.usefulmachinery.client.gui.CrusherScreen;
import themcbros.usefulmachinery.compat.jei.crusher.CrushingCategory;
import themcbros.usefulmachinery.container.CoalGeneratorContainer;
import themcbros.usefulmachinery.container.CrusherContainer;
import themcbros.usefulmachinery.init.ModItems;

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
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(UsefulMachineryRecipeValidator.getCrusherRecipes(), ModRecipeCategoryUid.CRUSHING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(CoalGeneratorContainer.class, VanillaRecipeCategoryUid.FUEL, 0, 1, 2, 36);
        registration.addRecipeTransferHandler(CrusherContainer.class, ModRecipeCategoryUid.CRUSHING, 0, 1, 4, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.COAL_GENERATOR), VanillaRecipeCategoryUid.FUEL);
        registration.addRecipeCatalyst(new ItemStack(ModItems.CRUSHER), ModRecipeCategoryUid.CRUSHING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CoalGeneratorScreen.class, 54, 34, 14, 14, VanillaRecipeCategoryUid.FUEL);
        registration.addRecipeClickArea(CrusherScreen.class, 58, 34, 28, 23, ModRecipeCategoryUid.CRUSHING);
    }
}
