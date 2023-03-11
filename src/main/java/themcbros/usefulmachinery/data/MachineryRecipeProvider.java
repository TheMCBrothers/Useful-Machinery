package themcbros.usefulmachinery.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import themcbros.usefulmachinery.data.recipes.CompactingRecipeBuilder;
import themcbros.usefulmachinery.data.recipes.CrushingRecipeBuilder;
import themcbros.usefulmachinery.machine.CompactorMode;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

import static themcbros.usefulfoundation.FoundationTags.Items.*;
import static themcbros.usefulfoundation.init.FoundationBlocks.*;
import static themcbros.usefulfoundation.init.FoundationItems.*;
import static themcbros.usefulmachinery.MachineryTags.Items.BATTERIES;
import static themcbros.usefulmachinery.UsefulMachinery.getId;
import static themcbros.usefulmachinery.init.MachineryBlocks.*;
import static themcbros.usefulmachinery.init.MachineryItems.*;

public class MachineryRecipeProvider extends RecipeProvider {
    public MachineryRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@Nonnull Consumer<FinishedRecipe> recipe) {
        //Gears
        CompactingRecipeBuilder.compacting(ALUMINUM_GEAR.get(), Ingredient.of(INGOTS_ALUMINUM), 4, 200, CompactorMode.GEAR).unlockedBy("has_aluminum", has(INGOTS_ALUMINUM)).save(recipe, getId("aluminum_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(BRONZE_GEAR.get(), Ingredient.of(INGOTS_BRONZE), 4, 200, CompactorMode.GEAR).unlockedBy("has_bronze", has(INGOTS_BRONZE)).save(recipe, getId("bronze_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(COPPER_GEAR.get(), Ingredient.of(Tags.Items.INGOTS_COPPER), 4, 200, CompactorMode.GEAR).unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER)).save(recipe, getId("copper_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(DIAMOND_GEAR.get(), Ingredient.of(Tags.Items.GEMS_DIAMOND), 4, 200, CompactorMode.GEAR).unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND)).save(recipe, getId("diamond_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(ELECTRUM_GEAR.get(), Ingredient.of(INGOTS_ELECTRUM), 4, 200, CompactorMode.GEAR).unlockedBy("has_electrum", has(INGOTS_ELECTRUM)).save(recipe, getId("electrum_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(ENDERIUM_GEAR.get(), Ingredient.of(INGOTS_ENDERIUM), 4, 200, CompactorMode.GEAR).unlockedBy("has_enderium", has(INGOTS_ENDERIUM)).save(recipe, getId("enderium_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(GOLD_GEAR.get(), Ingredient.of(Tags.Items.INGOTS_GOLD), 4, 200, CompactorMode.GEAR).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(recipe, getId("gold_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(INVAR_GEAR.get(), Ingredient.of(INGOTS_INVAR), 4, 200, CompactorMode.GEAR).unlockedBy("has_invar", has(INGOTS_INVAR)).save(recipe, getId("invar_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(IRON_GEAR.get(), Ingredient.of(Tags.Items.INGOTS_IRON), 4, 200, CompactorMode.GEAR).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).save(recipe, getId("iron_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(LEAD_GEAR.get(), Ingredient.of(INGOTS_LEAD), 4, 200, CompactorMode.GEAR).unlockedBy("has_lead", has(INGOTS_LEAD)).save(recipe, getId("lead_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(NICKEL_GEAR.get(), Ingredient.of(INGOTS_NICKEL), 4, 200, CompactorMode.GEAR).unlockedBy("has_nickel", has(INGOTS_NICKEL)).save(recipe, getId("nickel_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(PLATINUM_GEAR.get(), Ingredient.of(INGOTS_PLATINUM), 4, 200, CompactorMode.GEAR).unlockedBy("has_platinum", has(INGOTS_PLATINUM)).save(recipe, getId("platinum_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(SIGNALUM_GEAR.get(), Ingredient.of(INGOTS_SIGNALUM), 4, 200, CompactorMode.GEAR).unlockedBy("has_signalum", has(INGOTS_SIGNALUM)).save(recipe, getId("signalum_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(SILVER_GEAR.get(), Ingredient.of(INGOTS_SILVER), 4, 200, CompactorMode.GEAR).unlockedBy("has_silver", has(INGOTS_SILVER)).save(recipe, getId("silver_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(STEEL_GEAR.get(), Ingredient.of(INGOTS_STEEL), 4, 200, CompactorMode.GEAR).unlockedBy("has_steel", has(INGOTS_STEEL)).save(recipe, getId("steel_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(TIN_GEAR.get(), Ingredient.of(INGOTS_TIN), 4, 200, CompactorMode.GEAR).unlockedBy("has_tin", has(INGOTS_TIN)).save(recipe, getId("tin_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(URANIUM_GEAR.get(), Ingredient.of(INGOTS_URANIUM), 4, 200, CompactorMode.GEAR).unlockedBy("has_uranium", has(INGOTS_URANIUM)).save(recipe, getId("uranium_gear_from_compacting"));

        //Storage Blocks
        CompactingRecipeBuilder.compacting(ALUMINUM_BLOCK.get(), Ingredient.of(INGOTS_ALUMINUM), 9, 250, CompactorMode.BLOCK).unlockedBy("has_aluminum", has(INGOTS_ALUMINUM)).save(recipe, getId("aluminum_block_from_compacting"));
        CompactingRecipeBuilder.compacting(BRONZE_BLOCK.get(), Ingredient.of(INGOTS_BRONZE), 9, 250, CompactorMode.BLOCK).unlockedBy("has_bronze", has(INGOTS_BRONZE)).save(recipe, getId("bronze_block_from_compacting"));
        CompactingRecipeBuilder.compacting(Items.COPPER_BLOCK, Ingredient.of(Tags.Items.INGOTS_COPPER), 9, 250, CompactorMode.BLOCK).unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER)).save(recipe, getId("copper_block_from_compacting"));
        CompactingRecipeBuilder.compacting(ELECTRUM_BLOCK.get(), Ingredient.of(INGOTS_ELECTRUM), 9, 250, CompactorMode.BLOCK).unlockedBy("has_electrum", has(INGOTS_ELECTRUM)).save(recipe, getId("electrum_block_from_compacting"));
        CompactingRecipeBuilder.compacting(ENDERIUM_BLOCK.get(), Ingredient.of(INGOTS_ENDERIUM), 9, 250, CompactorMode.BLOCK).unlockedBy("has_enderium", has(INGOTS_ENDERIUM)).save(recipe, getId("enderium_block_from_compacting"));
        CompactingRecipeBuilder.compacting(Items.GOLD_BLOCK, Ingredient.of(Tags.Items.INGOTS_GOLD), 9, 250, CompactorMode.BLOCK).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(recipe, getId("gold_block_from_compacting"));
        CompactingRecipeBuilder.compacting(INVAR_BLOCK.get(), Ingredient.of(INGOTS_INVAR), 9, 250, CompactorMode.BLOCK).unlockedBy("has_invar", has(INGOTS_INVAR)).save(recipe, getId("invar_block_from_compacting"));
        CompactingRecipeBuilder.compacting(Items.IRON_BLOCK, Ingredient.of(Tags.Items.INGOTS_IRON), 9, 250, CompactorMode.BLOCK).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).save(recipe, getId("iron_block_from_compacting"));
        CompactingRecipeBuilder.compacting(LEAD_BLOCK.get(), Ingredient.of(INGOTS_LEAD), 9, 250, CompactorMode.BLOCK).unlockedBy("has_lead", has(INGOTS_LEAD)).save(recipe, getId("lead_block_from_compacting"));
        CompactingRecipeBuilder.compacting(NICKEL_BLOCK.get(), Ingredient.of(INGOTS_NICKEL), 9, 250, CompactorMode.BLOCK).unlockedBy("has_nickel", has(INGOTS_NICKEL)).save(recipe, getId("nickel_block_from_compacting"));
        CompactingRecipeBuilder.compacting(PLATINUM_BLOCK.get(), Ingredient.of(INGOTS_PLATINUM), 9, 250, CompactorMode.BLOCK).unlockedBy("has_platinum", has(INGOTS_PLATINUM)).save(recipe, getId("platinum_block_from_compacting"));
        CompactingRecipeBuilder.compacting(SIGNALUM_BLOCK.get(), Ingredient.of(INGOTS_SIGNALUM), 9, 250, CompactorMode.BLOCK).unlockedBy("has_signalum", has(INGOTS_SIGNALUM)).save(recipe, getId("signalum_block_from_compacting"));
        CompactingRecipeBuilder.compacting(SILVER_BLOCK.get(), Ingredient.of(INGOTS_SILVER), 9, 250, CompactorMode.BLOCK).unlockedBy("has_silver", has(INGOTS_SILVER)).save(recipe, getId("silver_block_from_compacting"));
        CompactingRecipeBuilder.compacting(STEEL_BLOCK.get(), Ingredient.of(INGOTS_STEEL), 9, 250, CompactorMode.BLOCK).unlockedBy("has_steel", has(INGOTS_STEEL)).save(recipe, getId("steel_block_from_compacting"));
        CompactingRecipeBuilder.compacting(TIN_BLOCK.get(), Ingredient.of(INGOTS_TIN), 9, 250, CompactorMode.BLOCK).unlockedBy("has_tin", has(INGOTS_TIN)).save(recipe, getId("tin_block_from_compacting"));
        CompactingRecipeBuilder.compacting(URANIUM_BLOCK.get(), Ingredient.of(INGOTS_URANIUM), 9, 250, CompactorMode.BLOCK).unlockedBy("has_uranium", has(INGOTS_URANIUM)).save(recipe, getId("uranium_block_from_compacting"));

        //Plates
        CompactingRecipeBuilder.compacting(ALUMINUM_PLATE.get(), Ingredient.of(INGOTS_ALUMINUM), 1, 200, CompactorMode.PLATE).unlockedBy("has_aluminum", has(INGOTS_ALUMINUM)).save(recipe, getId("aluminum_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(BRONZE_PLATE.get(), Ingredient.of(INGOTS_BRONZE), 1, 200, CompactorMode.PLATE).unlockedBy("has_bronze", has(INGOTS_BRONZE)).save(recipe, getId("bronze_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(COPPER_PLATE.get(), Ingredient.of(Tags.Items.INGOTS_COPPER), 1, 200, CompactorMode.PLATE).unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER)).save(recipe, getId("copper_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(DIAMOND_PLATE.get(), Ingredient.of(Tags.Items.GEMS_DIAMOND), 1, 200, CompactorMode.PLATE).unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND)).save(recipe, getId("diamond_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(ELECTRUM_PLATE.get(), Ingredient.of(INGOTS_ELECTRUM), 1, 200, CompactorMode.PLATE).unlockedBy("has_electrum", has(INGOTS_ELECTRUM)).save(recipe, getId("electrum_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(ENDERIUM_PLATE.get(), Ingredient.of(INGOTS_ENDERIUM), 1, 200, CompactorMode.PLATE).unlockedBy("has_enderium", has(INGOTS_ENDERIUM)).save(recipe, getId("enderium_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(GOLD_PLATE.get(), Ingredient.of(Tags.Items.INGOTS_GOLD), 1, 200, CompactorMode.PLATE).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(recipe, getId("gold_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(INVAR_PLATE.get(), Ingredient.of(INGOTS_INVAR), 1, 200, CompactorMode.PLATE).unlockedBy("has_invar", has(INGOTS_INVAR)).save(recipe, getId("invar_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(IRON_PLATE.get(), Ingredient.of(Tags.Items.INGOTS_IRON), 1, 200, CompactorMode.PLATE).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).save(recipe, getId("iron_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(LEAD_PLATE.get(), Ingredient.of(INGOTS_LEAD), 1, 200, CompactorMode.PLATE).unlockedBy("has_lead", has(INGOTS_LEAD)).save(recipe, getId("lead_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(NICKEL_PLATE.get(), Ingredient.of(INGOTS_NICKEL), 1, 200, CompactorMode.PLATE).unlockedBy("has_nickel", has(INGOTS_NICKEL)).save(recipe, getId("nickel_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(PLATINUM_PLATE.get(), Ingredient.of(INGOTS_PLATINUM), 1, 200, CompactorMode.PLATE).unlockedBy("has_platinum", has(INGOTS_PLATINUM)).save(recipe, getId("platinum_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(SIGNALUM_PLATE.get(), Ingredient.of(INGOTS_SIGNALUM), 1, 200, CompactorMode.PLATE).unlockedBy("has_signalum", has(INGOTS_SIGNALUM)).save(recipe, getId("signalum_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(SILVER_PLATE.get(), Ingredient.of(INGOTS_SILVER), 1, 200, CompactorMode.PLATE).unlockedBy("has_silver", has(INGOTS_SILVER)).save(recipe, getId("silver_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(STEEL_PLATE.get(), Ingredient.of(INGOTS_STEEL), 1, 200, CompactorMode.PLATE).unlockedBy("has_steel", has(INGOTS_STEEL)).save(recipe, getId("steel_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(TIN_PLATE.get(), Ingredient.of(INGOTS_TIN), 1, 200, CompactorMode.PLATE).unlockedBy("has_tin", has(INGOTS_TIN)).save(recipe, getId("tin_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(URANIUM_PLATE.get(), Ingredient.of(INGOTS_URANIUM), 1, 200, CompactorMode.PLATE).unlockedBy("has_uranium", has(INGOTS_URANIUM)).save(recipe, getId("uranium_plate_from_compacting"));

        //Raw Materials
        Ingredient upgrades = Ingredient.of(EFFICIENCY_UPGRADE, PRECISION_UPGRADE);
        CrushingRecipeBuilder.crushing(RAW_ALUMINUM.get(), 2, Ingredient.of(ORES_ALUMINUM), 200).supportedUpgrades(upgrades).unlockedBy("has_aluminum_ore", has(ORES_ALUMINUM)).save(recipe, getId("raw_aluminum_from_ore"));
        CrushingRecipeBuilder.crushing(Items.RAW_COPPER, 2, Ingredient.of(Tags.Items.ORES_COPPER), 200).supportedUpgrades(upgrades).secondary(Items.RAW_GOLD, 0.05F).unlockedBy("has_copper_ore", has(Tags.Items.ORES_COPPER)).save(recipe, getId("raw_copper_from_ore"));
        CrushingRecipeBuilder.crushing(Items.DIAMOND, 2, Ingredient.of(Tags.Items.ORES_DIAMOND), 200).supportedUpgrades(upgrades).unlockedBy("has_diamond_ore", has(Tags.Items.ORES_DIAMOND)).save(recipe, getId("raw_diamond_from_ore"));
        CrushingRecipeBuilder.crushing(Items.RAW_GOLD, 2, Ingredient.of(Tags.Items.ORES_GOLD), 200).supportedUpgrades(upgrades).secondary(Items.RAW_COPPER, 0.4F).unlockedBy("has_gold_ore", has(Tags.Items.ORES_GOLD)).save(recipe, getId("raw_gold_from_ore"));
        CrushingRecipeBuilder.crushing(Items.RAW_IRON, 2, Ingredient.of(Tags.Items.ORES_IRON), 200).supportedUpgrades(upgrades).secondary(RAW_NICKEL.get(), 0.1F).unlockedBy("has_iron_ore", has(Tags.Items.ORES_IRON)).save(recipe, getId("raw_iron_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_LEAD.get(), 2, Ingredient.of(ORES_LEAD), 200).supportedUpgrades(upgrades).secondary(RAW_SILVER.get(), 0.08F).unlockedBy("has_lead_ore", has(ORES_LEAD)).save(recipe, getId("raw_lead_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_NICKEL.get(), 2, Ingredient.of(ORES_NICKEL), 200).supportedUpgrades(upgrades).unlockedBy("has_nickel_ore", has(ORES_NICKEL)).save(recipe, getId("raw_nickel_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_PLATINUM.get(), 2, Ingredient.of(ORES_PLATINUM), 200).supportedUpgrades(upgrades).unlockedBy("has_platinum_ore", has(ORES_PLATINUM)).save(recipe, getId("raw_platinum_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_SILVER.get(), 2, Ingredient.of(ORES_SILVER), 200).supportedUpgrades(upgrades).secondary(RAW_LEAD.get(), 0.09F).unlockedBy("has_silver_ore", has(ORES_SILVER)).save(recipe, getId("raw_silver_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_TIN.get(), 2, Ingredient.of(ORES_TIN), 200).supportedUpgrades(upgrades).unlockedBy("has_tin_ore", has(ORES_TIN)).save(recipe, getId("raw_tin_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_URANIUM.get(), 2, Ingredient.of(ORES_URANIUM), 200).supportedUpgrades(upgrades).unlockedBy("has_uranium_ore", has(ORES_URANIUM)).save(recipe, getId("raw_uranium_from_ore"));

        //Dusts
        CrushingRecipeBuilder.crushing(ALUMINUM_DUST.get(), 1, Ingredient.of(INGOTS_ALUMINUM), 200).unlockedBy("has_aluminum", has(INGOTS_ALUMINUM)).save(recipe, getId("aluminum_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(BRONZE_DUST.get(), 1, Ingredient.of(INGOTS_BRONZE), 200).unlockedBy("has_bronze", has(INGOTS_BRONZE)).save(recipe, getId("bronze_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(COPPER_DUST.get(), 1, Ingredient.of(Tags.Items.INGOTS_COPPER), 200).unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER)).save(recipe, getId("copper_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(DIAMOND_DUST.get(), 1, Ingredient.of(Tags.Items.GEMS_DIAMOND), 200).unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND)).save(recipe, getId("diamond_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(ELECTRUM_DUST.get(), 1, Ingredient.of(INGOTS_ELECTRUM), 200).unlockedBy("has_electrum", has(INGOTS_ELECTRUM)).save(recipe, getId("electrum_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(ENDERIUM_DUST.get(), 1, Ingredient.of(INGOTS_ENDERIUM), 200).unlockedBy("has_enderium", has(INGOTS_ENDERIUM)).save(recipe, getId("enderium_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(GOLD_DUST.get(), 1, Ingredient.of(Tags.Items.INGOTS_GOLD), 200).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(recipe, getId("gold_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(INVAR_DUST.get(), 1, Ingredient.of(INGOTS_INVAR), 200).unlockedBy("has_invar", has(INGOTS_INVAR)).save(recipe, getId("invar_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(IRON_DUST.get(), 1, Ingredient.of(Tags.Items.INGOTS_IRON), 200).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).save(recipe, getId("iron_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(LEAD_DUST.get(), 1, Ingredient.of(INGOTS_LEAD), 200).unlockedBy("has_lead", has(INGOTS_LEAD)).save(recipe, getId("lead_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(NICKEL_DUST.get(), 1, Ingredient.of(INGOTS_NICKEL), 200).unlockedBy("has_nickel", has(INGOTS_NICKEL)).save(recipe, getId("nickel_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(PLATINUM_DUST.get(), 1, Ingredient.of(INGOTS_PLATINUM), 200).unlockedBy("has_platinum", has(INGOTS_PLATINUM)).save(recipe, getId("platinum_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(SIGNALUM_DUST.get(), 1, Ingredient.of(INGOTS_SIGNALUM), 200).unlockedBy("has_signalum", has(INGOTS_SIGNALUM)).save(recipe, getId("signalum_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(SILVER_DUST.get(), 1, Ingredient.of(INGOTS_SILVER), 200).unlockedBy("has_silver", has(INGOTS_SILVER)).save(recipe, getId("silver_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(STEEL_DUST.get(), 1, Ingredient.of(INGOTS_STEEL), 200).unlockedBy("has_steel", has(INGOTS_STEEL)).save(recipe, getId("steel_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(TIN_DUST.get(), 1, Ingredient.of(INGOTS_TIN), 200).unlockedBy("has_tin", has(INGOTS_TIN)).save(recipe, getId("tin_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(URANIUM_DUST.get(), 1, Ingredient.of(INGOTS_URANIUM), 200).unlockedBy("has_uranium", has(INGOTS_URANIUM)).save(recipe, getId("uranium_dust_from_compacting"));

        //Machines
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, COAL_GENERATOR.get(), 1).pattern(" X ").pattern("#R#").pattern("IBI").define('X', Tags.Items.DUSTS_REDSTONE).define('#', ItemTags.COALS).define('R', MACHINE_FRAME.get()).define('I', Tags.Items.INGOTS_IRON).define('B', BATTERIES).unlockedBy("has_machine_frame", has(MACHINE_FRAME.get())).save(recipe);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, COMPACTOR.get(), 1).pattern(" X ").pattern("#R#").pattern("IBI").define('X', COMPACTOR_KIT.get()).define('#', Tags.Items.DUSTS_REDSTONE).define('R', MACHINE_FRAME.get()).define('I', INGOTS_ELECTRUM).define('B', BATTERIES).unlockedBy("has_machine_frame", has(MACHINE_FRAME.get())).save(recipe);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CRUSHER.get(), 1).pattern(" X ").pattern("#R#").pattern("IBI").define('X', Tags.Items.DUSTS_REDSTONE).define('#', Items.FLINT).define('R', MACHINE_FRAME.get()).define('I', Tags.Items.INGOTS_COPPER).define('B', BATTERIES).unlockedBy("has_machine_frame", has(MACHINE_FRAME.get())).save(recipe);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ELECTRIC_SMELTER.get(), 1).pattern(" X ").pattern("#R#").pattern("IBI").define('X', Tags.Items.DUSTS_REDSTONE).define('#', GEARS_COPPER).define('R', Items.FURNACE).define('I', Tags.Items.INGOTS_IRON).define('B', BATTERIES).unlockedBy("has_furnace", has(Items.FURNACE)).save(recipe);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, LAVA_GENERATOR.get(), 1).pattern(" X ").pattern("#R#").pattern("IBI").define('X', Items.BUCKET).define('#', Tags.Items.DUSTS_REDSTONE).define('R', MACHINE_FRAME.get()).define('I', Tags.Items.INGOTS_NETHER_BRICK).define('B', BATTERIES).unlockedBy("has_machine_frame", has(MACHINE_FRAME.get())).save(recipe);

        //Items
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MACHINE_FRAME.get(), 1).pattern("X#X").pattern("#R#").pattern("X#X").define('X', INGOTS_TIN).define('#', Tags.Items.GLASS).define('R', GEARS_IRON).unlockedBy("has_tin_ingot", has(INGOTS_TIN)).save(recipe);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BATTERY.get(), 1).pattern(" X ").pattern("#R#").pattern("#R#").define('X', Tags.Items.NUGGETS_GOLD).define('#', INGOTS_TIN).define('R', Tags.Items.DUSTS_REDSTONE).unlockedBy("has_tin_ingot", has(INGOTS_TIN)).save(recipe);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, COMPACTOR_KIT.get(), 1).requires(GEARS_GOLD).requires(PLATES_LEAD).requires(HAMMER.get()).unlockedBy("has_hammer", has(HAMMER.get())).save(recipe);
    }
}
