package net.themcbrothers.examplemod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.themcbrothers.examplemod.ExampleMod;
import net.themcbrothers.examplemod.core.ExampleBlocks;

public class ExampleBlockStateProvider extends BlockStateProvider {
    public ExampleBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ExampleMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ExampleBlocks.EXAMPLE_BLOCK.get(), models().cubeAll("example_block", mcLoc("block/cauldron_inner")));

        // Item Models
        simpleBlockItem(ExampleBlocks.EXAMPLE_BLOCK.get());
    }

    private void simpleBlockItem(Block block) {
        simpleBlockItem(block, models().getExistingFile(blockTexture(block)));
    }
}
