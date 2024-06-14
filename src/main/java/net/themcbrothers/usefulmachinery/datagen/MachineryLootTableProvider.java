package net.themcbrothers.usefulmachinery.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaLootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MachineryLootTableProvider extends LootTableProvider {
    public MachineryLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), VanillaLootTableProvider.create(output, registries).getTables(), registries);
    }

    @Override
    public List<SubProviderEntry> getTables() {
        return List.of(new LootTableProvider.SubProviderEntry(MachineryBlockLootSubProvider::new, LootContextParamSets.BLOCK));
    }

    @Override
    protected void validate(WritableRegistry<LootTable> registry, ValidationContext context, ProblemReporter.Collector collector) {
        super.validate(registry, context, collector);

        registry.holders().forEach((lootTable) -> {
            lootTable.value()
                    .validate(context.setParams(lootTable.value().getParamSet())
                            .enterElement("{" + lootTable.key().location() + "}", lootTable.key()));
        });
    }
}
