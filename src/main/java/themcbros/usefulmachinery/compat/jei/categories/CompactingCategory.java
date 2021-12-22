package themcbros.usefulmachinery.compat.jei.categories;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.compat.jei.ModRecipeCategoryUid;
import themcbros.usefulmachinery.init.MachineryItems;
import themcbros.usefulmachinery.recipes.CompactingRecipe;
import themcbros.usefulmachinery.util.TextUtils;

import java.util.Arrays;

public class CompactingCategory implements IRecipeCategory<CompactingRecipe> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.getId("textures/gui/container/electric_smelter.png");

    private final IDrawable icon, background;
    private final IDrawableAnimated arrow, energyBar;

    public CompactingCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(new ItemStack(MachineryItems.COMPACTOR));
        this.background = helper.createDrawable(TEXTURES, 34, 16, 132, 52);

        this.arrow = helper.drawableBuilder(TEXTURES, 176, 14, 24, 17).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        this.energyBar = helper.drawableBuilder(TEXTURES, 246, 0, 10, 50).buildAnimated(200, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override
    public void draw(CompactingRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
        this.arrow.draw(poseStack, 24, 16);
        this.energyBar.draw(poseStack, 121, 1);
    }

    @Override
    public ResourceLocation getUid() {
        return ModRecipeCategoryUid.COMPACTING;
    }

    @Override
    public Class<? extends CompactingRecipe> getRecipeClass() {
        return CompactingRecipe.class;
    }

    @Override
    public Component getTitle() {
        return TextUtils.translate("jei", "compacting");
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
    public void setIngredients(CompactingRecipe recipe, IIngredients ingredients) {
        ItemStack[] stacks = recipe.getIngredients().get(0).getItems();

        for (ItemStack stack : stacks) {
            stack.setCount(recipe.getCount());
        }

        ingredients.setInputs(VanillaTypes.ITEM, Arrays.asList(stacks));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CompactingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 0, 16);
        guiItemStacks.init(1, false, 60, 16);
        guiItemStacks.set(ingredients);
    }
}
