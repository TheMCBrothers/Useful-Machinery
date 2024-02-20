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
import net.themcbrothers.usefulmachinery.recipe.CrushingRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class CrushingRecipeBuilder implements RecipeBuilder {
    private final ItemStack primaryResult;
    private final Ingredient ingredient;
    private final int processTime;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private ItemStack secondaryResult = ItemStack.EMPTY;
    private float secondaryChance = 0F;
    private Ingredient supportedUpgrades = Ingredient.EMPTY;
    private String group = "";

    private CrushingRecipeBuilder(ItemStack primaryResult, Ingredient ingredient, int processTime) {
        this.primaryResult = primaryResult;
        this.ingredient = ingredient;
        this.processTime = processTime;
    }

    public static CrushingRecipeBuilder crushing(ItemLike item, int count, Ingredient ingredient, int processTime) {
        ItemStack stack = item.asItem().getDefaultInstance();
        stack.setCount(count);

        return new CrushingRecipeBuilder(stack, ingredient, processTime);
    }

    public CrushingRecipeBuilder secondary(ItemLike item, float chance) {
        this.secondaryResult = item.asItem().getDefaultInstance();
        this.secondaryChance = chance;

        return this;
    }

    public CrushingRecipeBuilder supportedUpgrades(Ingredient upgrades) {
        this.supportedUpgrades = upgrades;

        return this;
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
        return this.primaryResult.getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        this.validate(id);

        Advancement.Builder advancement = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancement::addCriterion);

        recipeOutput.accept(id, new CrushingRecipe(this.group, this.ingredient, this.supportedUpgrades,
                        this.primaryResult, this.secondaryResult,
                        this.secondaryChance, this.processTime),
                advancement.build(id.withPrefix("recipes/")));
    }

    private void validate(ResourceLocation id) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe: " + id);
        }
    }
}
