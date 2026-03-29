package net.themcbrothers.usefulmachinery.datagen.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.themcbrothers.usefulmachinery.recipe.CrushingRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class CrushingRecipeBuilder implements RecipeBuilder {
    private final ItemStackTemplate primaryResult;
    private final Ingredient ingredient;
    private final int processTime;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private @Nullable ItemStackTemplate secondaryResult = null;
    private float secondaryChance = 0F;
    private @Nullable Ingredient supportedUpgrades;
    private String group = "";

    private CrushingRecipeBuilder(ItemStackTemplate primaryResult, Ingredient ingredient, int processTime) {
        this.primaryResult = primaryResult;
        this.ingredient = ingredient;
        this.processTime = processTime;
    }

    public static CrushingRecipeBuilder crushing(ItemLike item, int count, Ingredient ingredient, int processTime) {
        ItemStackTemplate itemStackTemplate = new ItemStackTemplate(item.asItem(), count);

        return new CrushingRecipeBuilder(itemStackTemplate, ingredient, processTime);
    }

    public CrushingRecipeBuilder secondary(ItemLike item, float chance) {
        this.secondaryResult = new ItemStackTemplate(item.asItem());
        this.secondaryChance = chance;

        return this;
    }

    public CrushingRecipeBuilder supportedUpgrades(@Nullable Ingredient upgrades) {
        this.supportedUpgrades = upgrades;

        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(this.primaryResult);
    }

    @Override
    public RecipeBuilder unlockedBy(String key, Criterion<?> criterion) {
        this.criteria.put(key, criterion);

        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String name) {
        this.group = name != null ? name : "";

        return this;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> resourceKey) {
        this.validate(resourceKey);

        Advancement.Builder advancement = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancement::addCriterion);

        recipeOutput.accept(resourceKey,
                new CrushingRecipe(this.group,
                        this.ingredient,
                        Optional.ofNullable(this.supportedUpgrades),
                        this.primaryResult,
                        Optional.ofNullable(this.secondaryResult),
                        this.secondaryChance,
                        this.processTime
                ),
                advancement.build(resourceKey.identifier().withPrefix("recipes/")));
    }

    private void validate(ResourceKey<Recipe<?>> resourceKey) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe: " + resourceKey);
        }
    }
}
