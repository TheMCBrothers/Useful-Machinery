package themcbros.usefulmachinery.data;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
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
import java.util.Collections;
import java.util.Objects;

import static themcbros.usefulmachinery.init.MachineryBlocks.*;

public class MachineryBlockLoot extends BlockLootSubProvider {
    protected MachineryBlockLoot() {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.add(COAL_GENERATOR.get(), this::simpleMachineBlock);
        this.add(COMPACTOR.get(), this::simpleMachineBlock);
        this.add(CRUSHER.get(), this::simpleMachineBlock);
        this.add(ELECTRIC_SMELTER.get(), this::simpleMachineBlock);
        this.add(LAVA_GENERATOR.get(), this::simpleMachineBlock);
    }

    private LootTable.Builder simpleMachineBlock(Block block) {
        return LootTable.lootTable()
                .withPool(applyExplosionCondition(block, LootPool.lootPool())
                        .setRolls(ConstantValue.exactly(1F))
                        .add(LootItem.lootTableItem(block)
                                .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                                .apply(SetContainerContents.setContents(Objects.requireNonNull(ForgeRegistries.BLOCK_ENTITY_TYPES.getValue(ForgeRegistries.BLOCKS.getKey(block))))
                                        .withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("contents"))))
                                .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                        .copy("", "BlockEntityTag"))
                        ));
    }

    @Override
    @Nonnull
    protected Iterable<Block> getKnownBlocks() {
        return Registration.BLOCKS.getEntries().stream().map(RegistryObject::get).toList();
    }
}
