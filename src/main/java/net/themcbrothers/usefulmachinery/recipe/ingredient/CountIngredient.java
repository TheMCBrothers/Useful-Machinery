package net.themcbrothers.usefulmachinery.recipe.ingredient;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.themcbrothers.usefulmachinery.core.MachineryIngredientTypes;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class CountIngredient extends Ingredient {
    public static final Codec<CountIngredient> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(o -> ((TagValue) o.values[0]).tag),
                    Codec.INT.optionalFieldOf("count", 1).forGetter(CountIngredient::getCount)
            ).apply(instance, CountIngredient::new));

    private CountIngredient(TagKey<Item> tag, int count) {
        super(Stream.of(new TagValue(tag, count)), MachineryIngredientTypes.COUNT_INGREDIENT);
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    protected boolean areStacksEqual(ItemStack left, ItemStack right) {
        return compareStacksWithCount(left, right);
    }

    @Override
    public boolean synchronizeWithContents() {
        return false;
    }

    public int getCount() {
        return ((TagValue) this.values[0]).count;
    }

    private static boolean compareStacksWithCount(ItemStack left, ItemStack right) {
        return left.is(right.getItem()) && right.getCount() >= left.getCount();
    }

    public static CountIngredient of(int count, TagKey<Item> tag) {
        return new CountIngredient(tag, count);
    }

    public record TagValue(TagKey<Item> tag, int count) implements Ingredient.Value {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof TagValue value && value.tag.location().equals(this.tag.location());
        }

        @Override
        public Collection<ItemStack> getItems() {
            List<ItemStack> list = Lists.newArrayList();

            for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(this.tag)) {
                list.add(new ItemStack(holder, this.count));
            }

            if (list.isEmpty()) {
                list.add(new ItemStack(Blocks.BARRIER).setHoverName(Component.literal("Empty Tag: " + this.tag.location())));
            }

            return list;
        }
    }
}
