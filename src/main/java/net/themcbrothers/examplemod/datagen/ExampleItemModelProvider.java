package net.themcbrothers.examplemod.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.themcbrothers.examplemod.ExampleMod;
import net.themcbrothers.examplemod.core.ExampleItems;

public class ExampleItemModelProvider extends ItemModelProvider {
    public ExampleItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ExampleMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ExampleItems.EXAMPLE_ITEM.value());
    }
}
