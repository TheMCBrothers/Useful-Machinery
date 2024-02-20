package net.themcbrothers.usefulmachinery.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MachineryLootTableProvider extends LootTableProvider {
    public MachineryLootTableProvider(PackOutput output) {
        super(output, Set.of(), VanillaLootTableProvider.create(output).getTables());
    }

    @Override
    public List<SubProviderEntry> getTables() {
        return List.of(new LootTableProvider.SubProviderEntry(MachineryBlockLootSubProvider::new, LootContextParamSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext context) {
        map.forEach((location, lootTable) -> {
            LootDataId<?> id = new LootDataId<>(LootDataType.TABLE, location);

            lootTable.validate(context.setParams(lootTable.getParamSet()).enterElement("{" + location + "}", id));
        });
    }
}
