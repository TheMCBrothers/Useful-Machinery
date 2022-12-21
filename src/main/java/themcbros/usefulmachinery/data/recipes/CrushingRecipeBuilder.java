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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import themcbros.usefulmachinery.init.MachineryRecipeSerializers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public class CrushingRecipeBuilder implements RecipeBuilder {
    private final Item item;
    private final int count;
    private final Ingredient ingredient;
    private Item secondary = Items.AIR;
    private final int processTime;
    private float secondaryChance = 0.0F;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    @Nullable
    private String group;

    private CrushingRecipeBuilder(ItemLike item, int count, Ingredient ingredient, int processTime) {
        this.item = item.asItem();
        this.count = count;
        this.ingredient = ingredient;
        this.processTime = processTime;
    }

    public static CrushingRecipeBuilder crushing(ItemLike item, int count, Ingredient ingredient, int processTime) {
        return new CrushingRecipeBuilder(item, count, ingredient, processTime);
    }

    public CrushingRecipeBuilder secondary(ItemLike item, float chance) {
        this.secondary = item.asItem();
        this.secondaryChance = chance;

        return this;
    }

    @Nonnull
    @Override
    public CrushingRecipeBuilder unlockedBy(@Nonnull String key, @Nonnull CriterionTriggerInstance criterion) {
        this.advancement.addCriterion(key, criterion);
        return this;
    }

    @Nonnull
    @Override
    public CrushingRecipeBuilder group(@Nullable String group) {
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

        recipe.accept(new Result(location, group == null ? "" : group, ingredient, item, count, secondary, processTime, secondaryChance, advancement, new ResourceLocation(location.getNamespace(), "recipes/" + location.getPath())));
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
        private final Item result;
        private final int count;
        private final Item secondary;
        private final int processTime;
        private final float secondaryChance;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, String group, Ingredient ingredient, Item result, int count, Item secondary, int processTime, float secondaryChance, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.group = group;
            this.ingredient = ingredient;
            this.result = result;
            this.count = count;
            this.secondary = secondary;
            this.processTime = processTime;
            this.secondaryChance = secondaryChance;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(@Nonnull JsonObject object) {
            if (!this.group.isEmpty()) {
                object.addProperty("group", this.group);
            }
            object.add("ingredient", this.ingredient.toJson());
            object.addProperty("processingtime", this.processTime);
            object.addProperty("result", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.result)).toString());

            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.result)).toString());
            resultObject.addProperty("count", this.count);

            object.add("result", resultObject);

            if (!secondary.equals(Items.AIR) && secondaryChance > 0) {
                JsonObject secondaryObject = new JsonObject();
                secondaryObject.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.secondary)).toString());
                secondaryObject.addProperty("chance", this.secondaryChance);

                object.add("secondary", secondaryObject);
            }
        }

        @Nonnull
        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Nonnull
        @Override
        public RecipeSerializer<?> getType() {
            return MachineryRecipeSerializers.CRUSHING.get();
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
