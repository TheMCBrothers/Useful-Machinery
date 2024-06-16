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
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.themcbrothers.lib.crafting.CommonRecipe;
import net.themcbrothers.usefulmachinery.block.entity.extension.Compactor;
import net.themcbrothers.usefulmachinery.core.MachineryBlocks;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeSerializers;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeTypes;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;

public record CompactingRecipe(String group, SizedIngredient sizedIngredient, ItemStack result, int compactTime,
                               CompactorMode mode) implements CommonRecipe<Compactor> {

    @Override
    public boolean matches(Compactor compactor, Level level) {
        boolean isSameMode = this.mode == compactor.getMode();

        return this.sizedIngredient.test(compactor.getItem(0)) && isSameMode;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
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
        return NonNullList.of(Ingredient.EMPTY, this.sizedIngredient.ingredient());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MachineryRecipeSerializers.COMPACTING.get();
    }

    public static class Serializer implements RecipeSerializer<CompactingRecipe> {
        private static final MapCodec<CompactingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(CompactingRecipe::group),
                        SizedIngredient.FLAT_CODEC.fieldOf("ingredient").forGetter(CompactingRecipe::sizedIngredient),
                        ItemStack.SINGLE_ITEM_CODEC.fieldOf("result").forGetter(CompactingRecipe::result),
                        Codec.INT.fieldOf("compactTime").forGetter(CompactingRecipe::compactTime),
                        CompactorMode.CODEC.fieldOf("mode").forGetter(CompactingRecipe::mode)
                ).apply(instance, CompactingRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, CompactingRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<CompactingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CompactingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static CompactingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);

            SizedIngredient ingredient = SizedIngredient.STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            CompactorMode mode = CompactorMode.STREAM_CODEC.decode(buffer);

            int processTime = buffer.readVarInt();

            return new CompactingRecipe(group, ingredient, result, processTime, mode);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, CompactingRecipe recipe) {
            buffer.writeUtf(recipe.group);

            SizedIngredient.STREAM_CODEC.encode(buffer, recipe.sizedIngredient());
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result());
            CompactorMode.STREAM_CODEC.encode(buffer, recipe.mode());

            buffer.writeVarInt(recipe.compactTime());
        }
    }
}
