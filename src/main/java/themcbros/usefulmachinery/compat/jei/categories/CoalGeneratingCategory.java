package themcbros.usefulmachinery.compat.jei.categories;

import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.compat.jei.MachineryJeiRecipeTypes;
import themcbros.usefulmachinery.init.MachineryBlocks;
import themcbros.usefulmachinery.util.TextUtils;

public class CoalGeneratingCategory implements IRecipeCategory<IJeiFuelingRecipe> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.getId("textures/gui/container/coal_generator.png");
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
    public void draw(IJeiFuelingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        this.energyBar.draw(stack, 121, 1);
        this.fire.draw(stack, 20, 18);
    }

    @Override
    public ResourceLocation getUid() {
        return MachineryJeiRecipeTypes.COAL_GENERATING.getUid();
    }

    @Override
    public Class<? extends IJeiFuelingRecipe> getRecipeClass() {
        return MachineryJeiRecipeTypes.COAL_GENERATING.getRecipeClass();
    }

    @Override
    public RecipeType<IJeiFuelingRecipe> getRecipeType() {
        return MachineryJeiRecipeTypes.COAL_GENERATING;
    }

    @Override
    public Component getTitle() {
        return TextUtils.translate("jei", "fuel");
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