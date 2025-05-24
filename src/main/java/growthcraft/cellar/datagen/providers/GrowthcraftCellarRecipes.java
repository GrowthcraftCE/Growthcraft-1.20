package growthcraft.cellar.datagen.providers;

import static growthcraft.lib.utils.FormatUtils.HAS_ITEM;

import java.util.function.Consumer;

import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.init.GrowthcraftCellarTags;
import growthcraft.cellar.shared.Reference;
import growthcraft.core.datagen.shared.GrowthcraftRecipeBuilder;
import growthcraft.core.init.config.BooleanFromConfigFileCondition;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;

public class GrowthcraftCellarRecipes extends RecipeProvider{

	public GrowthcraftCellarRecipes(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GrowthcraftCellarBlocks.BREW_KETTLE.get())
		.requires(Blocks.CAULDRON)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CAULDRON))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GrowthcraftCellarBlocks.CULTURE_JAR.get())
		.pattern("BAB")
		.pattern("B B")
		.pattern("BBB")
		.define('A', ItemTags.PLANKS)
		.define('B', Tags.Items.GLASS_PANES)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemTags.PLANKS).build()))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GrowthcraftCellarBlocks.FERMENTATION_BARREL_OAK.get())
		.pattern("AAA")
		.pattern("BBB")
		.pattern("AAA")
		.define('A', Tags.Items.INGOTS_IRON)
		.define('B', Blocks.OAK_PLANKS)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.OAK_PLANKS))
		.save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GrowthcraftCellarBlocks.FRUIT_PRESS.get())
		.pattern("ABA")
		.pattern("CCC")
		.pattern("DDD")
		.define('A', Tags.Items.FENCES)
		.define('B', Blocks.PISTON)
		.define('C', Tags.Items.INGOTS_IRON)
		.define('D', ItemTags.PLANKS)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.PISTON))
		.save(consumer);
		
		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.MISC, GrowthcraftCellarItems.GRAIN.get(), 3)
			.addCondition(new TagEmptyCondition("forge:grain/barley"))
			.requires(Items.WHEAT, 4)
			.group(Reference.MODID).unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.WHEAT))
			.save(consumer);
		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.MISC, GrowthcraftCellarItems.GRAIN.get(), 3)
			.addCondition(new NotCondition(new TagEmptyCondition("forge:grain/barley")))
			.requires(Items.WHEAT, 5)
			.group(Reference.MODID).unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.WHEAT))
			.save(consumer);
		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.MISC, GrowthcraftCellarItems.GRAIN.get(), 3)
			.addCondition(new NotCondition(new TagEmptyCondition("forge:grain/barley")))
			.requires(GrowthcraftCellarTags.Items.TAG_BARLEY)
			.requires(GrowthcraftCellarTags.Items.TAG_BARLEY)
			.requires(GrowthcraftCellarTags.Items.TAG_BARLEY)
			.group(Reference.MODID).unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.WHEAT))
			.save(consumer);
		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.MISC, GrowthcraftCellarItems.GRAIN.get(), 3)
			.addCondition(new NotCondition(new TagEmptyCondition("forge:grain/barley")))
			.addCondition(new NotCondition(new BooleanFromConfigFileCondition("cellar", "brewing.allow_additional_adjunct_grains")))
			.addCondition(new NotCondition(new TagEmptyCondition("growthcraft_cellar:adjunct_grains_basic")))
			.requires(GrowthcraftCellarTags.Items.TAG_BARLEY)
			.requires(GrowthcraftCellarTags.Items.TAG_BARLEY)
			.requires(GrowthcraftCellarTags.Items.TAG_ADJUNCT_GRAINS1)
			.requires(GrowthcraftCellarTags.Items.TAG_ADJUNCT_GRAINS1)
			.group(Reference.MODID).unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.WHEAT))
			.save(consumer);
		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.MISC, GrowthcraftCellarItems.GRAIN.get(), 3)
			.addCondition(new NotCondition(new TagEmptyCondition("forge:grain/barley")))
			.addCondition(new BooleanFromConfigFileCondition("cellar", "brewing.allow_additional_adjunct_grains"))
			.addCondition(new NotCondition(new TagEmptyCondition("growthcraft_cellar:adjunct_grains_extended")))
			.requires(GrowthcraftCellarTags.Items.TAG_BARLEY)
			.requires(GrowthcraftCellarTags.Items.TAG_BARLEY)
			.requires(GrowthcraftCellarTags.Items.TAG_ADJUNCT_GRAINS2)
			.requires(GrowthcraftCellarTags.Items.TAG_ADJUNCT_GRAINS2)
			.group(Reference.MODID).unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.WHEAT))
			.save(consumer);
		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.MISC, GrowthcraftCellarItems.GRAIN.get(), 3)
			.addCondition(new TagEmptyCondition("forge:grain/barley"))
			.addCondition(new NotCondition(new BooleanFromConfigFileCondition("cellar", "brewing.allow_additional_adjunct_grains")))
			.addCondition(new NotCondition(new TagEmptyCondition("growthcraft_cellar:adjunct_grains_basic")))
			.requires(Items.WHEAT)
			.requires(Items.WHEAT)
			.requires(Items.WHEAT)
			.requires(GrowthcraftCellarTags.Items.TAG_ADJUNCT_GRAINS1)
			.requires(GrowthcraftCellarTags.Items.TAG_ADJUNCT_GRAINS1)
			.group(Reference.MODID).unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.WHEAT))
			.save(consumer);
		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.MISC, GrowthcraftCellarItems.GRAIN.get(), 3)
			.addCondition(new TagEmptyCondition("forge:grain/barley"))
			.addCondition(new BooleanFromConfigFileCondition("cellar", "brewing.allow_additional_adjunct_grains"))
			.addCondition(new NotCondition(new TagEmptyCondition("growthcraft_cellar:adjunct_grains_extended_minus_wheat")))
			.requires(Items.WHEAT)
			.requires(Items.WHEAT)
			.requires(Items.WHEAT)
			.requires(GrowthcraftCellarTags.Items.TAG_ADJUNCT_GRAINS2S)
			.requires(GrowthcraftCellarTags.Items.TAG_ADJUNCT_GRAINS2S)
			.group(Reference.MODID).unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.WHEAT))
			.save(consumer);
		// don't think too hard about the above thing.
		// for example the last one above is: grain recipe if we don't have barley, but we do have something like oats and user said in the config file that they're okay with corn rice too and the tag containing last two isn't empty.

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GrowthcraftCellarBlocks.ROASTER.get())
		.pattern(" I ")
		.pattern(" B ")
		.pattern("I I")
		.define('I', Tags.Items.INGOTS_IRON)
		.define('B', GrowthcraftCellarBlocks.BREW_KETTLE.get())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftCellarBlocks.BREW_KETTLE.get()))
		.save(consumer);
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GrowthcraftCellarBlocks.CORK_COASTER.get(), 2)
		.requires(GrowthcraftCellarItems.CORK_BARK.get())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftCellarItems.CORK_BARK.get()))
		.save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GrowthcraftCellarItems.HOPS_SEED.get(), 2)
		.requires(GrowthcraftCellarItems.HOPS.get())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftCellarItems.CORK_BARK.get()))
		.save(consumer);

	}
	
    @Override
    public String getName() {
        return "Growthcraft Cellar Recipes";
    }
}
