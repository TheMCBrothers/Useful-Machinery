package themcbros.usefulmachinery.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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
        simpleItem(BATTERY.get());

        ResourceLocation machineFrame = MACHINE_FRAME.get().getRegistryName();
        this.generatedModels.put(machineFrame, this.singleTexture(Objects.requireNonNull(machineFrame).getPath(), mcLoc("block/cube_all"), "all", modLoc("block/" + machineFrame.getPath())));

        this.generatedModels.put(TIER_UPGRADE.get().getRegistryName(), this.getBuilder(Objects.requireNonNull(TIER_UPGRADE.get().getRegistryName()).getPath())
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", modLoc("item/tier_upgrade_0"))
                .texture("layer1", modLoc("item/tier_upgrade_1")));
    }

    private void simpleItem(Item item) {
        ResourceLocation id = item.getRegistryName();
        this.generatedModels.put(id, this.singleTexture(Objects.requireNonNull(id).getPath(), mcLoc("item/generated"), "layer0", modLoc("item/" + id.getPath())));
    }
}
