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
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.compat.jei.MachineryJeiRecipeTypes;
import net.themcbrothers.usefulmachinery.core.MachineryBlocks;
import net.themcbrothers.usefulmachinery.recipe.CompactingRecipe;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public class CompactingCategory implements IRecipeCategory<RecipeHolder<CompactingRecipe>> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.rl("textures/gui/container/compactor.png");
    private final IDrawable icon;
    private final IDrawable background;
    private final IDrawableAnimated arrow;
    private final IDrawableAnimated energyBar;
    private final float modeScale;
    private final int modePosX;
    private final int modePosY;

    public CompactingCategory(IGuiHelper helper) {
        this.modeScale = 0.9F;
        this.modePosX = 28;
        this.modePosY = 34;

        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MachineryBlocks.COMPACTOR.get()));
        this.background = helper.createDrawable(TEXTURES, 34, 16, 132, 52);

        this.arrow = helper.drawableBuilder(TEXTURES, 176, 14, 24, 17).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        this.energyBar = helper.drawableBuilder(TEXTURES, 246, 0, 10, 50).buildAnimated(200, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override
    public void draw(RecipeHolder<CompactingRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.arrow.draw(guiGraphics, 24, 16);
        this.energyBar.draw(guiGraphics, 121, 1);

        Font font = Minecraft.getInstance().font;
        ItemStack renderStack = recipe.value().mode().getItemProvider().asItem().getDefaultInstance();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(this.modeScale, this.modeScale, this.modeScale);
        guiGraphics.renderItemDecorations(font, renderStack, Math.round(this.modePosX / this.modeScale), Math.round(this.modePosY / this.modeScale));
        guiGraphics.renderItem(renderStack, Math.round(this.modePosX / this.modeScale), Math.round(this.modePosY / this.modeScale));
        guiGraphics.pose().popPose();
    }

    @Override
    public List<Component> getTooltipStrings(RecipeHolder<CompactingRecipe> recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= this.modePosX && mouseX < this.modePosX + Math.round(16 * this.modeScale)
                && mouseY >= this.modePosY && mouseY < this.modePosY + Math.round(16 * this.modeScale)) {
            return List.of(TEXT_UTILS.translate("misc", "compact_" + recipe.value().mode().getSerializedName()));
        }

        return List.of();
    }

    @Override
    public RecipeType<RecipeHolder<CompactingRecipe>> getRecipeType() {
        return MachineryJeiRecipeTypes.COMPACTING;
    }

    @Override
    public Component getTitle() {
        return TEXT_UTILS.translate("jei", "compacting");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CompactingRecipe> recipe, IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 17).addItemStacks(Arrays.asList(recipe.value().getIngredients().get(0).getItems()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 17).addItemStack(recipe.value().getResultItem(Objects.requireNonNull(Minecraft.getInstance().level).registryAccess()));
    }
}
