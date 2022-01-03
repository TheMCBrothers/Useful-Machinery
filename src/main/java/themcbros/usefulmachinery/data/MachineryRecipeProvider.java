package themcbros.usefulmachinery.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import themcbros.usefulmachinery.data.recipes.CompactingRecipeBuilder;
import themcbros.usefulmachinery.data.recipes.CrushingRecipeBuilder;
import themcbros.usefulmachinery.machine.CompactorMode;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

import static themcbros.usefulfoundation.FoundationTags.Items.*;
import static themcbros.usefulfoundation.init.FoundationItems.*;
import static themcbros.usefulmachinery.UsefulMachinery.getId;

public class MachineryRecipeProvider extends RecipeProvider {
    public MachineryRecipeProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void buildCraftingRecipes(@Nonnull Consumer<FinishedRecipe> recipe) {
        //Gears
        CompactingRecipeBuilder.compacting(BRONZE_GEAR, Ingredient.of(INGOTS_BRONZE), 4, 200, CompactorMode.GEAR).unlockedBy("has_bronze", has(INGOTS_BRONZE)).save(recipe, getId("bronze_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(COPPER_GEAR, Ingredient.of(INGOTS_COPPER), 4, 200, CompactorMode.GEAR).unlockedBy("has_copper", has(INGOTS_COPPER)).save(recipe, getId("copper_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(ELECTRUM_GEAR, Ingredient.of(INGOTS_ELECTRUM), 4, 200, CompactorMode.GEAR).unlockedBy("has_electrum", has(INGOTS_ELECTRUM)).save(recipe, getId("electrum_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(GOLD_GEAR, Ingredient.of(Tags.Items.INGOTS_GOLD), 4, 200, CompactorMode.GEAR).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(recipe, getId("gold_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(INVAR_GEAR, Ingredient.of(INGOTS_INVAR), 4, 200, CompactorMode.GEAR).unlockedBy("has_invar", has(INGOTS_INVAR)).save(recipe, getId("invar_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(IRON_GEAR, Ingredient.of(Tags.Items.INGOTS_IRON), 4, 200, CompactorMode.GEAR).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).save(recipe, getId("iron_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(LEAD_GEAR, Ingredient.of(INGOTS_LEAD), 4, 200, CompactorMode.GEAR).unlockedBy("has_lead", has(INGOTS_LEAD)).save(recipe, getId("lead_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(NICKEL_GEAR, Ingredient.of(INGOTS_NICKEL), 4, 200, CompactorMode.GEAR).unlockedBy("has_nickel", has(INGOTS_NICKEL)).save(recipe, getId("nickel_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(SILVER_GEAR, Ingredient.of(INGOTS_SILVER), 4, 200, CompactorMode.GEAR).unlockedBy("has_silver", has(INGOTS_SILVER)).save(recipe, getId("silver_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(TIN_GEAR, Ingredient.of(INGOTS_TIN), 4, 200, CompactorMode.GEAR).unlockedBy("has_tin", has(INGOTS_TIN)).save(recipe, getId("tin_gear_from_compacting"));
        CompactingRecipeBuilder.compacting(URANIUM_GEAR, Ingredient.of(INGOTS_URANIUM), 4, 200, CompactorMode.GEAR).unlockedBy("has_uranium", has(INGOTS_URANIUM)).save(recipe, getId("uranium_gear_from_compacting"));

        //Storage Blocks
        CompactingRecipeBuilder.compacting(BRONZE_BLOCK, Ingredient.of(INGOTS_BRONZE), 9, 250, CompactorMode.BLOCK).unlockedBy("has_bronze", has(INGOTS_BRONZE)).save(recipe, getId("bronze_block_from_compacting"));
        CompactingRecipeBuilder.compacting(Items.COPPER_BLOCK, Ingredient.of(INGOTS_COPPER), 9, 250, CompactorMode.BLOCK).unlockedBy("has_copper", has(INGOTS_COPPER)).save(recipe, getId("copper_block_from_compacting"));
        CompactingRecipeBuilder.compacting(ELECTRUM_BLOCK, Ingredient.of(INGOTS_ELECTRUM), 9, 250, CompactorMode.BLOCK).unlockedBy("has_electrum", has(INGOTS_ELECTRUM)).save(recipe, getId("electrum_block_from_compacting"));
        CompactingRecipeBuilder.compacting(Items.GOLD_BLOCK, Ingredient.of(Tags.Items.INGOTS_GOLD), 9, 250, CompactorMode.BLOCK).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(recipe, getId("gold_block_from_compacting"));
        CompactingRecipeBuilder.compacting(INVAR_BLOCK, Ingredient.of(INGOTS_INVAR), 9, 250, CompactorMode.BLOCK).unlockedBy("has_invar", has(INGOTS_INVAR)).save(recipe, getId("invar_block_from_compacting"));
        CompactingRecipeBuilder.compacting(Items.IRON_BLOCK, Ingredient.of(Tags.Items.INGOTS_IRON), 9, 250, CompactorMode.BLOCK).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).save(recipe, getId("iron_block_from_compacting"));
        CompactingRecipeBuilder.compacting(LEAD_BLOCK, Ingredient.of(INGOTS_LEAD), 9, 250, CompactorMode.BLOCK).unlockedBy("has_lead", has(INGOTS_LEAD)).save(recipe, getId("lead_block_from_compacting"));
        CompactingRecipeBuilder.compacting(NICKEL_BLOCK, Ingredient.of(INGOTS_NICKEL), 9, 250, CompactorMode.BLOCK).unlockedBy("has_nickel", has(INGOTS_NICKEL)).save(recipe, getId("nickel_block_from_compacting"));
        CompactingRecipeBuilder.compacting(SILVER_BLOCK, Ingredient.of(INGOTS_SILVER), 9, 250, CompactorMode.BLOCK).unlockedBy("has_silver", has(INGOTS_SILVER)).save(recipe, getId("silver_block_from_compacting"));
        CompactingRecipeBuilder.compacting(TIN_BLOCK, Ingredient.of(INGOTS_TIN), 9, 250, CompactorMode.BLOCK).unlockedBy("has_tin", has(INGOTS_TIN)).save(recipe, getId("tin_block_from_compacting"));
        CompactingRecipeBuilder.compacting(URANIUM_BLOCK, Ingredient.of(INGOTS_URANIUM), 9, 250, CompactorMode.BLOCK).unlockedBy("has_uranium", has(INGOTS_URANIUM)).save(recipe, getId("uranium_block_from_compacting"));

        //Plates
        CompactingRecipeBuilder.compacting(BRONZE_PLATE, Ingredient.of(INGOTS_BRONZE), 1, 200, CompactorMode.PLATE).unlockedBy("has_bronze", has(INGOTS_BRONZE)).save(recipe, getId("bronze_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(COPPER_PLATE, Ingredient.of(INGOTS_COPPER), 1, 200, CompactorMode.PLATE).unlockedBy("has_copper", has(INGOTS_COPPER)).save(recipe, getId("copper_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(ELECTRUM_PLATE, Ingredient.of(INGOTS_ELECTRUM), 1, 200, CompactorMode.PLATE).unlockedBy("has_electrum", has(INGOTS_ELECTRUM)).save(recipe, getId("electrum_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(GOLD_PLATE, Ingredient.of(Tags.Items.INGOTS_GOLD), 1, 200, CompactorMode.PLATE).unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(recipe, getId("gold_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(INVAR_PLATE, Ingredient.of(INGOTS_INVAR), 1, 200, CompactorMode.PLATE).unlockedBy("has_invar", has(INGOTS_INVAR)).save(recipe, getId("invar_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(IRON_PLATE, Ingredient.of(Tags.Items.INGOTS_IRON), 1, 200, CompactorMode.PLATE).unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).save(recipe, getId("iron_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(LEAD_PLATE, Ingredient.of(INGOTS_LEAD), 1, 200, CompactorMode.PLATE).unlockedBy("has_lead", has(INGOTS_LEAD)).save(recipe, getId("lead_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(NICKEL_PLATE, Ingredient.of(INGOTS_NICKEL), 1, 200, CompactorMode.PLATE).unlockedBy("has_nickel", has(INGOTS_NICKEL)).save(recipe, getId("nickel_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(SILVER_PLATE, Ingredient.of(INGOTS_SILVER), 1, 200, CompactorMode.PLATE).unlockedBy("has_silver", has(INGOTS_SILVER)).save(recipe, getId("silver_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(TIN_PLATE, Ingredient.of(INGOTS_TIN), 1, 200, CompactorMode.PLATE).unlockedBy("has_tin", has(INGOTS_TIN)).save(recipe, getId("tin_plate_from_compacting"));
        CompactingRecipeBuilder.compacting(URANIUM_PLATE, Ingredient.of(INGOTS_URANIUM), 1, 200, CompactorMode.PLATE).unlockedBy("has_uranium", has(INGOTS_URANIUM)).save(recipe, getId("uranium_plate_from_compacting"));

        //TODO in Foundation mod
        final Tags.IOptionalNamedTag<Item> oresCopper = ItemTags.createOptional(new ResourceLocation("forge", "ores/copper"));

        //Raw Materials
        CrushingRecipeBuilder.crushing(Items.RAW_COPPER, 2, Ingredient.of(oresCopper), 200).secondary(Items.RAW_GOLD, 0.05F).unlockedBy("has_copper_ore", has(oresCopper)).save(recipe, getId("raw_copper_from_ore"));
        CrushingRecipeBuilder.crushing(Items.DIAMOND, 2, Ingredient.of(Tags.Items.ORES_DIAMOND), 200).unlockedBy("has_diamond_ore", has(Tags.Items.ORES_DIAMOND)).save(recipe, getId("raw_diamond_from_ore"));
        CrushingRecipeBuilder.crushing(Items.RAW_GOLD, 2, Ingredient.of(Tags.Items.ORES_GOLD), 200).secondary(Items.RAW_COPPER, 0.4F).unlockedBy("has_gold_ore", has(Tags.Items.ORES_GOLD)).save(recipe, getId("raw_gold_from_ore"));
        CrushingRecipeBuilder.crushing(Items.RAW_IRON, 2, Ingredient.of(Tags.Items.ORES_IRON), 200).secondary(RAW_NICKEL, 0.1F).unlockedBy("has_iron_ore", has(Tags.Items.ORES_IRON)).save(recipe, getId("raw_iron_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_LEAD, 2, Ingredient.of(ORES_LEAD), 200).secondary(RAW_SILVER, 0.08F).unlockedBy("has_lead_ore", has(ORES_LEAD)).save(recipe, getId("raw_lead_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_NICKEL, 2, Ingredient.of(ORES_NICKEL), 200).unlockedBy("has_nickel_ore", has(ORES_NICKEL)).save(recipe, getId("raw_nickel_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_SILVER, 2, Ingredient.of(ORES_SILVER), 200).secondary(RAW_LEAD, 0.09F).unlockedBy("has_silver_ore", has(ORES_SILVER)).save(recipe, getId("raw_silver_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_TIN, 2, Ingredient.of(ORES_TIN), 200).unlockedBy("has_tin_ore", has(ORES_TIN)).save(recipe, getId("raw_tin_from_ore"));
        CrushingRecipeBuilder.crushing(RAW_URANIUM, 2, Ingredient.of(ORES_URANIUM), 200).unlockedBy("has_uranium_ore", has(ORES_URANIUM)).save(recipe, getId("raw_uranium_from_ore"));
    }
}
