package net.themcbrothers.usefulmachinery.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.core.MachineryItems;

import static net.themcbrothers.usefulmachinery.core.MachineryBlocks.*;

public class MachineryLanguageProvider extends LanguageProvider {

    public MachineryLanguageProvider(PackOutput output) {
        super(output, UsefulMachinery.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // Creative Tab
        this.add("itemGroup.usefulmachinery.base", "Useful Machinery");

        // Machines
        this.add(COAL_GENERATOR.get(), "Coal Generator");
        this.add(COMPACTOR.get(), "Compactor");
        this.add(CREATIVE_POWER_CELL.get(), "Creative Power Cell");
        this.add(CRUSHER.get(), "Crusher");
        this.add(ELECTRIC_SMELTER.get(), "Electric Smelter");
        this.add(LAVA_GENERATOR.get(), "Lava Generator");

        // Items
        this.add(MachineryItems.BATTERY.get(), "Battery");
        this.add(MachineryItems.COMPACTOR_KIT.get(), "Compactor Kit");
        this.add(MachineryItems.MACHINE_FRAME.get(), "Machine Frame");
        this.add(MachineryItems.TIER_UPGRADE.get().getDescriptionId() + "_basic", "Basic Upgrade");
        this.add(MachineryItems.TIER_UPGRADE.get().getDescriptionId() + "_reinforced", "Reinforced Upgrade");
        this.add(MachineryItems.TIER_UPGRADE.get().getDescriptionId() + "_factory", "Factory Upgrade");
        this.add(MachineryItems.TIER_UPGRADE.get().getDescriptionId() + "_overkill", "Overkill Upgrade");
        this.add(MachineryItems.EFFICIENCY_UPGRADE.get(), "Efficiency Upgrade");
        this.add(MachineryItems.PRECISION_UPGRADE.get(), "Precision Upgrade");
        this.add(MachineryItems.SUSTAINED_UPGRADE.get(), "Sustained Upgrade");

        // Item Tooltips
        this.add("tooltip.usefulmachinery.upgrade.shift.header", "Supported by:");
        this.add("tooltip.usefulmachinery.upgrade.header", "Hold shift to see more...");

        // Messages
        this.add("message.usefulmachinery.upgrade.applied.success", "Successfully upgraded machine to ");
        this.add("message.usefulmachinery.upgrade.applied.fail", "This is not a valid upgrade item");

        // Container Titles
        this.add("container.usefulmachinery.coal_generator", "Coal Generator");
        this.add("container.usefulmachinery.compactor", "Compactor");
        this.add("container.usefulmachinery.crusher", "Crusher");
        this.add("container.usefulmachinery.electric_smelter", "Electric Smelter");
        this.add("container.usefulmachinery.lava_generator", "Lava Generator");

        // JEI
        this.add("jei.usefulmachinery.crushing", "Crushing");
        this.add("jei.usefulmachinery.electric_smelting", "Electric Smelting");
        this.add("jei.usefulmachinery.compacting", "Compacting");
        this.add("jei.usefulmachinery.fuel", "Fuel for RF");

        // Misc
        this.add("misc.usefulmachinery.energy", "%s FE");
        this.add("misc.usefulmachinery.energyWithMax", "%s / %s FE");
        this.add("misc.usefulmachinery.empty", "Empty");
        this.add("misc.usefulmachinery.fluidWithMax", "%s / %s mB");
        this.add("misc.usefulmachinery.fluidWithMaxName", "%s, %s / %s mB");
        this.add("misc.usefulmachinery.redstoneMode", "Redstone Mode, %s");
        this.add("misc.usefulmachinery.compact_plate", "Plate Press");
        this.add("misc.usefulmachinery.compact_gear", "Gear Manufacturing");
        this.add("misc.usefulmachinery.compact_block", "Block Former");

        // Stats
        this.add("stat.usefulmachinery.interact_with_crusher", "Interactions with Crusher");
        this.add("stat.usefulmachinery.interact_with_electric_smelter", "Interactions with Electric Smelter");
        this.add("stat.usefulmachinery.interact_with_compactor", "Interactions with Compactor");
        this.add("stat.usefulmachinery.interact_with_coal_generator", "Interactions with Coal Generator");
        this.add("stat.usefulmachinery.interact_with_lava_generator", "Interactions with Lava Generator");
    }

}
