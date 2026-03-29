package net.themcbrothers.usefulmachinery.datagen.models;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import net.themcbrothers.lib.data.models.ModelSubProvider;

import static net.minecraft.client.data.models.BlockModelGenerators.createSimpleBlock;
import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;
import static net.themcbrothers.usefulmachinery.UsefulMachinery.id;
import static net.themcbrothers.usefulmachinery.core.MachineryBlocks.*;

public class MachineryBlockModelProvider extends ModelSubProvider {
    private static final ModelTemplate MACHINE_BLOCK_MODEL_TEMPLATE = ExtendedModelTemplateBuilder.builder().parent(id("block/machine_block")).requiredTextureSlot(TextureSlot.FRONT).build();
    private static final TexturedModel.Provider MACHINE_BLOCK_MODEL_PROVIDER = TexturedModel.createDefault(MachineryBlockModelProvider::createDefault, MACHINE_BLOCK_MODEL_TEMPLATE);

    public MachineryBlockModelProvider(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        super(blockModels, itemModels);
    }

    private static TextureMapping createDefault(Block block) {
        return new TextureMapping().put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front"));
    }

    @Override
    protected void register() {
        // Machine blocks
        blockModels.createFurnace(COAL_GENERATOR.get(), MACHINE_BLOCK_MODEL_PROVIDER);
        blockModels.createFurnace(COMPACTOR.get(), MACHINE_BLOCK_MODEL_PROVIDER);
        blockModels.createFurnace(CRUSHER.get(), MACHINE_BLOCK_MODEL_PROVIDER);
        blockModels.createFurnace(ELECTRIC_SMELTER.get(), MACHINE_BLOCK_MODEL_PROVIDER);
        blockModels.createFurnace(LAVA_GENERATOR.get(), MACHINE_BLOCK_MODEL_PROVIDER);

        // Common blocks
        blockModels.blockStateOutput.accept(createSimpleBlock(CREATIVE_POWER_CELL.get(), plainVariant(ModelLocationUtils.getModelLocation(Blocks.REDSTONE_BLOCK))));
        blockModels.registerSimpleItemModel(CREATIVE_POWER_CELL.get(), ModelLocationUtils.getModelLocation(Blocks.REDSTONE_BLOCK));
    }
}
