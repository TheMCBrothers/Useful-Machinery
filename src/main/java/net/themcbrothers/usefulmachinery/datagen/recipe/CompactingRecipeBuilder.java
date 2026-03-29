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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;
import net.themcbrothers.usefulmachinery.recipe.CompactingRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class CompactingRecipeBuilder implements RecipeBuilder {
    private final ItemStackTemplate result;
    private final SizedIngredient sizedIngredient;
    private final int processTime;
    private final CompactorMode mode;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private String group = "";

    private CompactingRecipeBuilder(ItemStackTemplate result, SizedIngredient sizedIngredient, int processTime, CompactorMode mode) {
        this.result = result;
        this.sizedIngredient = sizedIngredient;
        this.processTime = processTime;
        this.mode = mode;
    }

    public static CompactingRecipeBuilder compacting(ItemLike item, SizedIngredient sizedIngredient, int processTime, CompactorMode mode) {
        ItemStackTemplate itemStackTemplate = new ItemStackTemplate(item.asItem());

        return new CompactingRecipeBuilder(itemStackTemplate, sizedIngredient, processTime, mode);
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(this.result);
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

        recipeOutput.accept(resourceKey, new CompactingRecipe(this.group, this.sizedIngredient, this.result, this.processTime, this.mode),
                advancement.build(resourceKey.identifier().withPrefix("recipes/")));
    }

    private void validate(ResourceKey<Recipe<?>> resourceKey) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe: " + resourceKey);
        }
    }
}

