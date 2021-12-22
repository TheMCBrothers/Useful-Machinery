package themcbros.usefulmachinery.init;

import com.google.common.collect.Lists;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.blockentity.*;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = UsefulMachinery.MOD_ID)
public class MachineryBlockEntities {
    private static final List<BlockEntityType<?>> TILE_ENTITY_TYPES = Lists.newArrayList();

    public static final BlockEntityType<CoalGeneratorBlockEntity> COAL_GENERATOR = register("coal_generator", BlockEntityType.Builder.of(CoalGeneratorBlockEntity::new, MachineryBlocks.COAL_GENERATOR).build(null));
    public static final BlockEntityType<LavaGeneratorBlockEntity> LAVA_GENERATOR = register("lava_generator", BlockEntityType.Builder.of(LavaGeneratorBlockEntity::new, MachineryBlocks.LAVA_GENERATOR).build(null));
    public static final BlockEntityType<CrusherBlockEntity> CRUSHER = register("crusher", BlockEntityType.Builder.of(CrusherBlockEntity::new, MachineryBlocks.CRUSHER).build(null));
    public static final BlockEntityType<ElectricSmelterBlockEntity> ELECTRIC_SMELTER = register("electric_smelter", BlockEntityType.Builder.of(ElectricSmelterBlockEntity::new, MachineryBlocks.ELECTRIC_SMELTER).build(null));
    public static final BlockEntityType<CompactorBlockEntity> COMPACTOR = register("compactor", BlockEntityType.Builder.of(CompactorBlockEntity::new, MachineryBlocks.COMPACTOR).build(null));
    public static final BlockEntityType<CreativePowerCellBlockEntity> CREATIVE_POWER_CELL = register("creative_power_cell", BlockEntityType.Builder.of(CreativePowerCellBlockEntity::new, MachineryBlocks.CREATIVE_POWER_CELL).build(null));

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
