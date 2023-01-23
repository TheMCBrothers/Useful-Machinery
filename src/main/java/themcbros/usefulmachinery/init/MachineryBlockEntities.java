package themcbros.usefulmachinery.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import themcbros.usefulmachinery.blockentity.*;

import static themcbros.usefulmachinery.init.Registration.BLOCK_ENTITY_TYPES;


public class MachineryBlockEntities {
    public static final RegistryObject<BlockEntityType<CoalGeneratorBlockEntity>> COAL_GENERATOR = BLOCK_ENTITY_TYPES.register("coal_generator", CoalGeneratorBlockEntity::new, MachineryBlocks.COAL_GENERATOR);
    public static final RegistryObject<BlockEntityType<LavaGeneratorBlockEntity>> LAVA_GENERATOR = BLOCK_ENTITY_TYPES.register("lava_generator", LavaGeneratorBlockEntity::new, MachineryBlocks.LAVA_GENERATOR);
    public static final RegistryObject<BlockEntityType<CrusherBlockEntity>> CRUSHER = BLOCK_ENTITY_TYPES.register("crusher", CrusherBlockEntity::new, MachineryBlocks.CRUSHER);
    public static final RegistryObject<BlockEntityType<ElectricSmelterBlockEntity>> ELECTRIC_SMELTER = BLOCK_ENTITY_TYPES.register("electric_smelter", ElectricSmelterBlockEntity::new, MachineryBlocks.ELECTRIC_SMELTER);
    public static final RegistryObject<BlockEntityType<CompactorBlockEntity>> COMPACTOR = BLOCK_ENTITY_TYPES.register("compactor", CompactorBlockEntity::new, MachineryBlocks.COMPACTOR);
    public static final RegistryObject<BlockEntityType<CreativePowerCellBlockEntity>> CREATIVE_POWER_CELL = BLOCK_ENTITY_TYPES.register("creative_power_cell", CreativePowerCellBlockEntity::new, MachineryBlocks.CREATIVE_POWER_CELL);

    protected static void init() {
    }
}
