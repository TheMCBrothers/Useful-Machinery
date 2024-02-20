package net.themcbrothers.usefulmachinery.datagen.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.NBTIngredient;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;
import net.themcbrothers.usefulmachinery.recipe.CompactingRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class CompactingRecipeBuilder implements RecipeBuilder {
    private final ItemStack result;
    private final Ingredient ingredient;
    private final int processTime;
    private final CompactorMode mode;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private String group = "";

    private CompactingRecipeBuilder(ItemStack result, Ingredient ingredient, int processTime, CompactorMode mode) {
        this.result = result;
        this.ingredient = ingredient;
        this.processTime = processTime;
        this.mode = mode;
    }

    public static CompactingRecipeBuilder compacting(ItemLike item, Ingredient ingredient, int processTime, CompactorMode mode) {
        return new CompactingRecipeBuilder(item.asItem().getDefaultInstance(), ingredient, processTime, mode);
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
    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        this.validate(id);

        Advancement.Builder advancement = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancement::addCriterion);

        recipeOutput.accept(id, new CompactingRecipe(this.group, this.ingredient, this.result,this.processTime, this.mode),
                advancement.build(id.withPrefix("recipes/")));
    }

    private void validate(ResourceLocation id) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe: " + id);
        }
    }
}

