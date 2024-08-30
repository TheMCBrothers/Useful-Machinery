package net.themcbrothers.usefulmachinery.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.themcbrothers.lib.crafting.CommonRecipe;
import net.themcbrothers.usefulmachinery.core.MachineryBlocks;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeSerializers;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeTypes;

public record CrushingRecipe(String group, Ingredient ingredient, Ingredient supportedUpgrades,
                             ItemStack primaryResult,
                             ItemStack secondaryResult, float secondaryChance,
                             int crushTime) implements CommonRecipe<RecipeInput> {

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return this.ingredient.test(recipeInput.getItem(0));
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.primaryResult;
    }

    @Override
    public RecipeType<?> getType() {
        return MachineryRecipeTypes.CRUSHING.get();
    }

    @Override
    public ItemStack getToastSymbol() {
        return MachineryBlocks.CRUSHER.toStack();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, this.ingredient);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MachineryRecipeSerializers.CRUSHING.get();
    }

    public boolean supportUpgrade(ItemStack upgrade) {
        return this.supportedUpgrades.test(upgrade);
    }

    public static class Serializer implements RecipeSerializer<CrushingRecipe> {
        private static final MapCodec<CrushingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(CrushingRecipe::group),
                        Ingredient.CODEC.fieldOf("ingredient").forGetter(CrushingRecipe::ingredient),
                        Ingredient.CODEC.optionalFieldOf("supportedUpgrades", Ingredient.EMPTY).forGetter(CrushingRecipe::supportedUpgrades),
                        ItemStack.STRICT_CODEC.fieldOf("primary").forGetter(CrushingRecipe::primaryResult),
                        ItemStack.STRICT_CODEC.optionalFieldOf("secondary", ItemStack.EMPTY).forGetter(CrushingRecipe::secondaryResult),
                        Codec.FLOAT.optionalFieldOf("secondaryChance", 0F).forGetter(CrushingRecipe::secondaryChance),
                        Codec.INT.fieldOf("crushTime").forGetter(CrushingRecipe::crushTime)
                ).apply(instance, CrushingRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, CrushingRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<CrushingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CrushingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static CrushingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);

            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient supportedUpgrades = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack primaryResult = ItemStack.STREAM_CODEC.decode(buffer);
            ItemStack secondaryResult = ItemStack.EMPTY;

            if (buffer.readBoolean()) {
                secondaryResult = ItemStack.STREAM_CODEC.decode(buffer);
            }

            float secondaryChance = buffer.readFloat();
            int crushTime = buffer.readVarInt();

            return new CrushingRecipe(group, ingredient, supportedUpgrades, primaryResult, secondaryResult, secondaryChance, crushTime);
        }


        public static void toNetwork(RegistryFriendlyByteBuf buffer, CrushingRecipe recipe) {
            buffer.writeUtf(recipe.group);

            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.supportedUpgrades());

            ItemStack.STREAM_CODEC.encode(buffer, recipe.primaryResult());

            if (!recipe.secondaryResult().isEmpty()) {
                buffer.writeBoolean(true);
                ItemStack.STREAM_CODEC.encode(buffer, recipe.secondaryResult());
            } else {
                buffer.writeBoolean(false);
            }

            buffer.writeFloat(recipe.secondaryChance);
            buffer.writeVarInt(recipe.crushTime);
        }
    }
}
