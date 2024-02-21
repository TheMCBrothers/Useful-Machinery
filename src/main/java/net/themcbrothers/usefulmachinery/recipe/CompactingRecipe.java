package net.themcbrothers.usefulmachinery.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.themcbrothers.lib.crafting.CommonRecipe;
import net.themcbrothers.usefulmachinery.block.entity.extension.Compactor;
import net.themcbrothers.usefulmachinery.core.MachineryBlocks;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeSerializers;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeTypes;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;

public record CompactingRecipe(String group, Ingredient ingredient, ItemStack result, int compactTime,
                               CompactorMode mode) implements CommonRecipe<Compactor> {

    @Override
    public boolean matches(Compactor compactor, Level level) {
        boolean isSameMode = this.mode == compactor.getMode();

        return this.ingredient.test(compactor.getItem(0)) && isSameMode;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public RecipeType<?> getType() {
        return MachineryRecipeTypes.COMPACTING.get();
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(MachineryBlocks.COMPACTOR.get());
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, this.ingredient);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MachineryRecipeSerializers.COMPACTING.get();
    }

    public static class Serializer implements RecipeSerializer<CompactingRecipe> {
        private static final Codec<CompactingRecipe> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(CompactingRecipe::group),
                        Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(CompactingRecipe::ingredient),
                        ItemStack.RESULT_CODEC.fieldOf("result").forGetter(CompactingRecipe::result),
                        Codec.INT.fieldOf("compactTime").forGetter(CompactingRecipe::compactTime),
                        CompactorMode.CODEC.fieldOf("mode").forGetter(CompactingRecipe::mode)
                ).apply(instance, CompactingRecipe::new)
        );

        @Override
        public Codec<CompactingRecipe> codec() {
            return CODEC;
        }

        @Override
        public CompactingRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);

            Ingredient ingredient = Ingredient.fromNetwork(buffer);

            ItemStack result = buffer.readItem();

            int processTime = buffer.readVarInt();

            CompactorMode mode = CompactorMode.byOrdinal(buffer.readVarInt());

            return new CompactingRecipe(group, ingredient, result, processTime, mode);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CompactingRecipe recipe) {
            buffer.writeUtf(recipe.group);

            recipe.ingredient.toNetwork(buffer);

            buffer.writeItem(recipe.result);

            buffer.writeVarInt(recipe.compactTime);

            buffer.writeVarInt(recipe.mode.ordinal());
        }
    }
}
