package themcbros.usefulmachinery.data;

import net.minecraft.data.loot.BlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import themcbros.usefulmachinery.init.Registration;

import javax.annotation.Nonnull;
import java.util.Objects;

import static themcbros.usefulmachinery.init.MachineryBlocks.*;

public class MachineryBlockLoot extends BlockLoot {
    @Override
    protected void addTables() {
        this.add(COAL_GENERATOR.get(), MachineryBlockLoot::simpleMachineBlock);
        this.add(COMPACTOR.get(), MachineryBlockLoot::simpleMachineBlock);
        this.add(CRUSHER.get(), MachineryBlockLoot::simpleMachineBlock);
        this.add(ELECTRIC_SMELTER.get(), MachineryBlockLoot::simpleMachineBlock);
        this.add(LAVA_GENERATOR.get(), MachineryBlockLoot::simpleMachineBlock);
    }

    private static LootTable.Builder simpleMachineBlock(Block block) {
        return LootTable.lootTable()
                .withPool(applyExplosionCondition(block, LootPool.lootPool())
                        .setRolls(ConstantValue.exactly(1F))
                        .add(LootItem.lootTableItem(block)
                                .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                                .apply(SetContainerContents.setContents(Objects.requireNonNull(ForgeRegistries.BLOCK_ENTITIES.getValue(block.getRegistryName())))
                                        .withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("contents"))))
                                .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                        .copy("EnergyStored", "BlockEntityTag.EnergyStored")
                                        .copy("Items", "BlockEntityTag.Items")
                                        .copy("RedstoneMode", "BlockEntityTag.RedstoneMode"))
                        ));
    }

    @Override
    @Nonnull
    protected Iterable<Block> getKnownBlocks() {
        return Registration.BLOCKS.getEntries().stream().map(RegistryObject::get).toList();
    }
}
