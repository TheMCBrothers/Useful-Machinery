package themcbros.usefulmachinery.data;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.blocks.AbstractMachineBlock;

import java.util.Objects;

import static themcbros.usefulmachinery.init.MachineryBlocks.*;

public class MachineryBlockStateProvider extends BlockStateProvider {
    public MachineryBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, UsefulMachinery.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(CREATIVE_POWER_CELL.get(), cubeAll(Blocks.REDSTONE_BLOCK));

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
        simpleBlockItem(CREATIVE_POWER_CELL.get(), cubeAll(Blocks.REDSTONE_BLOCK));
    }

    private void horizontalMachineBlock(Block block) {
        ModelFile model = models().withExistingParent(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath(), modLoc("block/machine_block"))
                .texture("front", modLoc("block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath() + "_front"))
                .renderType("cutout");

        ModelFile modelOn = models().withExistingParent(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath() + "_on", modLoc("block/machine_block"))
                .texture("front", modLoc("block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath() + "_front_on"))
                .renderType("cutout");

        getVariantBuilder(block)
                .forAllStatesExcept(state -> ConfiguredModel.builder()
                        .modelFile(state.getValue(BlockStateProperties.LIT) ? modelOn : model)
                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                        .build(),
                        AbstractMachineBlock.TIER
                );
    }

    private void simpleBlockItem(Block block) {
        simpleBlockItem(block, models().getExistingFile(modLoc(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath())));
    }
}
