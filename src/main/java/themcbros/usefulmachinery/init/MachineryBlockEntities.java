package themcbros.usefulmachinery.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import themcbros.usefulmachinery.blockentity.CoalGeneratorBlockEntity;
import themcbros.usefulmachinery.blockentity.CompactorBlockEntity;
import themcbros.usefulmachinery.blockentity.CreativePowerCellBlockEntity;
import themcbros.usefulmachinery.blockentity.CrusherBlockEntity;
import themcbros.usefulmachinery.blockentity.ElectricSmelterBlockEntity;
import themcbros.usefulmachinery.blockentity.LavaGeneratorBlockEntity;

import static themcbros.usefulmachinery.init.Registration.BLOCK_ENTITY_TYPES;


public class MachineryBlockEntities {
    public static final RegistryObject<BlockEntityType<CoalGeneratorBlockEntity>> COAL_GENERATOR = BLOCK_ENTITY_TYPES.register("coal_generator", () -> BlockEntityType.Builder.of(CoalGeneratorBlockEntity::new, MachineryBlocks.COAL_GENERATOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<LavaGeneratorBlockEntity>> LAVA_GENERATOR = BLOCK_ENTITY_TYPES.register("lava_generator", () -> BlockEntityType.Builder.of(LavaGeneratorBlockEntity::new, MachineryBlocks.LAVA_GENERATOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<CrusherBlockEntity>> CRUSHER = BLOCK_ENTITY_TYPES.register("crusher", () -> BlockEntityType.Builder.of(CrusherBlockEntity::new, MachineryBlocks.CRUSHER.get()).build(null));
    public static final RegistryObject<BlockEntityType<ElectricSmelterBlockEntity>> ELECTRIC_SMELTER = BLOCK_ENTITY_TYPES.register("electric_smelter", () -> BlockEntityType.Builder.of(ElectricSmelterBlockEntity::new, MachineryBlocks.ELECTRIC_SMELTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<CompactorBlockEntity>> COMPACTOR = BLOCK_ENTITY_TYPES.register("compactor", () -> BlockEntityType.Builder.of(CompactorBlockEntity::new, MachineryBlocks.COMPACTOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<CreativePowerCellBlockEntity>> CREATIVE_POWER_CELL = BLOCK_ENTITY_TYPES.register("creative_power_cell", () -> BlockEntityType.Builder.of(CreativePowerCellBlockEntity::new, MachineryBlocks.CREATIVE_POWER_CELL.get()).build(null));

    protected static void init() {
    }
}
