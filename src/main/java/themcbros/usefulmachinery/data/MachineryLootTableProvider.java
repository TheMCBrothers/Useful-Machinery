package themcbros.usefulmachinery.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MachineryLootTableProvider extends LootTableProvider {
    public MachineryLootTableProvider(PackOutput output) {
        super(output, Set.of(), VanillaLootTableProvider.create(output).getTables());
    }

    @Override
    @Nonnull
    public List<SubProviderEntry> getTables() {
        return List.of(new LootTableProvider.SubProviderEntry(MachineryBlockLoot::new, LootContextParamSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationContext context) {
        map.forEach((location, lootTable) -> {
            lootTable.validate(context.setParams(lootTable.getParamSet()).enterElement("{" + location + "}", new LootDataId(LootDataType.TABLE, location)));
        });
    }
}
