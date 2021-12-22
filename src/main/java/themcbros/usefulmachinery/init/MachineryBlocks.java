package themcbros.usefulmachinery.init;

import com.google.common.collect.Lists;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.blocks.*;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = UsefulMachinery.MOD_ID)
public class MachineryBlocks {
    public static final List<Block> BLOCKS = Lists.newArrayList();

    public static final AbstractMachineBlock COAL_GENERATOR = register("coal_generator", new CoalGeneratorBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    public static final AbstractMachineBlock LAVA_GENERATOR = register("lava_generator", new LavaGeneratorBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    public static final AbstractMachineBlock CRUSHER = register("crusher", new CrusherBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    public static final AbstractMachineBlock ELECTRIC_SMELTER = register("electric_smelter", new ElectricSmelterBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    public static final AbstractMachineBlock COMPACTOR = register("compactor", new CompactorBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    public static final CreativePowerCellBlock CREATIVE_POWER_CELL = register("creative_power_cell", new CreativePowerCellBlock(Block.Properties.of(Material.DECORATION).strength(.5f).sound(SoundType.METAL).noDrops()));

    private static <T extends Block> T register(String registryName, T block) {
        block.setRegistryName(UsefulMachinery.getId(registryName));
        BLOCKS.add(block);
        return block;
    }

    @SubscribeEvent
    public static void onBlockRegistry(final RegistryEvent.Register<Block> event) {
        BLOCKS.forEach(event.getRegistry()::register);
    }
}
