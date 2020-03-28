package themcbros.usefulmachinery.init;

import com.google.common.collect.Lists;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.tileentity.*;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = UsefulMachinery.MOD_ID)
public class ModTileEntities {

    private static final List<TileEntityType<?>> TILE_ENTITY_TYPES = Lists.newArrayList();

    public static final TileEntityType<CoalGeneratorTileEntity> COAL_GENERATOR = register("coal_generator", TileEntityType.Builder.create(CoalGeneratorTileEntity::new, ModBlocks.COAL_GENERATOR).build(null));
    public static final TileEntityType<LavaGeneratorTileEntity> LAVA_GENERATOR = register("lava_generator", TileEntityType.Builder.create(LavaGeneratorTileEntity::new, ModBlocks.LAVA_GENERATOR).build(null));
    public static final TileEntityType<CrusherTileEntity> CRUSHER = register("crusher", TileEntityType.Builder.create(CrusherTileEntity::new, ModBlocks.CRUSHER).build(null));
    public static final TileEntityType<ElectricSmelterTileEntity> ELECTRIC_SMELTER = register("electric_smelter", TileEntityType.Builder.create(ElectricSmelterTileEntity::new, ModBlocks.ELECTRIC_SMELTER).build(null));
    public static final TileEntityType<CompactorTileEntity> COMPACTOR = register("compactor", TileEntityType.Builder.create(CompactorTileEntity::new, ModBlocks.COMPACTOR).build(null));
    public static final TileEntityType<WireTileEntity> WIRE = register("wire", TileEntityType.Builder.create(WireTileEntity::new, ModBlocks.WIRE).build(null));
    public static final TileEntityType<CreativePowerCellTileEntity> CREATIVE_POWER_CELL = register("creative_power_cell", TileEntityType.Builder.create(CreativePowerCellTileEntity::new, ModBlocks.CREATIVE_POWER_CELL).build(null));

    public static final TileEntityType<?> FRAMED_BUNDLED_CABLE = register("bundled_cable", TileEntityType.Builder.create(FramedBundledCableTileEntity::new, ModBlocks.FRAMED_BUNDLED_CABLE).build(null));

    private static <T extends TileEntityType<?>> T register(String registryName, T tileEntityType) {
        tileEntityType.setRegistryName(UsefulMachinery.getId(registryName));
        TILE_ENTITY_TYPES.add(tileEntityType);
        return tileEntityType;
    }

    @SubscribeEvent
    public static void onBlockRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        TILE_ENTITY_TYPES.forEach(event.getRegistry()::register);
    }

}
