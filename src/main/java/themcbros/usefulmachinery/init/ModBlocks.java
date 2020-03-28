package themcbros.usefulmachinery.init;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.blocks.*;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = UsefulMachinery.MOD_ID)
public class ModBlocks {

    private static final List<Block> BLOCKS = Lists.newArrayList();

    public static final MachineBlock COAL_GENERATOR = register("coal_generator", new CoalGeneratorBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(2f).sound(SoundType.METAL)));
    public static final MachineBlock LAVA_GENERATOR = register("lava_generator", new LavaGeneratorBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(2f).sound(SoundType.METAL)));
    public static final MachineBlock CRUSHER = register("crusher", new CrusherBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(2f).sound(SoundType.METAL)));
    public static final MachineBlock ELECTRIC_SMELTER = register("electric_smelter", new ElectricSmelterBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(2f).sound(SoundType.METAL)));
    public static final MachineBlock COMPACTOR = register("compactor", new CompactorBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(2f).sound(SoundType.METAL)));
    public static final WireBlock WIRE = register("wire", new WireBlock(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(.5f).sound(SoundType.METAL)));
    public static final FramedBundledCableBlock FRAMED_BUNDLED_CABLE = register("framed_bundled_cable", new FramedBundledCableBlock(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(.5f).sound(SoundType.METAL)));
    public static final FramedRedstoneWireBlock FRAMED_REDSTONE_WIRE = register("framed_redstone_wire", new FramedRedstoneWireBlock(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(.5f).sound(SoundType.METAL)));
    public static final CreativePowerCellBlock CREATIVE_POWER_CELL = register("creative_power_cell", new CreativePowerCellBlock(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(.5f).sound(SoundType.METAL)));

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
