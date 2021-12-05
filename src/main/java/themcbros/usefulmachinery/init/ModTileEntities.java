package themcbros.usefulmachinery.init;

import com.google.common.collect.Lists;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.tileentity.*;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = UsefulMachinery.MOD_ID)
public class ModTileEntities {
    private static final List<BlockEntityType<?>> TILE_ENTITY_TYPES = Lists.newArrayList();

    public static final BlockEntityType<CoalGeneratorTileEntity> COAL_GENERATOR = register("coal_generator", BlockEntityType.Builder.of(CoalGeneratorTileEntity::new, ModBlocks.COAL_GENERATOR).build(null));
    public static final BlockEntityType<LavaGeneratorTileEntity> LAVA_GENERATOR = register("lava_generator", BlockEntityType.Builder.of(LavaGeneratorTileEntity::new, ModBlocks.LAVA_GENERATOR).build(null));
    public static final BlockEntityType<CrusherTileEntity> CRUSHER = register("crusher", BlockEntityType.Builder.of(CrusherTileEntity::new, ModBlocks.CRUSHER).build(null));
    public static final BlockEntityType<ElectricSmelterTileEntity> ELECTRIC_SMELTER = register("electric_smelter", BlockEntityType.Builder.of(ElectricSmelterTileEntity::new, ModBlocks.ELECTRIC_SMELTER).build(null));
    public static final BlockEntityType<CompactorTileEntity> COMPACTOR = register("compactor", BlockEntityType.Builder.of(CompactorTileEntity::new, ModBlocks.COMPACTOR).build(null));
    public static final BlockEntityType<CreativePowerCellTileEntity> CREATIVE_POWER_CELL = register("creative_power_cell", BlockEntityType.Builder.of(CreativePowerCellTileEntity::new, ModBlocks.CREATIVE_POWER_CELL).build(null));

    private static <T extends BlockEntityType<?>> T register(String registryName, T BlockEntityType) {
        BlockEntityType.setRegistryName(UsefulMachinery.getId(registryName));
        TILE_ENTITY_TYPES.add(BlockEntityType);
        return BlockEntityType;
    }

    @SubscribeEvent
    public static void onBlockRegistry(final RegistryEvent.Register<BlockEntityType<?>> event) {
        TILE_ENTITY_TYPES.forEach(event.getRegistry()::register);
    }
}
