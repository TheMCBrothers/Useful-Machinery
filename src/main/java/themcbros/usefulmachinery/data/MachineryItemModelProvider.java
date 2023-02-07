package themcbros.usefulmachinery.data;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import themcbros.usefulmachinery.UsefulMachinery;

import java.util.Objects;

import static themcbros.usefulmachinery.init.MachineryItems.*;

public class MachineryItemModelProvider extends ItemModelProvider {
    public MachineryItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, UsefulMachinery.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Machinery Items
        basicItem(BATTERY.get());
        basicItem(EFFICIENCY_UPGRADE.get());
        basicItem(PRECISION_UPGRADE.get());
        basicItem(SUSTAINED_UPGRADE.get());

        withExistingParent(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(TIER_UPGRADE.get())).getPath(), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/tier_upgrade_0"))
                .texture("layer1", modLoc("item/tier_upgrade_1"));

        String machineFrame = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(MACHINE_FRAME.get())).getPath();
        withExistingParent(machineFrame, mcLoc("block/cube_all"))
                .texture("all", modLoc("block/" + machineFrame));
    }
}
