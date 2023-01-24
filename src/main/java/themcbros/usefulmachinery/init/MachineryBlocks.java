package themcbros.usefulmachinery.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.themcbrothers.lib.registration.object.ItemObject;
import themcbros.usefulmachinery.blocks.*;
import themcbros.usefulmachinery.items.CreativePowerCellItem;
import themcbros.usefulmachinery.items.MachineBlockItem;

import java.util.function.Function;

import static themcbros.usefulmachinery.init.Registration.BLOCKS;

public class MachineryBlocks {
    private static final Item.Properties PROPS = new Item.Properties();
    private static final Function<? super Block, MachineBlockItem> MACHINE_BLOCK_ITEM = block -> new MachineBlockItem(block, PROPS);

    public static final ItemObject<AbstractMachineBlock> CRUSHER = BLOCKS.register("crusher", () -> new CrusherBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), MACHINE_BLOCK_ITEM);
    public static final ItemObject<AbstractMachineBlock> COAL_GENERATOR = BLOCKS.register("coal_generator", () -> new CoalGeneratorBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), MACHINE_BLOCK_ITEM);
    public static final ItemObject<AbstractMachineBlock> LAVA_GENERATOR = BLOCKS.register("lava_generator", () -> new LavaGeneratorBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), MACHINE_BLOCK_ITEM);
    public static final ItemObject<AbstractMachineBlock> ELECTRIC_SMELTER = BLOCKS.register("electric_smelter", () -> new ElectricSmelterBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), MACHINE_BLOCK_ITEM);
    public static final ItemObject<AbstractMachineBlock> COMPACTOR = BLOCKS.register("compactor", () -> new CompactorBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), MACHINE_BLOCK_ITEM);
    public static final ItemObject<CreativePowerCellBlock> CREATIVE_POWER_CELL = BLOCKS.register("creative_power_cell", () -> new CreativePowerCellBlock(Block.Properties.of(Material.DECORATION).strength(.5f).sound(SoundType.METAL).noLootTable()), block -> new CreativePowerCellItem(block, PROPS));


    protected static void init() {
    }
}
