package themcbros.usefulmachinery.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.blocks.AbstractMachineBlock;
import themcbros.usefulmachinery.blocks.CoalGeneratorBlock;
import themcbros.usefulmachinery.blocks.CompactorBlock;
import themcbros.usefulmachinery.blocks.CreativePowerCellBlock;
import themcbros.usefulmachinery.blocks.CrusherBlock;
import themcbros.usefulmachinery.blocks.ElectricSmelterBlock;
import themcbros.usefulmachinery.blocks.LavaGeneratorBlock;
import themcbros.usefulmachinery.items.MachineBlockItem;

import java.util.function.Supplier;

import static themcbros.usefulmachinery.init.Registration.BLOCKS;
import static themcbros.usefulmachinery.init.Registration.ITEMS;

public class MachineryBlocks {
    public static final RegistryObject<AbstractMachineBlock> CRUSHER = register("crusher", () -> new CrusherBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), true);
    public static final RegistryObject<AbstractMachineBlock> COAL_GENERATOR = register("coal_generator", () -> new CoalGeneratorBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), true);
    public static final RegistryObject<AbstractMachineBlock> LAVA_GENERATOR = register("lava_generator", () -> new LavaGeneratorBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), true);
    public static final RegistryObject<AbstractMachineBlock> ELECTRIC_SMELTER = register("electric_smelter", () -> new ElectricSmelterBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), true);
    public static final RegistryObject<AbstractMachineBlock> COMPACTOR = register("compactor", () -> new CompactorBlock(Block.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).requiresCorrectToolForDrops()), true);
    public static final RegistryObject<CreativePowerCellBlock> CREATIVE_POWER_CELL = register("creative_power_cell", () -> new CreativePowerCellBlock(Block.Properties.of(Material.DECORATION).strength(.5f).sound(SoundType.METAL).noLootTable()), false);

    private static <T extends Block> RegistryObject<T> register(String registryName, Supplier<T> block, boolean isMachine) {
        RegistryObject<T> blockObj = BLOCKS.register(registryName, block);
        ITEMS.register(registryName, () -> {
            Item.Properties props = new Item.Properties().tab(UsefulMachinery.GROUP);
            if (isMachine) {
                return new MachineBlockItem(blockObj.get(), props);
            }
            return new BlockItem(blockObj.get(), props);
        });
        return blockObj;
    }

    protected static void init() {
    }
}
