package themcbros.usefulmachinery.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import themcbros.usefulmachinery.init.MachineryBlocks;
import themcbros.usefulmachinery.init.MachineryRecipeSerializers;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CrushingRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final String group;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final ItemStack secondary;
    private final float secondaryChance;
    private final int crushTime;

    public CrushingRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, ItemStack secondary, float secondaryChance, int crushTime) {
        this.id = id;
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
        this.secondary = secondary;
        this.secondaryChance = secondaryChance;
        this.crushTime = crushTime;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(MachineryBlocks.CRUSHER);
    }

    public int getCrushTime() {
        return crushTime;
    }

    public float getSecondaryChance() {
        return secondaryChance;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(this.ingredient);
        return ingredients;
    }

    @Override
    public boolean matches(Container inv, Level level) {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(Container inv) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MachineryRecipeSerializers.CRUSHING;
    }

    @Override
    public RecipeType<?> getType() {
        return MachineryRecipeTypes.CRUSHING;
    }

    public ItemStack getSecondRecipeOutput() {
        return ItemStack.EMPTY;
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<CrushingRecipe> {
        @Override
        public CrushingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String s = GsonHelper.getAsString(json, "group", "");
            JsonElement jsonelement = (GsonHelper.isArrayNode(json, "ingredient") ? GsonHelper.getAsJsonArray(json, "ingredient") : GsonHelper.getAsJsonObject(json, "ingredient"));
            Ingredient ingredient = Ingredient.fromJson(jsonelement);

            if (!json.has("result")) {
                throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            }

            ItemStack itemstack;
            ItemStack itemstack2 = ItemStack.EMPTY;
            float chance = 0.0F;

            if (json.get("result").isJsonObject())
                itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            else {
                String s1 = GsonHelper.getAsString(json, "result");
                ResourceLocation location = new ResourceLocation(s1);
                Item item = ForgeRegistries.ITEMS.getValue(location);

                if (item != null) {
                    itemstack = new ItemStack(item);
                } else {
                    throw new IllegalStateException("Item: " + s1 + " does not exist");
                }
            }

            if (json.has("secondary") && json.get("secondary").isJsonObject()) {
                itemstack2 = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "secondary"));
                chance = GsonHelper.getAsFloat(GsonHelper.getAsJsonObject(json, "secondary"), "chance", 0.0F);
            }

            int i = GsonHelper.getAsInt(json, "processingtime", 200);

            return new CrushingRecipe(recipeId, s, ingredient, itemstack, itemstack2, chance, i);
        }

        @Nullable
        @Override
        public CrushingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String s = buffer.readUtf(32767);
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack itemstack = buffer.readItem();
            ItemStack itemstack2 = buffer.readItem();

            float chance = buffer.readFloat();
            int i = buffer.readVarInt();

            return new CrushingRecipe(recipeId, s, ingredient, itemstack, itemstack2, chance, i);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrushingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeItem(recipe.secondary);
            buffer.writeFloat(recipe.secondaryChance);
            buffer.writeVarInt(recipe.crushTime);
        }
    }
}
