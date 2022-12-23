package themcbros.usefulmachinery.data;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import themcbros.usefulmachinery.UsefulMachinery;

import java.util.Objects;

import static themcbros.usefulmachinery.init.MachineryBlocks.*;

public class MachineryBlockStateProvider extends BlockStateProvider {
    public MachineryBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, UsefulMachinery.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(CREATIVE_POWER_CELL.get(), cubeAll(Blocks.REDSTONE_BLOCK));

        horizontalMachineBlock(COAL_GENERATOR.get());
        horizontalMachineBlock(COMPACTOR.get());
        horizontalMachineBlock(CRUSHER.get());
        horizontalMachineBlock(ELECTRIC_SMELTER.get());
        horizontalMachineBlock(LAVA_GENERATOR.get());

        machineBlockItem(COAL_GENERATOR.get());
        machineBlockItem(COMPACTOR.get());
        machineBlockItem(CRUSHER.get());
        machineBlockItem(ELECTRIC_SMELTER.get());
        machineBlockItem(LAVA_GENERATOR.get());
        simpleBlockItem(CREATIVE_POWER_CELL.get(), cubeAll(Blocks.REDSTONE_BLOCK));
    }

    private void horizontalMachineBlock(Block block) {
        ModelFile model = models().orientable(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath(), modLoc("block/machine_side"), modLoc("block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath() + "_front"), modLoc("block/machine_top"));
        ModelFile modelOn = models().orientable(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath() + "_on", modLoc("block/machine_side"), modLoc("block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath() + "_front_on"), modLoc("block/machine_top"));
        horizontalBlock(block, state -> state.getValue(BlockStateProperties.LIT) ? modelOn : model);
    }

    private void simpleBlockItem(Block block) {
        simpleBlockItem(block, models().getExistingFile(modLoc(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath())));
    }

    private void machineBlockItem(Block block) {
        ModelFile.UncheckedModelFile modelFile = new ModelFile.UncheckedModelFile("builtin/entity");

        itemModels().getBuilder(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath())
                .parent(modelFile)
                .transforms()
                .transform(ItemTransforms.TransformType.GUI).rotation(30, 225, 0).translation(0, 0, 0).scale(0.625F, 0.625F, 0.625F).end()
                .transform(ItemTransforms.TransformType.GROUND).rotation(0, 0, 0).translation(0, 3, 0).scale(0.25F, 0.25F, 0.25F).end()
                .transform(ItemTransforms.TransformType.FIXED).rotation(0, 0, 0).translation(0, 0, 0).scale(0.5F, 0.5F, 0.5F).end()
                .transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND).rotation(75, 45, 0).translation(0, 2.5F, 0).scale(0.375F, 0.375F, 0.375F).end()
                .transform(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND).rotation(0, 135, 0).translation(0, 0, 0).scale(0.4F, 0.4F, 0.4F).end()
                .transform(ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND).rotation(0, 135, 0).translation(0, 0, 0).scale(0.4F, 0.4F, 0.4F).end()
                .end()
                .guiLight(BlockModel.GuiLight.FRONT);
    }
}
