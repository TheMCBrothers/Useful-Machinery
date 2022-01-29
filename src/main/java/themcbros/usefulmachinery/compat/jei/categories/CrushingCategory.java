package themcbros.usefulmachinery.compat.jei.categories;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.compat.jei.ModRecipeCategoryUid;
import themcbros.usefulmachinery.init.MachineryItems;
import themcbros.usefulmachinery.recipes.CrushingRecipe;
import themcbros.usefulmachinery.util.TextUtils;

import java.util.List;

public class CrushingCategory implements IRecipeCategory<CrushingRecipe> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.getId("textures/gui/container/crusher.png");

    private final IDrawable icon, background;
    private final IDrawableAnimated arrow, energyBar;

    public CrushingCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(MachineryItems.CRUSHER));
        this.background = helper.createDrawable(TEXTURES, 34, 16, 132, 52);

        this.arrow = helper.drawableBuilder(TEXTURES, 176, 14, 24, 17).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        this.energyBar = helper.drawableBuilder(TEXTURES, 246, 0, 10, 50).buildAnimated(200, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override
    public void draw(CrushingRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
        this.arrow.draw(poseStack, 24, 18);
        this.energyBar.draw(poseStack, 121, 1);

        drawChance(recipe, poseStack);
    }

    protected void drawChance(CrushingRecipe recipe, PoseStack poseStack) {
        float secondaryChance = recipe.getSecondaryChance();
        if (secondaryChance > 0) {
            float secondaryChanceInPercent = secondaryChance * 100;

            TextComponent text = new TextComponent(secondaryChanceInPercent + "%");
            Minecraft minecraft = Minecraft.getInstance();

            Font fontRenderer = minecraft.font;
            fontRenderer.draw(poseStack, text, 79, 39, 0xFF808080);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ModRecipeCategoryUid.CRUSHING;
    }

    @Override
    public Class<? extends CrushingRecipe> getRecipeClass() {
        return CrushingRecipe.class;
    }

    @Override
    public Component getTitle() {
        return TextUtils.translate("jei", "crushing");
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
    public void setIngredients(CrushingRecipe crushingRecipe, IIngredients ingredients) {
        List<ItemStack> outputs = Lists.newArrayList(crushingRecipe.getResultItem());

        if (!crushingRecipe.getSecondRecipeOutput().isEmpty()) {
            outputs.add(crushingRecipe.getSecondRecipeOutput());
        }

        ingredients.setInputIngredients(crushingRecipe.getIngredients());
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CrushingRecipe crushingRecipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 0, 18);
        guiItemStacks.init(1, false, 60, 7);
        guiItemStacks.init(2, false, 60, 31);
        guiItemStacks.set(ingredients);
    }
}