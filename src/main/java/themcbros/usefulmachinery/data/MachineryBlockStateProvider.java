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
        ModelFile model = models().orientable(Objects.requireNonNull(block.getRegistryName()).getPath(), modLoc("block/machine_side"), modLoc("block/" + block.getRegistryName().getPath() + "_front"), modLoc("block/machine_top"));
        ModelFile modelOn = models().orientable(block.getRegistryName().getPath() + "_on", modLoc("block/machine_side"), modLoc("block/" + block.getRegistryName().getPath() + "_front_on"), modLoc("block/machine_top"));

        horizontalBlock(block, state -> state.getValue(BlockStateProperties.LIT) ? modelOn : model);
    }

    private void simpleBlockItem(Block block) {
        simpleBlockItem(block, models().getExistingFile(modLoc("block/" + Objects.requireNonNull(block.getRegistryName()).getPath())));
    }

    private void machineBlockItem(Block block) {
        itemModels().getBuilder(Objects.requireNonNull(block.getRegistryName()).getPath());
    }
}
