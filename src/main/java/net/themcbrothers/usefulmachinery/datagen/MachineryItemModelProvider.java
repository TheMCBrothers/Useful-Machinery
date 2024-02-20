package net.themcbrothers.usefulmachinery.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.themcbrothers.usefulmachinery.UsefulMachinery;

import static net.themcbrothers.usefulmachinery.core.MachineryItems.*;

public class MachineryItemModelProvider extends ItemModelProvider {
    public MachineryItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, UsefulMachinery.MOD_ID, existingFileHelper);
    }


    @Override
    protected void registerModels() {
        // Machinery Items
        basicItem(BATTERY.get());
        basicItem(EFFICIENCY_UPGRADE.get());
        basicItem(PRECISION_UPGRADE.get());
        basicItem(SUSTAINED_UPGRADE.get());

        String tierUpgrade = BuiltInRegistries.ITEM.getKey(TIER_UPGRADE.get()).getPath();
        withExistingParent(tierUpgrade, "item/generated")
                .texture("layer0", modLoc("item/tier_upgrade_0"))
                .texture("layer1", modLoc("item/tier_upgrade_1"));

        String machineFrame = BuiltInRegistries.ITEM.getKey(MACHINE_FRAME.get()).getPath();
        withExistingParent(machineFrame, "block/cube_all")
                .texture("all", modLoc("block/" + machineFrame));

        String compactorKit = BuiltInRegistries.ITEM.getKey(COMPACTOR_KIT.get()).getPath();
        withExistingParent(compactorKit, "item/generated")
                .texture("layer0", "usefulfoundation:item/lead_plate")
                .texture("layer1", "usefulfoundation:item/gold_gear");
    }
}
