package net.themcbrothers.usefulmachinery.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
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
import static net.themcbrothers.usefulmachinery.UsefulMachinery.id;
import static net.themcbrothers.usefulmachinery.core.MachineryBlocks.*;
import static net.themcbrothers.usefulmachinery.core.MachineryItems.*;
import static net.themcbrothers.usefulmachinery.core.MachineryTags.Items.BATTERIES;

public class MachineryRecipeProvider extends RecipeProvider {
    public MachineryRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    private ResourceKey<Recipe<?>> key(String path) {
        return ResourceKey.create(Registries.RECIPE, id(path));
    }

    @Override
    protected void buildRecipes() {
        // Gears
        CompactingRecipeBuilder.compacting(ALUMINUM_GEAR, new SizedIngredient(this.tag(INGOTS_ALUMINUM), 4), 200, CompactorMode.GEAR).unlockedBy("has_aluminum", has(INGOTS_ALUMINUM)).save(this.output, key("aluminum_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(BRONZE_GEAR, new SizedIngredient(this.tag(INGOTS_BRONZE), 4), 200, CompactorMode.GEAR).unlockedBy("has_bronze", has(INGOTS_BRONZE)).save(this.output, key("bronze_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(COPPER_GEAR, new SizedIngredient(this.tag(Tags.Items.INGOTS_COPPER), 4), 200, CompactorMode.GEAR).unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER)).save(this.output, key("copper_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(DIAMOND_GEAR, new SizedIngredient(this.tag(Tags.Items.GEMS_DIAMOND), 4), 200, CompactorMode.GEAR).unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND)).save(this.output, key("diamond_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(ELECTRUM_GEAR, new SizedIngredient(this.tag(INGOTS_ELECTRUM), 4), 200, CompactorMode.GEAR).unlockedBy("has_electrum", has(INGOTS_ELECTRUM)).save(this.output, key("electrum_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(ENDERIUM_GEAR, new SizedIngredient(this.tag(INGOTS_ENDERIUM), 4), 200, CompactorMode.GEAR).unlockedBy("has_enderium", has(INGOTS_ENDERIUM)).save(this.output, key("enderium_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(GOLD_GEAR, new SizedIngredient(this.tag(Tags.Items.INGOTS_GOLD), 4), 200, CompactorMode.GEAR).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(this.output, key("gold_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(INVAR_GEAR, new SizedIngredient(this.tag(INGOTS_INVAR), 4), 200, CompactorMode.GEAR).unlockedBy("has_invar", has(INGOTS_INVAR)).save(this.output, key("invar_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(IRON_GEAR, new SizedIngredient(this.tag(Tags.Items.INGOTS_IRON), 4), 200, CompactorMode.GEAR).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).save(this.output, key("iron_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(LEAD_GEAR, new SizedIngredient(this.tag(INGOTS_LEAD), 4), 200, CompactorMode.GEAR).unlockedBy("has_lead", has(INGOTS_LEAD)).save(this.output, key("lead_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(NICKEL_GEAR, new SizedIngredient(this.tag(INGOTS_NICKEL), 4), 200, CompactorMode.GEAR).unlockedBy("has_nickel", has(INGOTS_NICKEL)).save(this.output, key("nickel_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(PLATINUM_GEAR, new SizedIngredient(this.tag(INGOTS_PLATINUM), 4), 200, CompactorMode.GEAR).unlockedBy("has_platinum", has(INGOTS_PLATINUM)).save(this.output, key("platinum_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(SIGNALUM_GEAR, new SizedIngredient(this.tag(INGOTS_SIGNALUM), 4), 200, CompactorMode.GEAR).unlockedBy("has_signalum", has(INGOTS_SIGNALUM)).save(this.output, key("signalum_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(SILVER_GEAR, new SizedIngredient(this.tag(INGOTS_SILVER), 4), 200, CompactorMode.GEAR).unlockedBy("has_silver", has(INGOTS_SILVER)).save(this.output, key("silver_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(STEEL_GEAR, new SizedIngredient(this.tag(INGOTS_STEEL), 4), 200, CompactorMode.GEAR).unlockedBy("has_steel", has(INGOTS_STEEL)).save(this.output, key("steel_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(TIN_GEAR, new SizedIngredient(this.tag(INGOTS_TIN), 4), 200, CompactorMode.GEAR).unlockedBy("has_tin", has(INGOTS_TIN)).save(this.output, key("tin_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(URANIUM_GEAR, new SizedIngredient(this.tag(INGOTS_URANIUM), 4), 200, CompactorMode.GEAR).unlockedBy("has_uranium", has(INGOTS_URANIUM)).save(this.output, key("uranium_gear_from_compacting"));

        // Storage Blocks
        CompactingRecipeBuilder.compacting(ALUMINUM_BLOCK, new SizedIngredient(this.tag(INGOTS_ALUMINUM), 9), 250, CompactorMode.BLOCK).unlockedBy("has_aluminum", has(INGOTS_ALUMINUM)).save(this.output, key("aluminum_block_from_compacting"));
        CompactingRecipeBuilder.compacting(BRONZE_BLOCK, new SizedIngredient(this.tag(INGOTS_BRONZE), 9), 250, CompactorMode.BLOCK).unlockedBy("has_bronze", has(INGOTS_BRONZE)).save(this.output, key("bronze_block_from_compacting"));
        CompactingRecipeBuilder.compacting(Items.COPPER_BLOCK, new SizedIngredient(this.tag(Tags.Items.INGOTS_COPPER), 9), 250, CompactorMode.BLOCK).unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER)).save(this.output, key("copper_block_from_compacting"));
        CompactingRecipeBuilder.compacting(ELECTRUM_BLOCK, new SizedIngredient(this.tag(INGOTS_ELECTRUM), 9), 250, CompactorMode.BLOCK).unlockedBy("has_electrum", has(INGOTS_ELECTRUM)).save(this.output, key("electrum_block_from_compacting"));
        CompactingRecipeBuilder.compacting(ENDERIUM_BLOCK, new SizedIngredient(this.tag(INGOTS_ENDERIUM), 9), 250, CompactorMode.BLOCK).unlockedBy("has_enderium", has(INGOTS_ENDERIUM)).save(this.output, key("enderium_block_from_compacting"));
        CompactingRecipeBuilder.compacting(Items.GOLD_BLOCK, new SizedIngredient(this.tag(Tags.Items.INGOTS_GOLD), 9), 250, CompactorMode.BLOCK).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(this.output, key("gold_block_from_compacting"));
        CompactingRecipeBuilder.compacting(INVAR_BLOCK, new SizedIngredient(this.tag(INGOTS_INVAR), 9), 250, CompactorMode.BLOCK).unlockedBy("has_invar", has(INGOTS_INVAR)).save(this.output, key("invar_block_from_compacting"));
        CompactingRecipeBuilder.compacting(Items.IRON_BLOCK, new SizedIngredient(this.tag(Tags.Items.INGOTS_IRON), 9), 250, CompactorMode.BLOCK).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).save(this.output, key("iron_block_from_compacting"));
        CompactingRecipeBuilder.compacting(LEAD_BLOCK, new SizedIngredient(this.tag(INGOTS_LEAD), 9), 250, CompactorMode.BLOCK).unlockedBy("has_lead", has(INGOTS_LEAD)).save(this.output, key("lead_block_from_compacting"));
        CompactingRecipeBuilder.compacting(NICKEL_BLOCK, new SizedIngredient(this.tag(INGOTS_NICKEL), 9), 250, CompactorMode.BLOCK).unlockedBy("has_nickel", has(INGOTS_NICKEL)).save(this.output, key("nickel_block_from_compacting"));
        CompactingRecipeBuilder.compacting(PLATINUM_BLOCK, new SizedIngredient(this.tag(INGOTS_PLATINUM), 9), 250, CompactorMode.BLOCK).unlockedBy("has_platinum", has(INGOTS_PLATINUM)).save(this.output, key("platinum_block_from_compacting"));
        CompactingRecipeBuilder.compacting(SIGNALUM_BLOCK, new SizedIngredient(this.tag(INGOTS_SIGNALUM), 9), 250, CompactorMode.BLOCK).unlockedBy("has_signalum", has(INGOTS_SIGNALUM)).save(this.output, key("signalum_block_from_compacting"));
        CompactingRecipeBuilder.compacting(SILVER_BLOCK, new SizedIngredient(this.tag(INGOTS_SILVER), 9), 250, CompactorMode.BLOCK).unlockedBy("has_silver", has(INGOTS_SILVER)).save(this.output, key("silver_block_from_compacting"));
        CompactingRecipeBuilder.compacting(STEEL_BLOCK, new SizedIngredient(this.tag(INGOTS_STEEL), 9), 250, CompactorMode.BLOCK).unlockedBy("has_steel", has(INGOTS_STEEL)).save(this.output, key("steel_block_from_compacting"));
        CompactingRecipeBuilder.compacting(TIN_BLOCK, new SizedIngredient(this.tag(INGOTS_TIN), 9), 250, CompactorMode.BLOCK).unlockedBy("has_tin", has(INGOTS_TIN)).save(this.output, key("tin_block_from_compacting"));
        CompactingRecipeBuilder.compacting(URANIUM_BLOCK, new SizedIngredient(this.tag(INGOTS_URANIUM), 9), 250, CompactorMode.BLOCK).unlockedBy("has_uranium", has(INGOTS_URANIUM)).save(this.output, key("uranium_block_from_compacting"));

        // Plates
        CompactingRecipeBuilder.compacting(ALUMINUM_PLATE, new SizedIngredient(this.tag(INGOTS_ALUMINUM), 1), 200, CompactorMode.PLATE).unlockedBy("has_aluminum", has(INGOTS_ALUMINUM)).save(this.output, key("aluminum_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(BRONZE_PLATE, new SizedIngredient(this.tag(INGOTS_BRONZE), 1), 200, CompactorMode.PLATE).unlockedBy("has_bronze", has(INGOTS_BRONZE)).save(this.output, key("bronze_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(COPPER_PLATE, new SizedIngredient(this.tag(Tags.Items.INGOTS_COPPER), 1), 200, CompactorMode.PLATE).unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER)).save(this.output, key("copper_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(DIAMOND_PLATE, new SizedIngredient(this.tag(Tags.Items.GEMS_DIAMOND), 1), 200, CompactorMode.PLATE).unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND)).save(this.output, key("diamond_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(ELECTRUM_PLATE, new SizedIngredient(this.tag(INGOTS_ELECTRUM), 1), 200, CompactorMode.PLATE).unlockedBy("has_electrum", has(INGOTS_ELECTRUM)).save(this.output, key("electrum_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(ENDERIUM_PLATE, new SizedIngredient(this.tag(INGOTS_ENDERIUM), 1), 200, CompactorMode.PLATE).unlockedBy("has_enderium", has(INGOTS_ENDERIUM)).save(this.output, key("enderium_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(GOLD_PLATE, new SizedIngredient(this.tag(Tags.Items.INGOTS_GOLD), 1), 200, CompactorMode.PLATE).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(this.output, key("gold_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(INVAR_PLATE, new SizedIngredient(this.tag(INGOTS_INVAR), 1), 200, CompactorMode.PLATE).unlockedBy("has_invar", has(INGOTS_INVAR)).save(this.output, key("invar_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(IRON_PLATE, new SizedIngredient(this.tag(Tags.Items.INGOTS_IRON), 1), 200, CompactorMode.PLATE).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).save(this.output, key("iron_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(LEAD_PLATE, new SizedIngredient(this.tag(INGOTS_LEAD), 1), 200, CompactorMode.PLATE).unlockedBy("has_lead", has(INGOTS_LEAD)).save(this.output, key("lead_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(NICKEL_PLATE, new SizedIngredient(this.tag(INGOTS_NICKEL), 1), 200, CompactorMode.PLATE).unlockedBy("has_nickel", has(INGOTS_NICKEL)).save(this.output, key("nickel_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(PLATINUM_PLATE, new SizedIngredient(this.tag(INGOTS_PLATINUM), 1), 200, CompactorMode.PLATE).unlockedBy("has_platinum", has(INGOTS_PLATINUM)).save(this.output, key("platinum_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(SIGNALUM_PLATE, new SizedIngredient(this.tag(INGOTS_SIGNALUM), 1), 200, CompactorMode.PLATE).unlockedBy("has_signalum", has(INGOTS_SIGNALUM)).save(this.output, key("signalum_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(SILVER_PLATE, new SizedIngredient(this.tag(INGOTS_SILVER), 1), 200, CompactorMode.PLATE).unlockedBy("has_silver", has(INGOTS_SILVER)).save(this.output, key("silver_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(STEEL_PLATE, new SizedIngredient(this.tag(INGOTS_STEEL), 1), 200, CompactorMode.PLATE).unlockedBy("has_steel", has(INGOTS_STEEL)).save(this.output, key("steel_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(TIN_PLATE, new SizedIngredient(this.tag(INGOTS_TIN), 1), 200, CompactorMode.PLATE).unlockedBy("has_tin", has(INGOTS_TIN)).save(this.output, key("tin_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(URANIUM_PLATE, new SizedIngredient(this.tag(INGOTS_URANIUM), 1), 200, CompactorMode.PLATE).unlockedBy("has_uranium", has(INGOTS_URANIUM)).save(this.output, key("uranium_plate_from_compacting"));

        // Dusts from ores
        Ingredient upgrades = Ingredient.of(EFFICIENCY_UPGRADE, PRECISION_UPGRADE);
        CrushingRecipeBuilder.crushing(ALUMINUM_DUST, 3, this.tag(ORES_ALUMINUM), 200).supportedUpgrades(upgrades).unlockedBy("has_aluminum_ore", has(ORES_ALUMINUM)).save(this.output, key("aluminum_dust_from_ore"));
        CrushingRecipeBuilder.crushing(COPPER_DUST, 3, this.tag(Tags.Items.ORES_COPPER), 200).supportedUpgrades(upgrades).secondary(GOLD_DUST, 0.1F).unlockedBy("has_copper_ore", has(Tags.Items.ORES_COPPER)).save(this.output, key("copper_dust_from_ore"));
        CrushingRecipeBuilder.crushing(DIAMOND_DUST, 3, this.tag(Tags.Items.ORES_DIAMOND), 200).supportedUpgrades(upgrades).unlockedBy("has_diamond_ore", has(Tags.Items.ORES_DIAMOND)).save(this.output, key("diamond_dust_from_ore"));
        CrushingRecipeBuilder.crushing(GOLD_DUST, 3, this.tag(Tags.Items.ORES_GOLD), 200).supportedUpgrades(upgrades).secondary(COPPER_DUST, 0.8F).unlockedBy("has_gold_ore", has(Tags.Items.ORES_GOLD)).save(this.output, key("gold_dust_from_ore"));
        CrushingRecipeBuilder.crushing(IRON_DUST, 3, this.tag(Tags.Items.ORES_IRON), 200).supportedUpgrades(upgrades).secondary(NICKEL_DUST, 0.2F).unlockedBy("has_iron_ore", has(Tags.Items.ORES_IRON)).save(this.output, key("iron_dust_from_ore"));
        CrushingRecipeBuilder.crushing(LEAD_DUST, 3, this.tag(ORES_LEAD), 200).supportedUpgrades(upgrades).secondary(SILVER_DUST, 0.16F).unlockedBy("has_lead_ore", has(ORES_LEAD)).save(this.output, key("lead_dust_from_ore"));
        CrushingRecipeBuilder.crushing(NICKEL_DUST, 3, this.tag(ORES_NICKEL), 200).supportedUpgrades(upgrades).unlockedBy("has_nickel_ore", has(ORES_NICKEL)).save(this.output, key("nickel_dust_from_ore"));
        CrushingRecipeBuilder.crushing(PLATINUM_DUST, 3, this.tag(ORES_PLATINUM), 200).supportedUpgrades(upgrades).unlockedBy("has_platinum_ore", has(ORES_PLATINUM)).save(this.output, key("platinum_dust_from_ore"));
        CrushingRecipeBuilder.crushing(SILVER_DUST, 3, this.tag(ORES_SILVER), 200).supportedUpgrades(upgrades).secondary(LEAD_DUST, 0.18F).unlockedBy("has_silver_ore", has(ORES_SILVER)).save(this.output, key("silver_dust_from_ore"));
        CrushingRecipeBuilder.crushing(TIN_DUST, 3, this.tag(ORES_TIN), 200).supportedUpgrades(upgrades).unlockedBy("has_tin_ore", has(ORES_TIN)).save(this.output, key("tin_dust_from_ore"));
        CrushingRecipeBuilder.crushing(URANIUM_DUST, 3, this.tag(ORES_URANIUM), 200).supportedUpgrades(upgrades).unlockedBy("has_uranium_ore", has(ORES_URANIUM)).save(this.output, key("uranium_dust_from_ore"));

        // Dusts from raw materials
        CrushingRecipeBuilder.crushing(ALUMINUM_DUST, 2, this.tag(RAW_MATERIALS_ALUMINUM), 200).supportedUpgrades(upgrades).unlockedBy("has_aluminum_ore", has(ORES_ALUMINUM)).save(this.output, key("aluminum_dust_from_raw_material"));
        CrushingRecipeBuilder.crushing(COPPER_DUST, 2, this.tag(Tags.Items.RAW_MATERIALS_COPPER), 200).supportedUpgrades(upgrades).secondary(GOLD_DUST, 0.05F).unlockedBy("has_copper_ore", has(Tags.Items.ORES_COPPER)).save(this.output, key("copper_dust_from_raw_material"));
        CrushingRecipeBuilder.crushing(GOLD_DUST, 2, this.tag(Tags.Items.RAW_MATERIALS_GOLD), 200).supportedUpgrades(upgrades).secondary(COPPER_DUST, 0.4F).unlockedBy("has_gold_ore", has(Tags.Items.ORES_GOLD)).save(this.output, key("gold_dust_from_raw_material"));
        CrushingRecipeBuilder.crushing(IRON_DUST, 2, this.tag(Tags.Items.RAW_MATERIALS_IRON), 200).supportedUpgrades(upgrades).secondary(NICKEL_DUST, 0.1F).unlockedBy("has_iron_ore", has(Tags.Items.ORES_IRON)).save(this.output, key("iron_dust_from_raw_material"));
        CrushingRecipeBuilder.crushing(LEAD_DUST, 2, this.tag(RAW_MATERIALS_LEAD), 200).supportedUpgrades(upgrades).secondary(SILVER_DUST, 0.08F).unlockedBy("has_lead_ore", has(ORES_LEAD)).save(this.output, key("lead_dust_from_raw_material"));
        CrushingRecipeBuilder.crushing(NICKEL_DUST, 2, this.tag(RAW_MATERIALS_NICKEL), 200).supportedUpgrades(upgrades).unlockedBy("has_nickel_ore", has(ORES_NICKEL)).save(this.output, key("nickel_dust_from_raw_material"));
        CrushingRecipeBuilder.crushing(PLATINUM_DUST, 2, this.tag(RAW_MATERIALS_PLATINUM), 200).supportedUpgrades(upgrades).unlockedBy("has_platinum_ore", has(ORES_PLATINUM)).save(this.output, key("platinum_dust_from_raw_material"));
        CrushingRecipeBuilder.crushing(SILVER_DUST, 2, this.tag(RAW_MATERIALS_SILVER), 200).supportedUpgrades(upgrades).secondary(LEAD_DUST, 0.09F).unlockedBy("has_silver_ore", has(ORES_SILVER)).save(this.output, key("silver_dust_from_raw_material"));
        CrushingRecipeBuilder.crushing(TIN_DUST, 2, this.tag(RAW_MATERIALS_TIN), 200).supportedUpgrades(upgrades).unlockedBy("has_tin_ore", has(ORES_TIN)).save(this.output, key("tin_dust_from_raw_material"));
        CrushingRecipeBuilder.crushing(URANIUM_DUST, 2, this.tag(RAW_MATERIALS_URANIUM), 200).supportedUpgrades(upgrades).unlockedBy("has_uranium_ore", has(ORES_URANIUM)).save(this.output, key("uranium_dust_from_raw_material"));

        // Dusts from ingots
        CrushingRecipeBuilder.crushing(ALUMINUM_DUST, 1, this.tag(INGOTS_ALUMINUM), 200).unlockedBy("has_aluminum", has(INGOTS_ALUMINUM)).save(this.output, key("aluminum_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(BRONZE_DUST, 1, this.tag(INGOTS_BRONZE), 200).unlockedBy("has_bronze", has(INGOTS_BRONZE)).save(this.output, key("bronze_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(COPPER_DUST, 1, this.tag(Tags.Items.INGOTS_COPPER), 200).unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER)).save(this.output, key("copper_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(DIAMOND_DUST, 1, this.tag(Tags.Items.GEMS_DIAMOND), 200).unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND)).save(this.output, key("diamond_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(ELECTRUM_DUST, 1, this.tag(INGOTS_ELECTRUM), 200).unlockedBy("has_electrum", has(INGOTS_ELECTRUM)).save(this.output, key("electrum_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(ENDERIUM_DUST, 1, this.tag(INGOTS_ENDERIUM), 200).unlockedBy("has_enderium", has(INGOTS_ENDERIUM)).save(this.output, key("enderium_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(GOLD_DUST, 1, this.tag(Tags.Items.INGOTS_GOLD), 200).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(this.output, key("gold_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(INVAR_DUST, 1, this.tag(INGOTS_INVAR), 200).unlockedBy("has_invar", has(INGOTS_INVAR)).save(this.output, key("invar_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(IRON_DUST, 1, this.tag(Tags.Items.INGOTS_IRON), 200).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).save(this.output, key("iron_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(LEAD_DUST, 1, this.tag(INGOTS_LEAD), 200).unlockedBy("has_lead", has(INGOTS_LEAD)).save(this.output, key("lead_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(NICKEL_DUST, 1, this.tag(INGOTS_NICKEL), 200).unlockedBy("has_nickel", has(INGOTS_NICKEL)).save(this.output, key("nickel_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(PLATINUM_DUST, 1, this.tag(INGOTS_PLATINUM), 200).unlockedBy("has_platinum", has(INGOTS_PLATINUM)).save(this.output, key("platinum_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(SIGNALUM_DUST, 1, this.tag(INGOTS_SIGNALUM), 200).unlockedBy("has_signalum", has(INGOTS_SIGNALUM)).save(this.output, key("signalum_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(SILVER_DUST, 1, this.tag(INGOTS_SILVER), 200).unlockedBy("has_silver", has(INGOTS_SILVER)).save(this.output, key("silver_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(STEEL_DUST, 1, this.tag(INGOTS_STEEL), 200).unlockedBy("has_steel", has(INGOTS_STEEL)).save(this.output, key("steel_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(TIN_DUST, 1, this.tag(INGOTS_TIN), 200).unlockedBy("has_tin", has(INGOTS_TIN)).save(this.output, key("tin_dust_from_crushing"));
        CrushingRecipeBuilder.crushing(URANIUM_DUST, 1, this.tag(INGOTS_URANIUM), 200).unlockedBy("has_uranium", has(INGOTS_URANIUM)).save(this.output, key("uranium_dust_from_crushing"));

        // Machines
        this.shaped(RecipeCategory.MISC, COAL_GENERATOR, 1).pattern(" X ").pattern("#R#").pattern("IBI").define('X', Tags.Items.DUSTS_REDSTONE).define('#', ItemTags.COALS).define('R', MACHINE_FRAME.get()).define('I', Tags.Items.INGOTS_IRON).define('B', BATTERIES).unlockedBy("has_machine_frame", has(MACHINE_FRAME.get())).save(this.output);
        this.shaped(RecipeCategory.MISC, COMPACTOR, 1).pattern(" X ").pattern("#R#").pattern("IBI").define('X', COMPACTOR_KIT.get()).define('#', Tags.Items.DUSTS_REDSTONE).define('R', MACHINE_FRAME.get()).define('I', INGOTS_ELECTRUM).define('B', BATTERIES).unlockedBy("has_machine_frame", has(MACHINE_FRAME.get())).save(this.output);
        this.shaped(RecipeCategory.MISC, CRUSHER, 1).pattern(" X ").pattern("#R#").pattern("IBI").define('X', Tags.Items.DUSTS_REDSTONE).define('#', Items.FLINT).define('R', MACHINE_FRAME.get()).define('I', Tags.Items.INGOTS_COPPER).define('B', BATTERIES).unlockedBy("has_machine_frame", has(MACHINE_FRAME.get())).save(this.output);
        this.shaped(RecipeCategory.MISC, ELECTRIC_SMELTER, 1).pattern(" X ").pattern("#R#").pattern("IBI").define('X', Tags.Items.DUSTS_REDSTONE).define('#', GEARS_COPPER).define('R', Items.FURNACE).define('I', Tags.Items.INGOTS_IRON).define('B', BATTERIES).unlockedBy("has_furnace", has(Items.FURNACE)).save(this.output);
        this.shaped(RecipeCategory.MISC, LAVA_GENERATOR, 1).pattern(" X ").pattern("#R#").pattern("IBI").define('X', Items.BUCKET).define('#', Tags.Items.DUSTS_REDSTONE).define('R', MACHINE_FRAME.get()).define('I', Items.NETHER_BRICK).define('B', BATTERIES).unlockedBy("has_machine_frame", has(MACHINE_FRAME.get())).save(this.output);

        // Items
        this.shaped(RecipeCategory.MISC, MACHINE_FRAME, 1)
                .pattern("IBI")
                .pattern("BGB")
                .pattern("IBI")
                .define('I', INGOTS_TIN)
                .define('B', Tags.Items.GLASS_BLOCKS)
                .define('G', GEARS_IRON)
                .unlockedBy("has_tin_ingot", has(INGOTS_TIN))
                .save(this.output);

        this.shaped(RecipeCategory.MISC, BATTERY, 1)
                .pattern(" N ")
                .pattern("IRI")
                .pattern("IRI")
                .define('N', Tags.Items.NUGGETS_GOLD)
                .define('I', INGOTS_TIN)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_tin_ingot", has(INGOTS_TIN))
                .save(this.output);

        this.shapeless(RecipeCategory.MISC, COMPACTOR_KIT, 1)
                .requires(GEARS_GOLD)
                .requires(PLATES_LEAD)
                .requires(HAMMER.get())
                .unlockedBy("has_hammer", has(HAMMER.get()))
                .save(this.output);

        // Upgrades
        this.shaped(RecipeCategory.MISC, PRECISION_UPGRADE, 2)
                .pattern("IDI")
                .pattern("GPG")
                .pattern("IDI")
                .define('I', INGOTS_NICKEL)
                .define('D', Tags.Items.DUSTS_GLOWSTONE)
                .define('G', Tags.Items.GEMS_AMETHYST)
                .define('P', PLATES_STEEL)
                .unlockedBy("has_amethyst_shard", has(Tags.Items.GEMS_AMETHYST))
                .unlockedBy("has_glowstone_dust", has(Tags.Items.DUSTS_GLOWSTONE))
                .save(this.output);

        this.shaped(RecipeCategory.MISC, EFFICIENCY_UPGRADE, 2)
                .pattern("IDI")
                .pattern("GPG")
                .pattern("IDI")
                .define('I', INGOTS_BRONZE)
                .define('D', Tags.Items.DUSTS_REDSTONE)
                .define('G', Items.FLINT)
                .define('P', PLATES_STEEL)
                .unlockedBy("has_flint", has(Items.FLINT))
                .unlockedBy("has_redstone_dust", has(Tags.Items.DUSTS_REDSTONE))
                .save(this.output);

        this.shaped(RecipeCategory.MISC, SUSTAINED_UPGRADE, 2)
                .pattern("IDI")
                .pattern("GPG")
                .pattern("IDI")
                .define('I', Tags.Items.INGOTS_COPPER)
                .define('D', Items.BONE_MEAL)
                .define('G', ItemTags.SAPLINGS)
                .define('P', PLATES_STEEL)
                .unlockedBy("has_sapling", has(ItemTags.SAPLINGS))
                .unlockedBy("has_bone_meal", has(Items.BONE_MEAL))
                .save(this.output);

        // Tier Upgrades
        this.tierUpgradeRecipe(this.output, INGOTS_INVAR, GEARS_BRONZE, MachineTier.BASIC);
        this.tierUpgradeRecipe(this.output, INGOTS_ELECTRUM, GEARS_SILVER, MachineTier.REINFORCED);
        this.tierUpgradeRecipe(this.output, INGOTS_SIGNALUM, GEARS_ELECTRUM, MachineTier.FACTORY);
        this.tierUpgradeRecipe(this.output, INGOTS_ENDERIUM, GEARS_SIGNALUM, MachineTier.OVERKILL);
    }

    private void tierUpgradeRecipe(RecipeOutput output, TagKey<Item> ingotTag, TagKey<Item> gearTag, MachineTier tier) {
        ItemStackTemplate stackTemplate = new ItemStackTemplate(TIER_UPGRADE,
                DataComponentPatch.builder()
                        .set(MachineryDataComponentTypes.TIER.get(), tier)
                        .build());

        this.shaped(RecipeCategory.MISC, stackTemplate)
                .pattern(" I ")
                .pattern("IGI")
                .pattern("RIR")
                .define('I', ingotTag)
                .define('G', gearTag)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_gear", has(gearTag))
                .save(output, key("tier_upgrade_" + tier.getSerializedName()));
    }

    static final class Runner extends RecipeProvider.Runner {
        Runner(CompletableFuture<HolderLookup.Provider> registries, PackOutput packOutput) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new MachineryRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Useful Foundation Recipes";
        }
    }
}
