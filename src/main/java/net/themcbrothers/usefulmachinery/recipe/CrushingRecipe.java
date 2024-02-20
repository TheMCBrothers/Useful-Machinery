package net.themcbrothers.usefulmachinery.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.themcbrothers.lib.crafting.CommonRecipe;
import net.themcbrothers.usefulmachinery.core.MachineryBlocks;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeSerializers;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeTypes;

public record CrushingRecipe(String group, Ingredient ingredient, Ingredient supportedUpgrades, ItemStack primaryResult,
                             ItemStack secondaryResult, float secondaryChance,
                             int crushTime) implements CommonRecipe<Container> {

    @Override
    public boolean matches(Container container, Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
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
        return NonNullList.of(this.ingredient);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MachineryRecipeSerializers.CRUSHING.get();
    }

    public boolean supportUpgrade(ItemStack upgrade) {
        return this.supportedUpgrades.test(upgrade);
    }

    public static class Serializer implements RecipeSerializer<CrushingRecipe> {
        private static final Codec<CrushingRecipe> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(CrushingRecipe::group),
                        Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(CrushingRecipe::ingredient),
                        ExtraCodecs.strictOptionalField(Ingredient.CODEC, "supportedUpgrades", Ingredient.EMPTY).forGetter(CrushingRecipe::supportedUpgrades),
                        ItemStack.RESULT_CODEC.fieldOf("primary").forGetter(CrushingRecipe::primaryResult),
                        ExtraCodecs.strictOptionalField(ItemStack.RESULT_CODEC.codec(), "secondary", ItemStack.EMPTY).forGetter(CrushingRecipe::secondaryResult),
                        ExtraCodecs.strictOptionalField(Codec.FLOAT, "secondaryChance", 0F).forGetter(CrushingRecipe::secondaryChance),
                        Codec.INT.fieldOf("crushTime").forGetter(CrushingRecipe::crushTime)
                ).apply(instance, CrushingRecipe::new));

        @Override
        public Codec<CrushingRecipe> codec() {
            return CODEC;
        }

        @Override
        public CrushingRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);

            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Ingredient supportedUpgrades = Ingredient.fromNetwork(buffer);

            ItemStack primaryResult = buffer.readItem();
            ItemStack secondaryResult = buffer.readItem();

            float secondaryChance = buffer.readFloat();
            int crushTime = buffer.readVarInt();

            return new CrushingRecipe(group, ingredient, supportedUpgrades, primaryResult, secondaryResult, secondaryChance, crushTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrushingRecipe recipe) {
            buffer.writeUtf(recipe.group);

            recipe.ingredient.toNetwork(buffer);
            recipe.supportedUpgrades.toNetwork(buffer);

            buffer.writeItem(recipe.primaryResult);
            buffer.writeItem(recipe.secondaryResult);

            buffer.writeFloat(recipe.secondaryChance);
            buffer.writeVarInt(recipe.crushTime);
        }
    }
}
