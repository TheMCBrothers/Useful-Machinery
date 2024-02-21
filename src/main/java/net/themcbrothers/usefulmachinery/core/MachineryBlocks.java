package net.themcbrothers.usefulmachinery.core;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.themcbrothers.usefulmachinery.block.*;
import net.themcbrothers.usefulmachinery.item.CreativePowerCellItem;

import static net.themcbrothers.usefulmachinery.core.Registration.BLOCKS;

public final class MachineryBlocks {
    public static final DeferredBlock<CoalGeneratorBlock> COAL_GENERATOR = BLOCKS.registerBlock("coal_generator", CoalGeneratorBlock::new, BlockBehaviour.Properties.of().strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops());
    public static final DeferredBlock<CompactorBlock> COMPACTOR = BLOCKS.registerBlock("compactor", CompactorBlock::new, BlockBehaviour.Properties.of().strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops());
    public static final DeferredBlock<CrusherBlock> CRUSHER = BLOCKS.registerBlock("crusher", CrusherBlock::new, BlockBehaviour.Properties.of().strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops());
    public static final DeferredBlock<ElectricSmelterBlock> ELECTRIC_SMELTER = BLOCKS.registerBlock("electric_smelter", ElectricSmelterBlock::new, BlockBehaviour.Properties.of().strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops());
    public static final DeferredBlock<LavaGeneratorBlock> LAVA_GENERATOR = BLOCKS.registerBlock("lava_generator", LavaGeneratorBlock::new, BlockBehaviour.Properties.of().strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops());
    public static final DeferredBlock<Block> CREATIVE_POWER_CELL = BLOCKS.register("creative_power_cell", () -> new CreativePowerCellBlock(BlockBehaviour.Properties.of().strength(0.5F).sound(SoundType.METAL).noLootTable()), block -> new CreativePowerCellItem(block, new Item.Properties().stacksTo(1)));

    static void init() {
    }
}
