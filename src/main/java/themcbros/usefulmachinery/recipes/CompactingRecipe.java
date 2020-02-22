package themcbros.usefulmachinery.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import themcbros.usefulmachinery.init.ModItems;
import themcbros.usefulmachinery.init.ModRecipeSerializers;
import themcbros.usefulmachinery.machine.CompactorMode;

import javax.annotation.Nullable;

public class CompactingRecipe implements IRecipe<IInventory> {

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
    public boolean matches(IInventory inv, World worldIn) {
        return this.ingredient.test(inv.getStackInSlot(0)) && inv.getStackInSlot(0).getCount() >= this.count;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(ModItems.COMPACTOR);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.COMPACTING;
    }

    @Override
    public IRecipeType<?> getType() {
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

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CompactingRecipe> {

        @Override
        public CompactingRecipe read(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getString(json, "group", "");
            JsonElement jsonelement = JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient");
            Ingredient ingredient = Ingredient.deserialize(jsonelement);
            int count = JSONUtils.isJsonArray(json, "ingredient") ? 1 : JSONUtils.getInt(jsonelement.getAsJsonObject(), "count", 1);
            if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack itemstack;
            if (json.get("result").isJsonObject()) itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            else {
                String s1 = JSONUtils.getString(json, "result");
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                Item item = ForgeRegistries.ITEMS.getValue(resourcelocation);
                if (item != null)
                    itemstack = new ItemStack(item);
                else
                    throw new IllegalStateException("Item: " + s1 + " does not exist");
            }
            int i = JSONUtils.getInt(json, "processingtime", 200);
            if (!json.has("mode")) throw new com.google.gson.JsonSyntaxException("Missing mode, expected to find a string");
            CompactorMode mode = CompactorMode.byName(JSONUtils.getString(json, "mode"));
            return new CompactingRecipe(recipeId, s, ingredient, count, itemstack, i, mode != null ? mode : CompactorMode.PLATE);
        }

        @Nullable
        @Override
        public CompactingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readString(32767);
            Ingredient ingredient = Ingredient.read(buffer);
            int count = buffer.readVarInt();
            ItemStack itemstack = buffer.readItemStack();
            int i = buffer.readVarInt();
            CompactorMode mode = CompactorMode.byIndex(buffer.readVarInt());
            return new CompactingRecipe(recipeId, s, ingredient, count, itemstack, i, mode);
        }

        @Override
        public void write(PacketBuffer buffer, CompactingRecipe recipe) {
            buffer.writeString(recipe.group);
            recipe.ingredient.write(buffer);
            buffer.writeVarInt(recipe.count);
            buffer.writeItemStack(recipe.result);
            buffer.writeVarInt(recipe.processTime);
            buffer.writeVarInt(recipe.compactorMode.getIndex());
        }
    }

}
