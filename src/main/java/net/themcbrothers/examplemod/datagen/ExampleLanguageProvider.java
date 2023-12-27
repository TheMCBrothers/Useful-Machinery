package net.themcbrothers.examplemod.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.themcbrothers.examplemod.ExampleMod;
import net.themcbrothers.examplemod.core.ExampleBlocks;
import net.themcbrothers.examplemod.core.ExampleItems;

public class ExampleLanguageProvider extends LanguageProvider {
    public ExampleLanguageProvider(PackOutput output) {
        super(output, ExampleMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // Blocks
        addBlock(ExampleBlocks.EXAMPLE_BLOCK, "Example Block");

        // Items
        addItem(ExampleItems.EXAMPLE_ITEM, "Example Item");

        // Tab
        add("itemGroup.examplemod.example", "Example Mod");
    }
}
