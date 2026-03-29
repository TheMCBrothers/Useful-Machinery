package net.themcbrothers.usefulmachinery.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.themcbrothers.lib.crafting.CommonRecipe;
import net.themcbrothers.usefulmachinery.block.entity.extension.Compactor;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeSerializers;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeTypes;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;

public record CompactingRecipe(
        String group,
        SizedIngredient sizedIngredient,
        ItemStackTemplate result,
        int compactTime,
        CompactorMode mode
) implements CommonRecipe<Compactor> {
    public static final MapCodec<CompactingRecipe> MAP_CODEC = compactingMapCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, CompactingRecipe> STREAM_CODEC = compactingStreamCodec();
    public static final RecipeSerializer<CompactingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    @Override
    public boolean matches(Compactor compactor, Level level) {
        boolean isSameMode = this.mode == compactor.getMode();

        return this.sizedIngredient.test(compactor.getItem(0)) && isSameMode;
    }

    @Override
    public RecipeType<CompactingRecipe> getType() {
        return MachineryRecipeTypes.COMPACTING.get();
    }

    @Override
    public ItemStack assemble(Compactor input) {
        return this.result.create();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(this.sizedIngredient.ingredient());
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC; // TODO check how to support custom recipe
    }

    @Override
    public RecipeSerializer<CompactingRecipe> getSerializer() {
        return MachineryRecipeSerializers.COMPACTING.get();
    }

    private static MapCodec<CompactingRecipe> compactingMapCodec() {
        return RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(CompactingRecipe::group),
                        SizedIngredient.NESTED_CODEC.fieldOf("ingredient").forGetter(CompactingRecipe::sizedIngredient),
                        ItemStackTemplate.CODEC.fieldOf("result").forGetter(CompactingRecipe::result),
                        Codec.INT.fieldOf("compactTime").forGetter(CompactingRecipe::compactTime),
                        CompactorMode.CODEC.fieldOf("mode").forGetter(CompactingRecipe::mode)
                ).apply(instance, CompactingRecipe::new)
        );
    }

    private static StreamCodec<RegistryFriendlyByteBuf, CompactingRecipe> compactingStreamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, CompactingRecipe::group,
                SizedIngredient.STREAM_CODEC, CompactingRecipe::sizedIngredient,
                ItemStackTemplate.STREAM_CODEC, CompactingRecipe::result,
                ByteBufCodecs.INT, CompactingRecipe::compactTime,
                CompactorMode.STREAM_CODEC, CompactingRecipe::mode,
                CompactingRecipe::new
        );
    }
}