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
import net.themcbrothers.lib.crafting.CommonRecipe;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeBookCategories;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeSerializers;
import net.themcbrothers.usefulmachinery.core.MachineryRecipeTypes;

import java.util.Optional;

public record CrushingRecipe(
        String group,
        Ingredient ingredient,
        Optional<Ingredient> supportedUpgrades,
        ItemStackTemplate primaryResult,
        Optional<ItemStackTemplate> secondaryResult,
        float secondaryChance,
        int crushTime
) implements CommonRecipe<RecipeInput> {
    public static final MapCodec<CrushingRecipe> MAP_CODEC = crushingMapCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, CrushingRecipe> STREAM_CODEC = crushingStreamCodec();
    public static final RecipeSerializer<CrushingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return this.ingredient.test(recipeInput.getItem(0));
    }

    @Override
    public RecipeType<CrushingRecipe> getType() {
        return MachineryRecipeTypes.CRUSHING.get();
    }

    @Override
    public ItemStack assemble(RecipeInput input) {
        return this.primaryResult.create();
    }

    public ItemStack assembleSecondary() {
        return this.secondaryResult.map(ItemStackTemplate::create).orElse(ItemStack.EMPTY);
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(this.ingredient);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return MachineryRecipeBookCategories.CRUSHING.get();
    }

    @Override
    public RecipeSerializer<CrushingRecipe> getSerializer() {
        return MachineryRecipeSerializers.CRUSHING.get();
    }

    public boolean supportUpgrade(ItemStack upgrade) {
        return this.supportedUpgrades.map(upgrades -> upgrades.test(upgrade)).orElse(false);
    }

    private static MapCodec<CrushingRecipe> crushingMapCodec() {
        return RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                                Codec.STRING.optionalFieldOf("group", "").forGetter(CrushingRecipe::group),
                                Ingredient.CODEC.fieldOf("ingredient").forGetter(CrushingRecipe::ingredient),
                                Ingredient.CODEC.optionalFieldOf("supportedUpgrades").forGetter(CrushingRecipe::supportedUpgrades),
                                ItemStackTemplate.CODEC.fieldOf("primary").forGetter(CrushingRecipe::primaryResult),
                                ItemStackTemplate.CODEC.optionalFieldOf("secondary").forGetter(CrushingRecipe::secondaryResult),
                                Codec.FLOAT.optionalFieldOf("secondaryChance", 0F).forGetter(CrushingRecipe::secondaryChance),
                                Codec.INT.fieldOf("crushTime").forGetter(CrushingRecipe::crushTime))
                        .apply(instance, CrushingRecipe::new)
        );
    }

    private static StreamCodec<RegistryFriendlyByteBuf, CrushingRecipe> crushingStreamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, CrushingRecipe::group,
                Ingredient.CONTENTS_STREAM_CODEC, CrushingRecipe::ingredient,
                Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, CrushingRecipe::supportedUpgrades,
                ItemStackTemplate.STREAM_CODEC, CrushingRecipe::primaryResult,
                ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC), CrushingRecipe::secondaryResult,
                ByteBufCodecs.FLOAT, CrushingRecipe::secondaryChance,
                ByteBufCodecs.INT, CrushingRecipe::crushTime,
                CrushingRecipe::new
        );
    }
}
