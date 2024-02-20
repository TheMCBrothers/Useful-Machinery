package net.themcbrothers.usefulmachinery.core;

import com.google.gson.JsonArray;
import net.themcbrothers.lib.registries.DeferredBlockEntityType;
import net.themcbrothers.usefulmachinery.block.entity.*;

import static net.themcbrothers.usefulmachinery.core.Registration.BLOCK_ENTITY_TYPES;


public class MachineryBlockEntities {
    public static final DeferredBlockEntityType<CoalGeneratorBlockEntity> COAL_GENERATOR = BLOCK_ENTITY_TYPES.register("coal_generator", CoalGeneratorBlockEntity::new, MachineryBlocks.COAL_GENERATOR);
    public static final DeferredBlockEntityType<LavaGeneratorBlockEntity> LAVA_GENERATOR = BLOCK_ENTITY_TYPES.register("lava_generator", LavaGeneratorBlockEntity::new, MachineryBlocks.LAVA_GENERATOR);
    public static final DeferredBlockEntityType<CreativePowerCellBlockEntity> CREATIVE_POWER_CELL = BLOCK_ENTITY_TYPES.register("creative_power_cell", CreativePowerCellBlockEntity::new, MachineryBlocks.CREATIVE_POWER_CELL);
    public static final DeferredBlockEntityType<ElectricSmelterBlockEntity> ELECTRIC_SMELTER = BLOCK_ENTITY_TYPES.register("electric_smelter", ElectricSmelterBlockEntity::new, MachineryBlocks.ELECTRIC_SMELTER);
    public static final DeferredBlockEntityType<CrusherBlockEntity> CRUSHER = BLOCK_ENTITY_TYPES.register("crusher", CrusherBlockEntity::new, MachineryBlocks.CRUSHER);
    public static final DeferredBlockEntityType<CompactorBlockEntity> COMPACTOR = BLOCK_ENTITY_TYPES.register("compactor", CompactorBlockEntity::new, MachineryBlocks.COMPACTOR);

    static void init() {
    }
}
