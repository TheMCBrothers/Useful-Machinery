package themcbros.usefulmachinery.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import themcbros.usefulmachinery.init.ModItems;
import themcbros.usefulmachinery.init.ModRecipeSerializers;
import themcbros.usefulmachinery.machine.CompactorMode;

import javax.annotation.Nullable;

public class CompactingRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final String group;
    private final Ingredient ingredient;
    private final int count;
    private final ItemStack result;
    private final int processTime;
    private final CompactorMode compactorMode;

    public CompactingRecipe(ResourceLocation id, String group, Ingredient ingredient, int count, ItemStack result, int processTime, CompactorMode compactorMode) {
        this.id = id;
        this.group = group;
        this.ingredient = ingredient;
        this.count = count;
        this.result = result;
        this.processTime = processTime;
        this.compactorMode = compactorMode;
    }

    @Override
    public boolean matches(Container inv, Level level) {
        return this.ingredient.test(inv.getItem(0)) && inv.getItem(0).getCount() >= this.count;
    }

    @Override
    public ItemStack assemble(Container inv) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModItems.COMPACTOR);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.COMPACTING;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.COMPACTING;
    }

    public int getProcessTime() {
        return processTime;
    }

    public CompactorMode getCompactorMode() {
        return compactorMode;
    }

    public int getCount() {
        return count;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.withSize(1, this.ingredient);
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<CompactingRecipe> {
        @Override
        public CompactingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String s = GsonHelper.getAsString(json, "group", "");
            JsonElement jsonelement = GsonHelper.isArrayNode(json, "ingredient") ? GsonHelper.getAsJsonArray(json, "ingredient") : GsonHelper.getAsJsonObject(json, "ingredient");
            Ingredient ingredient = Ingredient.fromJson(jsonelement);

            int count = GsonHelper.isArrayNode(json, "ingredient") ? 1 : GsonHelper.getAsInt(jsonelement.getAsJsonObject(), "count", 1);

            if (!json.has("result"))
                throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");

            ItemStack itemstack;

            if (json.get("result").isJsonObject())
                itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            else {
                String s1 = GsonHelper.getAsString(json, "result");
                ResourceLocation resourcelocation = new ResourceLocation(s1);

                Item item = ForgeRegistries.ITEMS.getValue(resourcelocation);

                if (item != null)
                    itemstack = new ItemStack(item);
                else
                    throw new IllegalStateException("Item: " + s1 + " does not exist");
            }

            int i = GsonHelper.getAsInt(json, "processingtime", 200);

            if (!json.has("mode"))
                throw new com.google.gson.JsonSyntaxException("Missing mode, expected to find a string");
            CompactorMode mode = CompactorMode.byName(GsonHelper.getAsString(json, "mode"));

            return new CompactingRecipe(recipeId, s, ingredient, count, itemstack, i, mode != null ? mode : CompactorMode.PLATE);
        }

        @Nullable
        @Override
        public CompactingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String s = buffer.readUtf(32767);
            Ingredient ingredient = Ingredient.fromNetwork(buffer);

            int count = buffer.readVarInt();
            ItemStack itemstack = buffer.readItem();

            int i = buffer.readVarInt();
            CompactorMode mode = CompactorMode.byIndex(buffer.readVarInt());

            return new CompactingRecipe(recipeId, s, ingredient, count, itemstack, i, mode);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CompactingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            recipe.ingredient.toNetwork(buffer);
            buffer.writeVarInt(recipe.count);
            buffer.writeItem(recipe.result);
            buffer.writeVarInt(recipe.processTime);
            buffer.writeVarInt(recipe.compactorMode.getIndex());
        }
    }
}
