package net.themcbrothers.usefulmachinery.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.block.AbstractMachineBlock;

import static net.themcbrothers.usefulmachinery.core.MachineryBlocks.*;

public class MachineryBlockStateProvider extends BlockStateProvider {
    public MachineryBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, UsefulMachinery.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Machine blocks
        horizontalMachineBlock(COAL_GENERATOR.get());
        horizontalMachineBlock(COMPACTOR.get());
        horizontalMachineBlock(CRUSHER.get());
        horizontalMachineBlock(ELECTRIC_SMELTER.get());
        horizontalMachineBlock(LAVA_GENERATOR.get());

        simpleBlockItem(COAL_GENERATOR.get());
        simpleBlockItem(COMPACTOR.get());
        simpleBlockItem(CRUSHER.get());
        simpleBlockItem(ELECTRIC_SMELTER.get());
        simpleBlockItem(LAVA_GENERATOR.get());

        // Common blocks
        simpleBlock(CREATIVE_POWER_CELL.get(), cubeAll(Blocks.REDSTONE_BLOCK));

        simpleBlockItem(CREATIVE_POWER_CELL.get(), cubeAll(Blocks.REDSTONE_BLOCK));
    }

    private void simpleBlockItem(Block block) {
        simpleBlockItem(block, models().getExistingFile(blockTexture(block)));
    }

    private void horizontalMachineBlock(Block block) {
        String blockName = BuiltInRegistries.BLOCK.getKey(block).getPath();
        String blockPath = modLoc("block/" + blockName).getPath();
        ResourceLocation machineBlockModel = modLoc("block/machine_block");

        ModelFile model = models().withExistingParent(blockName, machineBlockModel)
                .texture("front", blockPath + "_front")
                .renderType("cutout");

        ModelFile modelOn = models().withExistingParent(blockName + "_on", machineBlockModel)
                .texture("front", blockPath + "_front_on")
                .renderType("cutout");

        getVariantBuilder(block)
                .forAllStatesExcept(state -> ConfiguredModel.builder()
                                .modelFile(state.getValue(BlockStateProperties.LIT) ? modelOn : model)
                                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                                .build(),
                        AbstractMachineBlock.TIER
                );
    }
}
