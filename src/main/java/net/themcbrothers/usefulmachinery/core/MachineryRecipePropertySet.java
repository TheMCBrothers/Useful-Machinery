package net.themcbrothers.usefulmachinery.core;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.themcbrothers.usefulmachinery.UsefulMachinery;

public class MachineryRecipePropertySet {
    public static final ResourceKey<RecipePropertySet> CRUSHER_INPUT = register("crusher_input");
    public static final ResourceKey<RecipePropertySet> COMPACTOR_INPUT = register("compactor_input");

    private static ResourceKey<RecipePropertySet> register(String name) {
        return ResourceKey.create(RecipePropertySet.TYPE_KEY, UsefulMachinery.id(name));
    }
}
