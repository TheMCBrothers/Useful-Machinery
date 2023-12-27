package net.themcbrothers.examplemod.core;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.themcbrothers.examplemod.block.ExampleDirectionBlock;

import java.util.function.Function;

import static net.themcbrothers.examplemod.core.Registration.BLOCKS;
import static net.themcbrothers.examplemod.core.Registration.ITEMS;

public final class ExampleBlocks {
    static void init() {
    }

    public static final DeferredBlock<Block> EXAMPLE_BLOCK = registerBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));

    public static final DeferredBlock<Block> SECONDARY_EXAMPLE_BLOCK = registerBlock("secondary_example_block", ExampleDirectionBlock::new, BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE));

    private static DeferredBlock<Block> registerBlock(String name, BlockBehaviour.Properties props) {
        return registerBlock(name, Block::new, props);
    }

    private static <B extends Block> DeferredBlock<B> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> func, BlockBehaviour.Properties props) {
        DeferredBlock<B> deferredBlock = BLOCKS.registerBlock(name, func, props);
        ITEMS.registerSimpleBlockItem(deferredBlock);
        return deferredBlock;
    }
}
