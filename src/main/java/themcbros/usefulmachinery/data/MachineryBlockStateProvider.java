package themcbros.usefulmachinery.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import themcbros.usefulmachinery.UsefulMachinery;

import java.util.Objects;

import static themcbros.usefulmachinery.init.MachineryBlocks.*;

public class MachineryBlockStateProvider extends BlockStateProvider {
    public MachineryBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, UsefulMachinery.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(CREATIVE_POWER_CELL, cubeAll(Blocks.REDSTONE_BLOCK));

        horizontalMachineBlock(COAL_GENERATOR);
        horizontalMachineBlock(COMPACTOR);
        horizontalMachineBlock(CRUSHER);
        horizontalMachineBlock(ELECTRIC_SMELTER);
        horizontalMachineBlock(LAVA_GENERATOR);

        simpleBlockItem(COAL_GENERATOR);
        simpleBlockItem(COMPACTOR);
        simpleBlockItem(CRUSHER);
        simpleBlockItem(ELECTRIC_SMELTER);
        simpleBlockItem(LAVA_GENERATOR);
        simpleBlockItem(CREATIVE_POWER_CELL, cubeAll(Blocks.REDSTONE_BLOCK));
    }

    private void horizontalMachineBlock(Block block) {
        ModelFile model = models().orientable(block.getRegistryName().getPath(), modLoc("block/machine_side"), modLoc("block/" + block.getRegistryName().getPath() + "_front"), modLoc("block/machine_top"));
        ModelFile modelOn = models().orientable(block.getRegistryName().getPath() + "_on", modLoc("block/machine_side"), modLoc("block/" + block.getRegistryName().getPath() + "_front_on"), modLoc("block/machine_top"));

        horizontalBlock(block, state -> state.getValue(BlockStateProperties.LIT) ? modelOn : model);
    }

    private void simpleBlockItem(Block block) {
        simpleBlockItem(block, models().getExistingFile(modLoc("block/" + Objects.requireNonNull(block.getRegistryName()).getPath())));
    }
}
