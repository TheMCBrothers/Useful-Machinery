package themcbros.usefulmachinery.data.recipes;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import themcbros.usefulmachinery.init.MachineryRecipeSerializers;
import themcbros.usefulmachinery.machine.CompactorMode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public class CompactingRecipeBuilder implements RecipeBuilder {
    private final Item item;
    private final Ingredient ingredient;
    private final int count;
    private final int processTime;
    private final CompactorMode mode;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    @Nullable
    private String group;

    private CompactingRecipeBuilder(ItemLike item, Ingredient ingredient, int count, int processTime, CompactorMode mode) {
        this.item = item.asItem();
        this.ingredient = ingredient;
        this.count = count;
        this.processTime = processTime;
        this.mode = mode;
    }

    public static CompactingRecipeBuilder compacting(ItemLike item, Ingredient ingredient, int count, int processTime, CompactorMode compactorMode) {
        return new CompactingRecipeBuilder(item, ingredient, count, processTime, compactorMode);
    }

    @Nonnull
    @Override
    public CompactingRecipeBuilder unlockedBy(@Nonnull String key, @Nonnull CriterionTriggerInstance criterion) {
        this.advancement.addCriterion(key, criterion);
        return this;
    }

    @Nonnull
    @Override
    public CompactingRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Nonnull
    @Override
    public Item getResult() {
        return this.item;
    }

    @Override
    public void save(Consumer<FinishedRecipe> recipe, @Nonnull ResourceLocation location) {
        this.validate(location);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location)).rewards(AdvancementRewards.Builder.recipe(location)).requirements(RequirementsStrategy.OR);

        recipe.accept(new Result(location, group == null ? "" : group, ingredient, count, item, processTime, mode, advancement, new ResourceLocation(location.getNamespace(), "recipes/" + location.getPath())));
    }

    private void validate(ResourceLocation location) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe: " + location);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient ingredient;
        private final int count;
        private final Item result;
        private final int processTime;
        private final CompactorMode mode;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, String group, Ingredient ingredient, int count, Item result, int processTime, CompactorMode mode, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.group = group;
            this.ingredient = ingredient;
            this.count = count;
            this.result = result;
            this.processTime = processTime;
            this.mode = mode;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(@Nonnull JsonObject object) {
            if (!this.group.isEmpty()) {
                object.addProperty("group", this.group);
            }

            JsonObject ingredientObject = (JsonObject) this.ingredient.toJson();
            ingredientObject.addProperty("count", this.count);

            object.add("ingredient", ingredientObject);
            object.addProperty("mode", this.mode.getSerializedName());
            object.addProperty("processingtime", this.processTime);
            object.addProperty("result", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.result)).toString());
        }

        @Nonnull
        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Nonnull
        @Override
        public RecipeSerializer<?> getType() {
            return MachineryRecipeSerializers.COMPACTING.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
