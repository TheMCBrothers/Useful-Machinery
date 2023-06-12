package themcbros.usefulmachinery.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.themcbrothers.lib.registration.object.ItemObject;
import themcbros.usefulmachinery.blocks.*;
import themcbros.usefulmachinery.items.CreativePowerCellItem;

import java.util.function.Function;

import static themcbros.usefulmachinery.init.Registration.BLOCKS;

public class MachineryBlocks {
    private static final Item.Properties PROPS = new Item.Properties();
    private static final Function<? super Block, BlockItem> BLOCK_ITEM = block -> new BlockItem(block, PROPS);

    public static final ItemObject<AbstractMachineBlock> CRUSHER = BLOCKS.register("crusher", () -> new CrusherBlock(Block.Properties.of().strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), BLOCK_ITEM);
    public static final ItemObject<AbstractMachineBlock> COAL_GENERATOR = BLOCKS.register("coal_generator", () -> new CoalGeneratorBlock(Block.Properties.of().strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), BLOCK_ITEM);
    public static final ItemObject<AbstractMachineBlock> LAVA_GENERATOR = BLOCKS.register("lava_generator", () -> new LavaGeneratorBlock(Block.Properties.of().strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), BLOCK_ITEM);
    public static final ItemObject<AbstractMachineBlock> ELECTRIC_SMELTER = BLOCKS.register("electric_smelter", () -> new ElectricSmelterBlock(Block.Properties.of().strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), BLOCK_ITEM);
    public static final ItemObject<AbstractMachineBlock> COMPACTOR = BLOCKS.register("compactor", () -> new CompactorBlock(Block.Properties.of().strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), BLOCK_ITEM);
    public static final ItemObject<CreativePowerCellBlock> CREATIVE_POWER_CELL = BLOCKS.register("creative_power_cell", () -> new CreativePowerCellBlock(Block.Properties.of().strength(.5f).sound(SoundType.METAL).noLootTable()), block -> new CreativePowerCellItem(block, new Item.Properties().stacksTo(1)));


    protected static void init() {
    }
}
