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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.compat.jei.MachineryJeiRecipeTypes;
import net.themcbrothers.usefulmachinery.core.MachineryBlocks;
import net.themcbrothers.usefulmachinery.recipe.CrushingRecipe;

import java.util.Objects;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public class CrushingCategory implements IRecipeCategory<RecipeHolder<CrushingRecipe>> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.rl("textures/gui/container/crusher.png");

    private final IDrawable icon, background;
    private final IDrawableAnimated arrow, energyBar;

    public CrushingCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MachineryBlocks.CRUSHER.get()));
        this.background = helper.createDrawable(TEXTURES, 34, 16, 132, 52);
        this.arrow = helper.drawableBuilder(TEXTURES, 176, 14, 24, 17).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        this.energyBar = helper.drawableBuilder(TEXTURES, 246, 0, 10, 50).buildAnimated(200, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override
    public void draw(RecipeHolder<CrushingRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        this.arrow.draw(graphics, 24, 18);
        this.energyBar.draw(graphics, 121, 1);

        drawChance(recipe, graphics);
    }

    @Override
    public RecipeType<RecipeHolder<CrushingRecipe>> getRecipeType() {
        return MachineryJeiRecipeTypes.CRUSHING;
    }

    protected void drawChance(RecipeHolder<CrushingRecipe> recipe, GuiGraphics graphics) {
        float secondaryChance = recipe.value().secondaryChance();

        if (secondaryChance > 0) {
            float secondaryChanceInPercent = secondaryChance * 100;

            Component text = Component.translatable(secondaryChanceInPercent + "%");
            Minecraft minecraft = Minecraft.getInstance();

            graphics.drawString(minecraft.font, text, 84, 39, 0xFF808080, false);
        }
    }

    @Override
    public Component getTitle() {
        return TEXT_UTILS.translate("jei", "crushing");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CrushingRecipe> recipe, IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 19).addIngredients(recipe.value().getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 8).addItemStack(recipe.value().getResultItem(Objects.requireNonNull(Minecraft.getInstance().level).registryAccess()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 32).addItemStack(recipe.value().secondaryResult());
    }
}
