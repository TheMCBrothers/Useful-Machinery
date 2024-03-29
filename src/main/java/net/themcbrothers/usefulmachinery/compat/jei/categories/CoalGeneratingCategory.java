package net.themcbrothers.usefulmachinery.compat.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.compat.jei.MachineryJeiRecipeTypes;
import net.themcbrothers.usefulmachinery.core.MachineryBlocks;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public class CoalGeneratingCategory implements IRecipeCategory<IJeiFuelingRecipe> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.rl("textures/gui/container/coal_generator.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated energyBar;
    private final IDrawableAnimated fire;

    public CoalGeneratingCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MachineryBlocks.COAL_GENERATOR.get()));
        this.background = helper.createDrawable(TEXTURES, 34, 16, 132, 52);
        this.energyBar = helper.drawableBuilder(TEXTURES, 246, 0, 10, 50)
                .buildAnimated(200, IDrawableAnimated.StartDirection.BOTTOM, false);
        this.fire = helper.drawableBuilder(TEXTURES, 176, 0, 14, 14)
                .buildAnimated(400, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override
    public void draw(IJeiFuelingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        this.energyBar.draw(graphics, 121, 1);
        this.fire.draw(graphics, 20, 18);
    }

    @Override
    public RecipeType<IJeiFuelingRecipe> getRecipeType() {
        return MachineryJeiRecipeTypes.COAL_GENERATING;
    }

    @Override
    public Component getTitle() {
        return TEXT_UTILS.translate("jei", "fuel");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IJeiFuelingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 46, 17).addItemStacks(recipe.getInputs());
    }
}
