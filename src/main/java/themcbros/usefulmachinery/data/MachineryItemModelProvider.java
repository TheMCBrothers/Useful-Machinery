package themcbros.usefulmachinery.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import themcbros.usefulmachinery.UsefulMachinery;

import java.util.Objects;

import static themcbros.usefulmachinery.init.MachineryItems.*;

public class MachineryItemModelProvider extends ItemModelProvider {
    public MachineryItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, UsefulMachinery.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Machinery Items
        basicItem(BATTERY.get());

        withExistingParent(Objects.requireNonNull(TIER_UPGRADE.get().getRegistryName()).getPath(), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/tier_upgrade_0"))
                .texture("layer1", modLoc("item/tier_upgrade_1"));

        ResourceLocation machineFrame = MACHINE_FRAME.get().getRegistryName();
        withExistingParent(Objects.requireNonNull(machineFrame).getPath(), mcLoc("block/cube_all"))
                .texture("all", modLoc("block/" + machineFrame.getPath()));
    }
}
