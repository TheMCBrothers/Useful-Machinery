package net.themcbrothers.usefulmachinery.datagen;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.themcbrothers.usefulmachinery.block.AbstractMachineBlock;
import net.themcbrothers.usefulmachinery.core.Registration;
import net.themcbrothers.usefulmachinery.machine.MachineTier;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.themcbrothers.usefulmachinery.core.MachineryBlocks.*;

public class MachineryBlockLootSubProvider extends BlockLootSubProvider {
    protected MachineryBlockLootSubProvider() {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Registration.BLOCKS.getEntries().stream().map(DeferredHolder::value).collect(Collectors.toList());
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
        BlockEntityType<?> blockEntityType = Objects.requireNonNull(BuiltInRegistries.BLOCK_ENTITY_TYPE.get(BuiltInRegistries.BLOCK.getKey(block)));

        return LootTable.lootTable()
                .withPool(applyExplosionCondition(block, LootPool.lootPool())
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(block)
                                .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                                .apply(SetContainerContents.setContents(blockEntityType)
                                        .withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("contents"))))
                                .apply(copyNbtFunction)
                                .apply(copyBlockState(block))
                        ));
    }

    private CopyBlockState.Builder copyBlockState(Block block) {
        return CopyBlockState.copyState(block)
                .copy(AbstractMachineBlock.TIER)
                .when(InvertedLootItemCondition.invert(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(AbstractMachineBlock.TIER, MachineTier.SIMPLE))));
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
}
