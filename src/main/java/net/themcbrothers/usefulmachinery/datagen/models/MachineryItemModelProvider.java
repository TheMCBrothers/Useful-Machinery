package net.themcbrothers.usefulmachinery.datagen.models;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.themcbrothers.lib.data.models.ModelSubProvider;
import net.themcbrothers.usefulfoundation.core.FoundationItems;
import net.themcbrothers.usefulmachinery.client.tintsources.TierItemTintSource;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.id;
import static net.themcbrothers.usefulmachinery.core.MachineryItems.*;

public class MachineryItemModelProvider extends ModelSubProvider {
    public MachineryItemModelProvider(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        super(blockModels, itemModels);
    }

    @Override
    protected void register() {
        // Machinery Items
        itemModels.generateFlatItem(BATTERY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(EFFICIENCY_UPGRADE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(PRECISION_UPGRADE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(SUSTAINED_UPGRADE.get(), ModelTemplates.FLAT_ITEM);

        itemModels.itemModelOutput.accept(TIER_UPGRADE.get(),
                ItemModelUtils.tintedModel(itemModels.generateLayeredItem(TIER_UPGRADE.get(),
                        TextureMapping.getItemTexture(TIER_UPGRADE.get(), "_0"),
                        TextureMapping.getItemTexture(TIER_UPGRADE.get(), "_1")
                ), ItemModelGenerators.BLANK_LAYER, TierItemTintSource.INSTANCE));

        itemModels.itemModelOutput.accept(MACHINE_FRAME.get(),
                ItemModelUtils.plainModel(ModelTemplates.CUBE_ALL.create(MACHINE_FRAME.get(),
                        new TextureMapping().put(TextureSlot.ALL, new Material(id("block/machine_frame"))), this.modelOutput)));


        itemModels.itemModelOutput.accept(COMPACTOR_KIT.get(),
                ItemModelUtils.plainModel(itemModels.generateLayeredItem(COMPACTOR_KIT.get(),
                        TextureMapping.getItemTexture(FoundationItems.LEAD_PLATE.get()),
                        TextureMapping.getItemTexture(FoundationItems.GOLD_GEAR.get())
                )));

    }
}
