package themcbros.usefulmachinery.compat.jei.categories;

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
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.compat.jei.MachineryJeiRecipeTypes;
import themcbros.usefulmachinery.init.MachineryBlocks;
import themcbros.usefulmachinery.recipes.CrushingRecipe;

import static themcbros.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public class CrushingCategory implements IRecipeCategory<CrushingRecipe> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.getId("textures/gui/container/crusher.png");

    private final IDrawable icon, background;
    private final IDrawableAnimated arrow, energyBar;

    public CrushingCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MachineryBlocks.CRUSHER.get()));
        this.background = helper.createDrawable(TEXTURES, 34, 16, 132, 52);
        this.arrow = helper.drawableBuilder(TEXTURES, 176, 14, 24, 17).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        this.energyBar = helper.drawableBuilder(TEXTURES, 246, 0, 10, 50).buildAnimated(200, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override
    public void draw(CrushingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        this.arrow.draw(graphics, 24, 18);
        this.energyBar.draw(graphics, 121, 1);

        drawChance(recipe, graphics);
    }

    @Override
    public RecipeType<CrushingRecipe> getRecipeType() {
        return MachineryJeiRecipeTypes.CRUSHING;
    }

    protected void drawChance(CrushingRecipe recipe, GuiGraphics graphics) {
        float secondaryChance = recipe.getSecondaryChance();
        if (secondaryChance > 0) {
            float secondaryChanceInPercent = secondaryChance * 100;

            Component text = Component.translatable(secondaryChanceInPercent + "%");
            Minecraft minecraft = Minecraft.getInstance();

            graphics.drawString(minecraft.font, text, 79, 39, 0xFF808080);
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
    public void setRecipe(IRecipeLayoutBuilder builder, CrushingRecipe recipe, IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 19).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 8).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 32).addItemStack(recipe.getSecondRecipeOutput());
    }
}
