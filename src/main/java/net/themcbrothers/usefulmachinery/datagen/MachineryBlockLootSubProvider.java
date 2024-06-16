package net.themcbrothers.usefulmachinery.datagen;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.themcbrothers.usefulmachinery.core.Registration;

import java.util.Collections;
import java.util.stream.Collectors;

import static net.themcbrothers.usefulmachinery.core.MachineryBlocks.*;
import static net.themcbrothers.usefulmachinery.core.MachineryDataComponentTypes.*;

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
        this.add(COAL_GENERATOR.get(), block -> simpleMachineBlock(block, copyMachineComponents()));
        this.add(COMPACTOR.get(), block -> simpleMachineBlock(block, copyMachineComponents().include(MODE.get())));
        this.add(CRUSHER.get(), block -> simpleMachineBlock(block, copyMachineComponents()));
        this.add(ELECTRIC_SMELTER.get(), block -> simpleMachineBlock(block, copyMachineComponents()));
        this.add(LAVA_GENERATOR.get(), block -> simpleMachineBlock(block, copyMachineComponentsWithTank()));
    }

    private LootTable.Builder simpleMachineBlock(Block block, LootItemFunction.Builder builder) {
        return LootTable.lootTable()
                .withPool(applyExplosionCondition(block, LootPool.lootPool())
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(block)
                                .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                                .apply(builder)
                        ));
    }

    private CopyComponentsFunction.Builder copyMachineComponents() {
        return CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                .include(DataComponents.CONTAINER)
                .include(CONTENTS.get())
                .include(TIER.get());
    }


    private CopyComponentsFunction.Builder copyMachineComponentsWithTank() {
        return copyMachineComponents()
                .include(TANK.get());
    }
}
