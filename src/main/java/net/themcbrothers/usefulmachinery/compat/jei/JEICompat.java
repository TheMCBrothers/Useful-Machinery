package net.themcbrothers.usefulmachinery.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.*;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.client.screen.*;
import net.themcbrothers.usefulmachinery.compat.jei.categories.CoalGeneratingCategory;
import net.themcbrothers.usefulmachinery.compat.jei.categories.CompactingCategory;
import net.themcbrothers.usefulmachinery.compat.jei.categories.CrushingCategory;
import net.themcbrothers.usefulmachinery.compat.jei.categories.LavaGeneratingCategory;
import net.themcbrothers.usefulmachinery.compat.jei.recipes.CoalGeneratingRecipeMaker;
import net.themcbrothers.usefulmachinery.compat.jei.recipes.LavaGeneratingRecipeMaker;
import net.themcbrothers.usefulmachinery.core.MachineryDataComponentTypes;
import net.themcbrothers.usefulmachinery.core.MachineryMenus;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeTypes;
import net.themcbrothers.usefulmachinery.machine.MachineTier;
import net.themcbrothers.usefulmachinery.menu.*;
import net.themcbrothers.usefulmachinery.util.RecipeHelper;

import static net.themcbrothers.usefulmachinery.core.MachineryBlocks.*;
import static net.themcbrothers.usefulmachinery.core.MachineryItems.TIER_UPGRADE;

/**
 * JEI compatibility
 */
@JeiPlugin
public class JEICompat implements IModPlugin {
    @Override
    public Identifier getPluginUid() {
        return UsefulMachinery.id("jeicompat");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(TIER_UPGRADE.get(), (stack, _) -> stack.getOrDefault(MachineryDataComponentTypes.TIER, MachineTier.SIMPLE).getSerializedName());
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
        RecipeManager recipeManager = RecipeHelper.getRecipeManager();

        registration.addRecipes(MachineryJeiRecipeTypes.CRUSHING, UsefulMachineryRecipeValidator.getRecipes(MachineryRecipeTypes.CRUSHING.get(), recipeManager));
        registration.addRecipes(MachineryJeiRecipeTypes.COMPACTING, UsefulMachineryRecipeValidator.getRecipes(MachineryRecipeTypes.COMPACTING.get(), recipeManager));
        registration.addRecipes(MachineryJeiRecipeTypes.LAVA_GENERATING, LavaGeneratingRecipeMaker.getRecipes(registration.getIngredientManager()));
        registration.addRecipes(MachineryJeiRecipeTypes.COAL_GENERATING, CoalGeneratingRecipeMaker.getRecipes(registration.getIngredientManager()));
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
        registration.addCraftingStation(MachineryJeiRecipeTypes.COAL_GENERATING, COAL_GENERATOR.get());
        registration.addCraftingStation(MachineryJeiRecipeTypes.CRUSHING, CRUSHER.get());
        registration.addCraftingStation(RecipeTypes.SMELTING, ELECTRIC_SMELTER.get());
        registration.addCraftingStation(RecipeTypes.BLASTING, ELECTRIC_SMELTER.get());
        registration.addCraftingStation(MachineryJeiRecipeTypes.COMPACTING, COMPACTOR.get());
        registration.addCraftingStation(MachineryJeiRecipeTypes.LAVA_GENERATING, LAVA_GENERATOR.get());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(CoalGeneratorScreen.class, new MachineGuiHandler(54, 34, 14, 14, MachineryJeiRecipeTypes.COAL_GENERATING));
        registration.addGuiContainerHandler(CrusherScreen.class, new MachineGuiHandler(58, 34, 28, 23, MachineryJeiRecipeTypes.CRUSHING));
        registration.addGuiContainerHandler(ElectricSmelterScreen.class, new MachineGuiHandler(58, 32, 24, 17, RecipeTypes.SMELTING, RecipeTypes.BLASTING));
        registration.addGuiContainerHandler(CompactorScreen.class, new MachineGuiHandler(58, 32, 24, 17, MachineryJeiRecipeTypes.COMPACTING));
        registration.addGuiContainerHandler(LavaGeneratorScreen.class, new MachineGuiHandler(81, 34, 14, 14, MachineryJeiRecipeTypes.LAVA_GENERATING));
    }
}
