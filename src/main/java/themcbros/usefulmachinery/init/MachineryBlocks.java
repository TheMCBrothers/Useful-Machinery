package themcbros.usefulmachinery.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.themcbrothers.lib.registration.object.ItemObject;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.blocks.*;
import themcbros.usefulmachinery.items.MachineBlockItem;

import java.util.function.Supplier;

import static themcbros.usefulmachinery.init.Registration.BLOCKS;

public class MachineryBlocks {
    public static final ItemObject<AbstractMachineBlock> CRUSHER = register("crusher", () -> new CrusherBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), true);
    public static final ItemObject<AbstractMachineBlock> COAL_GENERATOR = register("coal_generator", () -> new CoalGeneratorBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), true);
    public static final ItemObject<AbstractMachineBlock> LAVA_GENERATOR = register("lava_generator", () -> new LavaGeneratorBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), true);
    public static final ItemObject<AbstractMachineBlock> ELECTRIC_SMELTER = register("electric_smelter", () -> new ElectricSmelterBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), true);
    public static final ItemObject<AbstractMachineBlock> COMPACTOR = register("compactor", () -> new CompactorBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), true);
    public static final ItemObject<CreativePowerCellBlock> CREATIVE_POWER_CELL = register("creative_power_cell", () -> new CreativePowerCellBlock(Block.Properties.of(Material.DECORATION).strength(.5f).sound(SoundType.METAL).noDrops()), false);

    private static <T extends Block> ItemObject<T> register(String registryName, Supplier<T> blockSupplier, boolean isMachine) {
        return BLOCKS.register(registryName, blockSupplier, block -> {
            Item.Properties props = new Item.Properties().tab(UsefulMachinery.GROUP);
            if (isMachine) {
                return new MachineBlockItem(block, props);
            }
            return new BlockItem(block, props);
        });
    }

    protected static void init() {
    }
}
