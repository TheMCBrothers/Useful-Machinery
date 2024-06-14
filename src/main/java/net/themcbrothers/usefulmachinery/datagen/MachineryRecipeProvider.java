package net.themcbrothers.usefulmachinery.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.themcbrothers.usefulmachinery.core.MachineryDataComponentTypes;
import net.themcbrothers.usefulmachinery.datagen.recipe.CompactingRecipeBuilder;
import net.themcbrothers.usefulmachinery.datagen.recipe.CrushingRecipeBuilder;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;
import net.themcbrothers.usefulmachinery.machine.MachineTier;

import java.util.concurrent.CompletableFuture;

import static net.themcbrothers.usefulfoundation.core.FoundationBlocks.*;
import static net.themcbrothers.usefulfoundation.core.FoundationItems.*;
import static net.themcbrothers.usefulfoundation.core.FoundationTags.Items.*;
import static net.themcbrothers.usefulmachinery.UsefulMachinery.rl;
import static net.themcbrothers.usefulmachinery.core.MachineryBlocks.*;
import static net.themcbrothers.usefulmachinery.core.MachineryItems.*;
import static net.themcbrothers.usefulmachinery.core.MachineryTags.Items.BATTERIES;

public class MachineryRecipeProvider extends RecipeProvider {
    public MachineryRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        // Gears
        CompactingRecipeBuilder.compacting(ALUMINUM_GEAR, SizedIngredient.of(INGOTS_ALUMINUM, 4), 200, CompactorMode.GEAR).unlockedBy("has_aluminum", has(INGOTS_ALUMINUM)).save(recipeOutput, rl("aluminum_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(BRONZE_GEAR, SizedIngredient.of(INGOTS_BRONZE, 4), 200, CompactorMode.GEAR).unlockedBy("has_bronze", has(INGOTS_BRONZE)).save(recipeOutput, rl("bronze_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(COPPER_GEAR, SizedIngredient.of(Tags.Items.INGOTS_COPPER, 4), 200, CompactorMode.GEAR).unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER)).save(recipeOutput, rl("copper_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(DIAMOND_GEAR, SizedIngredient.of(Tags.Items.GEMS_DIAMOND, 4), 200, CompactorMode.GEAR).unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND)).save(recipeOutput, rl("diamond_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(ELECTRUM_GEAR, SizedIngredient.of(INGOTS_ELECTRUM, 4), 200, CompactorMode.GEAR).unlockedBy("has_electrum", has(INGOTS_ELECTRUM)).save(recipeOutput, rl("electrum_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(ENDERIUM_GEAR, SizedIngredient.of(INGOTS_ENDERIUM, 4), 200, CompactorMode.GEAR).unlockedBy("has_enderium", has(INGOTS_ENDERIUM)).save(recipeOutput, rl("enderium_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(GOLD_GEAR, SizedIngredient.of(Tags.Items.INGOTS_GOLD, 4), 200, CompactorMode.GEAR).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(recipeOutput, rl("gold_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(INVAR_GEAR, SizedIngredient.of(INGOTS_INVAR, 4), 200, CompactorMode.GEAR).unlockedBy("has_invar", has(INGOTS_INVAR)).save(recipeOutput, rl("invar_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(IRON_GEAR, SizedIngredient.of(Tags.Items.INGOTS_IRON, 4), 200, CompactorMode.GEAR).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).save(recipeOutput, rl("iron_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(LEAD_GEAR, SizedIngredient.of(INGOTS_LEAD, 4), 200, CompactorMode.GEAR).unlockedBy("has_lead", has(INGOTS_LEAD)).save(recipeOutput, rl("lead_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(NICKEL_GEAR, SizedIngredient.of(INGOTS_NICKEL, 4), 200, CompactorMode.GEAR).unlockedBy("has_nickel", has(INGOTS_NICKEL)).save(recipeOutput, rl("nickel_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(PLATINUM_GEAR, SizedIngredient.of(INGOTS_PLATINUM, 4), 200, CompactorMode.GEAR).unlockedBy("has_platinum", has(INGOTS_PLATINUM)).save(recipeOutput, rl("platinum_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(SIGNALUM_GEAR, SizedIngredient.of(INGOTS_SIGNALUM, 4), 200, CompactorMode.GEAR).unlockedBy("has_signalum", has(INGOTS_SIGNALUM)).save(recipeOutput, rl("signalum_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(SILVER_GEAR, SizedIngredient.of(INGOTS_SILVER, 4), 200, CompactorMode.GEAR).unlockedBy("has_silver", has(INGOTS_SILVER)).save(recipeOutput, rl("silver_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(STEEL_GEAR, SizedIngredient.of(INGOTS_STEEL, 4), 200, CompactorMode.GEAR).unlockedBy("has_steel", has(INGOTS_STEEL)).save(recipeOutput, rl("steel_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(TIN_GEAR, SizedIngredient.of(INGOTS_TIN, 4), 200, CompactorMode.GEAR).unlockedBy("has_tin", has(INGOTS_TIN)).save(recipeOutput, rl("tin_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(URANIUM_GEAR, SizedIngredient.of(INGOTS_URANIUM, 4), 200, CompactorMode.GEAR).unlockedBy("has_uranium", has(INGOTS_URANIUM)).save(recipeOutput, rl("uranium_gear_from_compacting"));

        // Storage Blocks
        CompactingRecipeBuilder.compacting(ALUMINUM_BLOCK, SizedIngredient.of(INGOTS_ALUMINUM, 9), 250, CompactorMode.BLOCK).unlockedBy("has_aluminum", has(INGOTS_ALUMINUM)).save(recipeOutput, rl("aluminum_block_from_compacting"));
        CompactingRecipeBuilder.compacting(BRONZE_BLOCK, SizedIngredient.of(INGOTS_BRONZE, 9), 250, CompactorMode.BLOCK).unlockedBy("has_bronze", has(INGOTS_BRONZE)).save(recipeOutput, rl("bronze_block_from_compacting"));
        CompactingRecipeBuilder.compacting(Items.COPPER_BLOCK, SizedIngredient.of(Tags.Items.INGOTS_COPPER, 9), 250, CompactorMode.BLOCK).unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER)).save(recipeOutput, rl("copper_block_from_compacting"));
        CompactingRecipeBuilder.compacting(ELECTRUM_BLOCK, SizedIngredient.of(INGOTS_ELECTRUM, 9), 250, CompactorMode.BLOCK).unlockedBy("has_electrum", has(INGOTS_ELECTRUM)).save(recipeOutput, rl("electrum_block_from_compacting"));
        CompactingRecipeBuilder.compacting(ENDERIUM_BLOCK, SizedIngredient.of(INGOTS_ENDERIUM, 9), 250, CompactorMode.BLOCK).unlockedBy("has_enderium", has(INGOTS_ENDERIUM)).save(recipeOutput, rl("enderium_block_from_compacting"));
        CompactingRecipeBuilder.compacting(Items.GOLD_BLOCK, SizedIngredient.of(Tags.Items.INGOTS_GOLD, 9), 250, CompactorMode.BLOCK).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(recipeOutput, rl("gold_block_from_compacting"));
        CompactingRecipeBuilder.compacting(INVAR_BLOCK, SizedIngredient.of(INGOTS_INVAR, 9), 250, CompactorMode.BLOCK).unlockedBy("has_invar", has(INGOTS_INVAR)).save(recipeOutput, rl("invar_block_from_compacting"));
        CompactingRecipeBuilder.compacting(Items.IRON_BLOCK, SizedIngredient.of(Tags.Items.INGOTS_IRON, 9), 250, CompactorMode.BLOCK).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).save(recipeOutput, rl("iron_block_from_compacting"));
        CompactingRecipeBuilder.compacting(LEAD_BLOCK, SizedIngredient.of(INGOTS_LEAD, 9), 250, CompactorMode.BLOCK).unlockedBy("has_lead", has(INGOTS_LEAD)).save(recipeOutput, rl("lead_block_from_compacting"));
        CompactingRecipeBuilder.compacting(NICKEL_BLOCK, SizedIngredient.of(INGOTS_NICKEL, 9), 250, CompactorMode.BLOCK).unlockedBy("has_nickel", has(INGOTS_NICKEL)).save(recipeOutput, rl("nickel_block_from_compacting"));
        CompactingRecipeBuilder.compacting(PLATINUM_BLOCK, SizedIngredient.of(INGOTS_PLATINUM, 9), 250, CompactorMode.BLOCK).unlockedBy("has_platinum", has(INGOTS_PLATINUM)).save(recipeOutput, rl("platinum_block_from_compacting"));
        CompactingRecipeBuilder.compacting(SIGNALUM_BLOCK, SizedIngredient.of(INGOTS_SIGNALUM, 9), 250, CompactorMode.BLOCK).unlockedBy("has_signalum", has(INGOTS_SIGNALUM)).save(recipeOutput, rl("signalum_block_from_compacting"));
        CompactingRecipeBuilder.compacting(SILVER_BLOCK, SizedIngredient.of(INGOTS_SILVER, 9), 250, CompactorMode.BLOCK).unlockedBy("has_silver", has(INGOTS_SILVER)).save(recipeOutput, rl("silver_block_from_compacting"));
        CompactingRecipeBuilder.compacting(STEEL_BLOCK, SizedIngredient.of(INGOTS_STEEL, 9), 250, CompactorMode.BLOCK).unlockedBy("has_steel", has(INGOTS_STEEL)).save(recipeOutput, rl("steel_block_from_compacting"));
        CompactingRecipeBuilder.compacting(TIN_BLOCK, SizedIngredient.of(INGOTS_TIN, 9), 250, CompactorMode.BLOCK).unlockedBy("has_tin", has(INGOTS_TIN)).save(recipeOutput, rl("tin_block_from_compacting"));
        CompactingRecipeBuilder.compacting(URANIUM_BLOCK, SizedIngredient.of(INGOTS_URANIUM, 9), 250, CompactorMode.BLOCK).unlockedBy("has_uranium", has(INGOTS_URANIUM)).save(recipeOutput, rl("uranium_block_from_compacting"));

        // Plates
        CompactingRecipeBuilder.compacting(ALUMINUM_PLATE, SizedIngredient.of(INGOTS_ALUMINUM, 1), 200, CompactorMode.PLATE).unlockedBy("has_aluminum", has(INGOTS_ALUMINUM)).save(recipeOutput, rl("aluminum_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(BRONZE_PLATE, SizedIngredient.of(INGOTS_BRONZE, 1), 200, CompactorMode.PLATE).unlockedBy("has_bronze", has(INGOTS_BRONZE)).save(recipeOutput, rl("bronze_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(COPPER_PLATE, SizedIngredient.of(Tags.Items.INGOTS_COPPER, 1), 200, CompactorMode.PLATE).unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER)).save(recipeOutput, rl("copper_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(DIAMOND_PLATE, SizedIngredient.of(Tags.Items.GEMS_DIAMOND, 1), 200, CompactorMode.PLATE).unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND)).save(recipeOutput, rl("diamond_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(ELECTRUM_PLATE, SizedIngredient.of(INGOTS_ELECTRUM, 1), 200, CompactorMode.PLATE).unlockedBy("has_electrum", has(INGOTS_ELECTRUM)).save(recipeOutput, rl("electrum_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(ENDERIUM_PLATE, SizedIngredient.of(INGOTS_ENDERIUM, 1), 200, CompactorMode.PLATE).unlockedBy("has_enderium", has(INGOTS_ENDERIUM)).save(recipeOutput, rl("enderium_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(GOLD_PLATE, SizedIngredient.of(Tags.Items.INGOTS_GOLD, 1), 200, CompactorMode.PLATE).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(recipeOutput, rl("gold_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(INVAR_PLATE, SizedIngredient.of(INGOTS_INVAR, 1), 200, CompactorMode.PLATE).unlockedBy("has_invar", has(INGOTS_INVAR)).save(recipeOutput, rl("invar_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(IRON_PLATE, SizedIngredient.of(Tags.Items.INGOTS_IRON, 1), 200, CompactorMode.PLATE).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).save(recipeOutput, rl("iron_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(LEAD_PLATE, SizedIngredient.of(INGOTS_LEAD, 1), 200, CompactorMode.PLATE).unlockedBy("has_lead", has(INGOTS_LEAD)).save(recipeOutput, rl("lead_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(NICKEL_PLATE, SizedIngredient.of(INGOTS_NICKEL, 1), 200, CompactorMode.PLATE).unlockedBy("has_nickel", has(INGOTS_NICKEL)).save(recipeOutput, rl("nickel_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(PLATINUM_PLATE, SizedIngredient.of(INGOTS_PLATINUM, 1), 200, CompactorMode.PLATE).unlockedBy("has_platinum", has(INGOTS_PLATINUM)).save(recipeOutput, rl("platinum_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(SIGNALUM_PLATE, SizedIngredient.of(INGOTS_SIGNALUM, 1), 200, CompactorMode.PLATE).unlockedBy("has_signalum", has(INGOTS_SIGNALUM)).save(recipeOutput, rl("signalum_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(SILVER_PLATE, SizedIngredient.of(INGOTS_SILVER, 1), 200, CompactorMode.PLATE).unlockedBy("has_silver", has(INGOTS_SILVER)).save(recipeOutput, rl("silver_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(STEEL_PLATE, SizedIngredient.of(INGOTS_STEEL, 1), 200, CompactorMode.PLATE).unlockedBy("has_steel", has(INGOTS_STEEL)).save(recipeOutput, rl("steel_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(TIN_PLATE, SizedIngredient.of(INGOTS_TIN, 1), 200, CompactorMode.PLATE).unlockedBy("has_tin", has(INGOTS_TIN)).save(recipeOutput, rl("tin_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(URANIUM_PLATE, SizedIngredient.of(INGOTS_URANIUM, 1), 200, CompactorMode.PLATE).unlockedBy("has_uranium", has(INGOTS_URANIUM)).save(recipeOutput, rl("uranium_plate_from_compacting"));

        // Raw Materials
        Ingredient upgrades = Ingredient.of(EFFICIENCY_UPGRADE, PRECISION_UPGRADE);
        CrushingRecipeBuilder.crushing(RAW_ALUMINUM, 2, Ingredient.of(ORES_ALUMINUM), 200).supportedUpgrades(upgrades).unlockedBy("has_aluminum_ore", has(ORES_ALUMINUM)).save(recipeOutput, rl("raw_aluminum_from_ore"));
        CrushingRecipeBuilder.crushing(Items.RAW_COPPER, 2, Ingredient.of(Tags.Items.ORES_COPPER), 200).supportedUpgrades(upgrades).secondary(Items.RAW_GOLD, 0.05F).unlockedBy("has_copper_ore", has(Tags.Items.ORES_COPPER)).save(recipeOutput, rl("raw_copper_from_ore"));
        CrushingRecipeBuilder.crushing(Items.DIAMOND, 2, Ingredient.of(Tags.Items.ORES_DIAMOND), 200).supportedUpgrades(upgrades).unlockedBy("has_diamond_ore", has(Tags.Items.ORES_DIAMOND)).save(recipeOutput, rl("raw_diamond_from_ore"));
        CrushingRecipeBuilder.crushing(Items.RAW_GOLD, 2, Ingredient.of(Tags.Items.ORES_GOLD), 200).supportedUpgrades(upgrades).secondary(Items.RAW_COPPER, 0.4F).unlockedBy("has_gold_ore", has(Tags.Items.ORES_GOLD)).save(recipeOutput, rl("raw_gold_from_ore"));
        CrushingRecipeBuilder.crushing(Items.RAW_IRON, 2, Ingredient.of(Tags.Items.ORES_IRON), 200).supportedUpgrades(upgrades).secondary(RAW_NICKEL, 0.1F).unlockedBy("has_iron_ore", has(Tags.Items.ORES_IRON)).save(recipeOutput, rl("raw_iron_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_LEAD, 2, Ingredient.of(ORES_LEAD), 200).supportedUpgrades(upgrades).secondary(RAW_SILVER, 0.08F).unlockedBy("has_lead_ore", has(ORES_LEAD)).save(recipeOutput, rl("raw_lead_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_NICKEL, 2, Ingredient.of(ORES_NICKEL), 200).supportedUpgrades(upgrades).unlockedBy("has_nickel_ore", has(ORES_NICKEL)).save(recipeOutput, rl("raw_nickel_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_PLATINUM, 2, Ingredient.of(ORES_PLATINUM), 200).supportedUpgrades(upgrades).unlockedBy("has_platinum_ore", has(ORES_PLATINUM)).save(recipeOutput, rl("raw_platinum_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_SILVER, 2, Ingredient.of(ORES_SILVER), 200).supportedUpgrades(upgrades).secondary(RAW_LEAD, 0.09F).unlockedBy("has_silver_ore", has(ORES_SILVER)).save(recipeOutput, rl("raw_silver_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_TIN, 2, Ingredient.of(ORES_TIN), 200).supportedUpgrades(upgrades).unlockedBy("has_tin_ore", has(ORES_TIN)).save(recipeOutput, rl("raw_tin_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_URANIUM, 2, Ingredient.of(ORES_URANIUM), 200).supportedUpgrades(upgrades).unlockedBy("has_uranium_ore", has(ORES_URANIUM)).save(recipeOutput, rl("raw_uranium_from_ore"));

        // Dusts
        CrushingRecipeBuilder.crushing(ALUMINUM_DUST, 1, Ingredient.of(INGOTS_ALUMINUM), 200).unlockedBy("has_aluminum", has(INGOTS_ALUMINUM)).save(recipeOutput, rl("aluminum_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(BRONZE_DUST, 1, Ingredient.of(INGOTS_BRONZE), 200).unlockedBy("has_bronze", has(INGOTS_BRONZE)).save(recipeOutput, rl("bronze_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(COPPER_DUST, 1, Ingredient.of(Tags.Items.INGOTS_COPPER), 200).unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER)).save(recipeOutput, rl("copper_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(DIAMOND_DUST, 1, Ingredient.of(Tags.Items.GEMS_DIAMOND), 200).unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND)).save(recipeOutput, rl("diamond_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(ELECTRUM_DUST, 1, Ingredient.of(INGOTS_ELECTRUM), 200).unlockedBy("has_electrum", has(INGOTS_ELECTRUM)).save(recipeOutput, rl("electrum_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(ENDERIUM_DUST, 1, Ingredient.of(INGOTS_ENDERIUM), 200).unlockedBy("has_enderium", has(INGOTS_ENDERIUM)).save(recipeOutput, rl("enderium_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(GOLD_DUST, 1, Ingredient.of(Tags.Items.INGOTS_GOLD), 200).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(recipeOutput, rl("gold_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(INVAR_DUST, 1, Ingredient.of(INGOTS_INVAR), 200).unlockedBy("has_invar", has(INGOTS_INVAR)).save(recipeOutput, rl("invar_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(IRON_DUST, 1, Ingredient.of(Tags.Items.INGOTS_IRON), 200).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).save(recipeOutput, rl("iron_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(LEAD_DUST, 1, Ingredient.of(INGOTS_LEAD), 200).unlockedBy("has_lead", has(INGOTS_LEAD)).save(recipeOutput, rl("lead_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(NICKEL_DUST, 1, Ingredient.of(INGOTS_NICKEL), 200).unlockedBy("has_nickel", has(INGOTS_NICKEL)).save(recipeOutput, rl("nickel_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(PLATINUM_DUST, 1, Ingredient.of(INGOTS_PLATINUM), 200).unlockedBy("has_platinum", has(INGOTS_PLATINUM)).save(recipeOutput, rl("platinum_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(SIGNALUM_DUST, 1, Ingredient.of(INGOTS_SIGNALUM), 200).unlockedBy("has_signalum", has(INGOTS_SIGNALUM)).save(recipeOutput, rl("signalum_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(SILVER_DUST, 1, Ingredient.of(INGOTS_SILVER), 200).unlockedBy("has_silver", has(INGOTS_SILVER)).save(recipeOutput, rl("silver_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(STEEL_DUST, 1, Ingredient.of(INGOTS_STEEL), 200).unlockedBy("has_steel", has(INGOTS_STEEL)).save(recipeOutput, rl("steel_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(TIN_DUST, 1, Ingredient.of(INGOTS_TIN), 200).unlockedBy("has_tin", has(INGOTS_TIN)).save(recipeOutput, rl("tin_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(URANIUM_DUST, 1, Ingredient.of(INGOTS_URANIUM), 200).unlockedBy("has_uranium", has(INGOTS_URANIUM)).save(recipeOutput, rl("uranium_dust_from_crushing"));

        // Machines
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, COAL_GENERATOR, 1).pattern(" X ").pattern("#R#").pattern("IBI").define('X', Tags.Items.DUSTS_REDSTONE).define('#', ItemTags.COALS).define('R', MACHINE_FRAME.get()).define('I', Tags.Items.INGOTS_IRON).define('B', BATTERIES).unlockedBy("has_machine_frame", has(MACHINE_FRAME.get())).save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, COMPACTOR, 1).pattern(" X ").pattern("#R#").pattern("IBI").define('X', COMPACTOR_KIT.get()).define('#', Tags.Items.DUSTS_REDSTONE).define('R', MACHINE_FRAME.get()).define('I', INGOTS_ELECTRUM).define('B', BATTERIES).unlockedBy("has_machine_frame", has(MACHINE_FRAME.get())).save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CRUSHER, 1).pattern(" X ").pattern("#R#").pattern("IBI").define('X', Tags.Items.DUSTS_REDSTONE).define('#', Items.FLINT).define('R', MACHINE_FRAME.get()).define('I', Tags.Items.INGOTS_COPPER).define('B', BATTERIES).unlockedBy("has_machine_frame", has(MACHINE_FRAME.get())).save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ELECTRIC_SMELTER, 1).pattern(" X ").pattern("#R#").pattern("IBI").define('X', Tags.Items.DUSTS_REDSTONE).define('#', GEARS_COPPER).define('R', Items.FURNACE).define('I', Tags.Items.INGOTS_IRON).define('B', BATTERIES).unlockedBy("has_furnace", has(Items.FURNACE)).save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, LAVA_GENERATOR, 1).pattern(" X ").pattern("#R#").pattern("IBI").define('X', Items.BUCKET).define('#', Tags.Items.DUSTS_REDSTONE).define('R', MACHINE_FRAME.get()).define('I', Items.NETHER_BRICK).define('B', BATTERIES).unlockedBy("has_machine_frame", has(MACHINE_FRAME.get())).save(recipeOutput);

        // Items
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MACHINE_FRAME, 1)
                .pattern("IBI")
                .pattern("BGB")
                .pattern("IBI")
                .define('I', INGOTS_TIN)
                .define('B', Tags.Items.GLASS_BLOCKS)
                .define('G', GEARS_IRON)
                .unlockedBy("has_tin_ingot", has(INGOTS_TIN))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BATTERY, 1)
                .pattern(" N ")
                .pattern("IRI")
                .pattern("IRI")
                .define('N', Tags.Items.NUGGETS_GOLD)
                .define('I', INGOTS_TIN)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_tin_ingot", has(INGOTS_TIN))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, COMPACTOR_KIT, 1)
                .requires(GEARS_GOLD)
                .requires(PLATES_LEAD)
                .requires(HAMMER.get())
                .unlockedBy("has_hammer", has(HAMMER.get()))
                .save(recipeOutput);

        // Upgrades
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PRECISION_UPGRADE, 2)
                .pattern("IDI")
                .pattern("GPG")
                .pattern("IDI")
                .define('I', INGOTS_NICKEL)
                .define('D', Tags.Items.DUSTS_GLOWSTONE)
                .define('G', Tags.Items.GEMS_AMETHYST)
                .define('P', PLATES_STEEL)
                .unlockedBy("has_amethyst_shard", has(Tags.Items.GEMS_AMETHYST))
                .unlockedBy("has_glowstone_dust", has(Tags.Items.DUSTS_GLOWSTONE))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EFFICIENCY_UPGRADE, 2)
                .pattern("IDI")
                .pattern("GPG")
                .pattern("IDI")
                .define('I', INGOTS_BRONZE)
                .define('D', Tags.Items.DUSTS_REDSTONE)
                .define('G', Items.FLINT)
                .define('P', PLATES_STEEL)
                .unlockedBy("has_flint", has(Items.FLINT))
                .unlockedBy("has_redstone_dust", has(Tags.Items.DUSTS_REDSTONE))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SUSTAINED_UPGRADE, 2)
                .pattern("IDI")
                .pattern("GPG")
                .pattern("IDI")
                .define('I', Tags.Items.INGOTS_COPPER)
                .define('D', Items.BONE_MEAL)
                .define('G', ItemTags.SAPLINGS)
                .define('P', PLATES_STEEL)
                .unlockedBy("has_sapling", has(ItemTags.SAPLINGS))
                .unlockedBy("has_bone_meal", has(Items.BONE_MEAL))
                .save(recipeOutput);

        // Tier Upgrades
        this.tierUpgradeRecipe(recipeOutput, INGOTS_INVAR, GEARS_BRONZE, MachineTier.BASIC);
        this.tierUpgradeRecipe(recipeOutput, INGOTS_ELECTRUM, GEARS_SILVER, MachineTier.REINFORCED);
        this.tierUpgradeRecipe(recipeOutput, INGOTS_SIGNALUM, GEARS_ELECTRUM, MachineTier.FACTORY);
        this.tierUpgradeRecipe(recipeOutput, INGOTS_ENDERIUM, GEARS_SIGNALUM, MachineTier.OVERKILL);
    }

    private void tierUpgradeRecipe(RecipeOutput recipeOutput, TagKey<Item> ingotTag, TagKey<Item> gearTag, MachineTier tier) {
        ItemStack result = new ItemStack(TIER_UPGRADE.get());
        result.set(MachineryDataComponentTypes.TIER, tier);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
                .pattern(" I ")
                .pattern("IGI")
                .pattern("RIR")
                .define('I', ingotTag)
                .define('G', gearTag)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_gear", has(gearTag))
                .save(recipeOutput, rl("tier_upgrade_" + tier.getSerializedName()));
    }
}
