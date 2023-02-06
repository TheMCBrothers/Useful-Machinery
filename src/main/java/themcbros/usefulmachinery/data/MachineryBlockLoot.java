package themcbros.usefulmachinery.data;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import themcbros.usefulmachinery.blocks.AbstractMachineBlock;
import themcbros.usefulmachinery.init.Registration;
import themcbros.usefulmachinery.machine.MachineTier;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Objects;

import static themcbros.usefulmachinery.init.MachineryBlocks.*;

public class MachineryBlockLoot extends BlockLootSubProvider {
    protected MachineryBlockLoot() {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.add(COAL_GENERATOR.get(), block -> simpleMachineBlock(block, copyMachineNbtWithBurnTime()));
        this.add(COMPACTOR.get(), block -> simpleMachineBlock(block, copyMachineNbt()));
        this.add(CRUSHER.get(), block -> simpleMachineBlock(block, copyMachineNbt()));
        this.add(ELECTRIC_SMELTER.get(), block -> simpleMachineBlock(block, copyMachineNbt()));
        this.add(LAVA_GENERATOR.get(), block -> simpleMachineBlock(block, copyMachineNbtWithBurnTimeAndTank()));
    }

    private LootTable.Builder simpleMachineBlock(Block block, LootItemFunction.Builder copyNbtFunction) {
        return LootTable.lootTable()
                .withPool(applyExplosionCondition(block, LootPool.lootPool())
                        .setRolls(ConstantValue.exactly(1F))
                        .add(LootItem.lootTableItem(block)
                                .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                                .apply(SetContainerContents.setContents(Objects.requireNonNull(ForgeRegistries.BLOCK_ENTITY_TYPES.getValue(ForgeRegistries.BLOCKS.getKey(block))))
                                        .withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("contents"))))
                                .apply(copyNbtFunction)
                                .apply(CopyBlockState.copyState(block)
                                        .copy(AbstractMachineBlock.TIER)
                                        .when(InvertedLootItemCondition.invert(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(AbstractMachineBlock.TIER, MachineTier.SIMPLE)))))
                        ));
    }

    private CopyNbtFunction.Builder copyMachineNbt() {
        return CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy("Items", "BlockEntityTag.Items")
                .copy("Upgrades", "BlockEntityTag.Upgrades")
                .copy("EnergyStored", "BlockEntityTag.EnergyStored")
                .copy("RedstoneMode", "BlockEntityTag.RedstoneMode")
                .copy("ProcessTime", "BlockEntityTag.ProcessTime")
                .copy("ProcessTimeTotal", "BlockEntityTag.ProcessTimeTotal");
    }

    private CopyNbtFunction.Builder copyMachineNbtWithBurnTime() {
        return copyMachineNbt()
                .copy("BurnTime", "BlockEntityTag.BurnTime")
                .copy("BurnTimeTotal", "BlockEntityTag.BurnTimeTotal");
    }

    private CopyNbtFunction.Builder copyMachineNbtWithBurnTimeAndTank() {
        return copyMachineNbtWithBurnTime()
                .copy("Tank", "BlockEntityTag.Tank");
    }

    @Override
    @Nonnull
    protected Iterable<Block> getKnownBlocks() {
        return Registration.BLOCKS.getEntries().stream().map(RegistryObject::get).toList();
    }
}
