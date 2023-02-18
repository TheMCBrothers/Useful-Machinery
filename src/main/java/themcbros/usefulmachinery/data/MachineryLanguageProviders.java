package themcbros.usefulmachinery.data;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.init.MachineryItems;

import static themcbros.usefulmachinery.init.MachineryBlocks.*;

public class MachineryLanguageProviders {
    public static class EnglishUS extends LanguageProvider {
        public EnglishUS(PackOutput output) {
            super(output, UsefulMachinery.MOD_ID, "en_us");
        }

        @Override
        protected void addTranslations() {
            //Creative Tab
            this.add("itemGroup.usefulmachinery", "Useful Machinery");

            //Machines
            this.add(COAL_GENERATOR.get(), "Coal Generator");
            this.add(COMPACTOR.get(), "Compactor");
            this.add(CREATIVE_POWER_CELL.get(), "Creative Power Cell");
            this.add(CRUSHER.get(), "Crusher");
            this.add(ELECTRIC_SMELTER.get(), "Electric Smelter");
            this.add(LAVA_GENERATOR.get(), "Lava Generator");

            //Items
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

            //Container Titles
            this.add("container.usefulmachinery.coal_generator", "Coal Generator");
            this.add("container.usefulmachinery.compactor", "Compactor");
            this.add("container.usefulmachinery.crusher", "Crusher");
            this.add("container.usefulmachinery.electric_smelter", "Electric Smelter");
            this.add("container.usefulmachinery.lava_generator", "Lava Generator");

            //JEI
            this.add("jei.usefulmachinery.crushing", "Crushing");
            this.add("jei.usefulmachinery.electric_smelting", "Electric Smelting");
            this.add("jei.usefulmachinery.compacting", "Compacting");
            this.add("jei.usefulmachinery.fuel", "Fuel for RF");

            //Misc
            this.add("misc.usefulmachinery.energy", "%s FE");
            this.add("misc.usefulmachinery.energyWithMax", "%s / %s FE");
            this.add("misc.usefulmachinery.empty", "Empty");
            this.add("misc.usefulmachinery.fluidWithMax", "%s / %s mB");
            this.add("misc.usefulmachinery.fluidWithMaxName", "%s, %s / %s mB");
            this.add("misc.usefulmachinery.redstoneMode", "Redstone Mode, %s");
            this.add("misc.usefulmachinery.compact_plate", "Plate Press");
            this.add("misc.usefulmachinery.compact_gear", "Gear Manufacturing");
            this.add("misc.usefulmachinery.compact_block", "Block Former");

            //Stats
            this.add("stat.usefulmachinery.interact_with_crusher", "Interactions with Crusher");
            this.add("stat.usefulmachinery.interact_with_electric_smelter", "Interactions with Electric Smelter");
            this.add("stat.usefulmachinery.interact_with_compactor", "Interactions with Compactor");
            this.add("stat.usefulmachinery.interact_with_coal_generator", "Interactions with Coal Generator");
            this.add("stat.usefulmachinery.interact_with_lava_generator", "Interactions with Lava Generator");
        }
    }

    public static class SwissGerman extends LanguageProvider {
        public SwissGerman(PackOutput output) {
            super(output, UsefulMachinery.MOD_ID, "de_ch");
        }

        @Override
        protected void addTranslations() {
            //Creative Tab
            this.add("itemGroup.usefulmachinery", "Useful Machinery");

            //Machines
            this.add(COAL_GENERATOR.get(), "Cholegenerator");
            this.add(COMPACTOR.get(), "Kompaktierer");
            this.add(CREATIVE_POWER_CELL.get(), "Kreativi Energiezaelle");
            this.add(CRUSHER.get(), "Zerbroesmeler");
            this.add(ELECTRIC_SMELTER.get(), "Elektrischi Schmelzi");
            this.add(LAVA_GENERATOR.get(), "Lavagenerator");

            //Items
            this.add(MachineryItems.BATTERY.get(), "Batterie");
            this.add(MachineryItems.COMPACTOR_KIT.get(), "Kompaktier Bousatz");
            this.add(MachineryItems.MACHINE_FRAME.get(), "Maschineteil");
            this.add(MachineryItems.TIER_UPGRADE.get().getDescriptionId() + "_basic", "Eifachs Oepgraid");
            this.add(MachineryItems.TIER_UPGRADE.get().getDescriptionId() + "_reinforced", "Verstaerkts Oepgraid");
            this.add(MachineryItems.TIER_UPGRADE.get().getDescriptionId() + "_factory", "Fabrik Oepgraid");
            this.add(MachineryItems.TIER_UPGRADE.get().getDescriptionId() + "_overkill", "Übermaessigs Oepgraid");
            this.add(MachineryItems.EFFICIENCY_UPGRADE.get(), "Effizianz Oepgrade");
            this.add(MachineryItems.PRECISION_UPGRADE.get(), "Gnaus Oepgrade");
            this.add(MachineryItems.SUSTAINED_UPGRADE.get(), "Oekologischs Oepgrade");

            //Container Titles
            this.add("container.usefulmachinery.coal_generator", "Cholegenerator");
            this.add("container.usefulmachinery.compactor", "Kompaktierer");
            this.add("container.usefulmachinery.crusher", "Zerbroesmeler");
            this.add("container.usefulmachinery.electric_smelter", "Elektrischi Schmelzi");
            this.add("container.usefulmachinery.lava_generator", "Lavagenerator");

            //JEI
            this.add("jei.usefulmachinery.crushing", "Zerbroesmele");
            this.add("jei.usefulmachinery.electric_smelting", "Elektrisch Schmelzae");
            this.add("jei.usefulmachinery.compacting", "Kompaktiere");
            this.add("jei.usefulmachinery.fuel", "Triebstoff fuer Strom");

            //Misc
            this.add("misc.usefulmachinery.energy", "%s FE");
            this.add("misc.usefulmachinery.energyWithMax", "%s / %s FE");
            this.add("misc.usefulmachinery.empty", "Laer");
            this.add("misc.usefulmachinery.fluidWithMax", "%s / %s mB");
            this.add("misc.usefulmachinery.fluidWithMaxName", "%s, %s / %s mB");
            this.add("misc.usefulmachinery.redstoneMode", "Rotsteimodus, %s");
            this.add("misc.usefulmachinery.compact_plate", "Plattepraessi");
            this.add("misc.usefulmachinery.compact_gear", "Zahradherstellig");
            this.add("misc.usefulmachinery.compact_block", "Blockbilder");

            //Stats
            this.add("stat.usefulmachinery.interact_with_crusher", "Ustusch mitem Zerbroesmeler");
            this.add("stat.usefulmachinery.interact_with_electric_smelter", "Ustusch mit dae Elektrischi Schmelzi");
            this.add("stat.usefulmachinery.interact_with_compactor", "Ustusch mitem Kompaktierer");
            this.add("stat.usefulmachinery.interact_with_coal_generator", "Ustusch mitem Cholegenerator");
            this.add("stat.usefulmachinery.interact_with_lava_generator", "Ustusch mitem Lavagenerator");
        }
    }
}


