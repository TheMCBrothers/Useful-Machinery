package themcbros.usefulmachinery.compat.jei.crusher;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.compat.jei.ModRecipeCategoryUid;
import themcbros.usefulmachinery.init.ModItems;
import themcbros.usefulmachinery.recipes.CrusherRecipe;
import themcbros.usefulmachinery.util.TextUtils;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CrushingCategory /* implements IRecipeCategory<CrusherRecipe> {

    private static final ResourceLocation TEXTURES = UsefulMachinery.getId("textures/gui/container/crusher.png");

    private final IDrawable icon, background;
    private final IDrawableAnimated arrow, energyBar;

    public CrushingCategory(IGuiHelper helper) {

        this.icon = helper.createDrawableIngredient(new ItemStack(ModItems.CRUSHER));
        this.background = helper.createDrawable(TEXTURES, 55, 16, 111, 54);

        this.arrow = helper.drawableBuilder(TEXTURES, 176, 14, 24, 17).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        this.energyBar = helper.drawableBuilder(TEXTURES, 246, 0, 10, 50).buildAnimated(200, IDrawableAnimated.StartDirection.TOP, true);

    }

    @Override
    public void draw(CrusherRecipe recipe, double mouseX, double mouseY) {
        this.arrow.draw(24, 18);
        this.energyBar.draw(100, 1);
    }

    @Override
    public ResourceLocation getUid() {
        return ModRecipeCategoryUid.CRUSHING;
    }

    @Override
    public Class<? extends CrusherRecipe> getRecipeClass() {
        return CrusherRecipe.class;
    }

    @Override
    public String getTitle() {
        return TextUtils.translate("jei", "crushing").getFormattedText();
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
    public void setIngredients(CrusherRecipe crusherRecipe, IIngredients ingredients) {
        ingredients.setInputIngredients(crusherRecipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, crusherRecipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CrusherRecipe crusherRecipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 0, 0);
        guiItemStacks.init(2, false, 60, 18);
        guiItemStacks.set(ingredients);
    }
}
*/{}