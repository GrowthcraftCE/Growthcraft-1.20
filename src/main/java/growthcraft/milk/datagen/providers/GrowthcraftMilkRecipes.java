package growthcraft.milk.datagen.providers;

import java.util.function.Consumer;

import growthcraft.apiary.init.GrowthcraftApiaryItems;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.core.datagen.shared.GrowthcraftRecipeBuilder;
import growthcraft.core.init.GrowthcraftTags;
import growthcraft.core.init.config.OptionalFeatureCondition;
import growthcraft.milk.init.GrowthcraftMilkBlocks;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.init.GrowthcraftMilkTags;
import growthcraft.milk.shared.Reference;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import static growthcraft.lib.utils.FormatUtils.HAS_ITEM;

public class GrowthcraftMilkRecipes extends RecipeProvider{

	public GrowthcraftMilkRecipes(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		GrowthcraftRecipeBuilder.crafting_shaped(RecipeCategory.MISC, GrowthcraftMilkItems.CHEESE_CLOTH.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.pattern("sss")
		.pattern("s s")
		.pattern("sss")
		.define('s', Items.STRING)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shaped(RecipeCategory.MISC, GrowthcraftMilkBlocks.CHEESE_PRESS.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.pattern("III")
		.pattern("ICI")
		.pattern("SSS")
		.define('I', Tags.Items.INGOTS_IRON)
		.define('C', Tags.Items.CHESTS_WOODEN)
		.define('S', ItemTags.WOODEN_SLABS)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shaped(RecipeCategory.MISC, GrowthcraftMilkBlocks.CHURN.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.pattern(" S ")
		.pattern("P P")
		.pattern("PPP")
		.define('P', ItemTags.PLANKS)
		.define('S', Tags.Items.RODS_WOODEN)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemTags.PLANKS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.ICE_CREAM_APPLE.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(Items.APPLE)
		.requires(Items.SUGAR)
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.ICE_CREAM_CHOCOLATE.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(Items.COCOA_BEANS)
		.requires(Items.SUGAR)
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.ICE_CREAM_GRAPE_PURPLE.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(GrowthcraftCellarItems.GRAPE_PURPLE.get())
		.requires(Items.SUGAR)
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.ICE_CREAM_GRAPE_RED.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(GrowthcraftCellarItems.GRAPE_RED.get())
		.requires(Items.SUGAR)
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.ICE_CREAM_GRAPE_WHITE.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(GrowthcraftCellarItems.GRAPE_WHITE.get())
		.requires(Items.SUGAR)
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.ICE_CREAM_HONEY.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(Ingredient.of(GrowthcraftApiaryItems.HONEY_COMB_FULL.get(), Items.HONEYCOMB))
		.requires(Items.SUGAR)
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.ICE_CREAM_PUMPKIN.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(Items.PUMPKIN)
		.requires(Items.SUGAR)
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.ICE_CREAM_WATERMELON.get())
		  .addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(Items.MELON_SLICE)
		.requires(Items.SUGAR)
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shaped(RecipeCategory.MISC, GrowthcraftMilkItems.MILKING_BUCKET_IRON.get())
		   .addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.pattern("NNN")
		.pattern("I I")
		.pattern(" I ")
		.define('N', Tags.Items.NUGGETS_IRON)
		.define('I', Tags.Items.INGOTS_IRON)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shaped(RecipeCategory.MISC, GrowthcraftMilkBlocks.MIXING_VAT.get())
		   .addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.pattern("   ")
		.pattern(" B ")
		.pattern("I I")
		.define('B', GrowthcraftCellarBlocks.BREW_KETTLE.get())
		.define('I', Tags.Items.INGOTS_IRON)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftCellarBlocks.BREW_KETTLE.get()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shaped(RecipeCategory.MISC, GrowthcraftMilkBlocks.PANCHEON.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.pattern("C C")
		.pattern("CCC")
		.define('C', Items.CLAY_BALL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(Items.CLAY_BALL))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.BUTTER_SALTED.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkItems.BUTTER.get())
		.requires(GrowthcraftTags.Items.SALT_FORGE)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(GrowthcraftMilkItems.BUTTER.get()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.MISC, GrowthcraftMilkItems.THISTLE_SEED.get(), 2)
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkItems.THISTLE.get())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkItems.THISTLE.get()).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.YOGURT_APPLE.get())
		    .addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(Items.APPLE)
		.requires(GrowthcraftMilkItems.STARTER_CULTURE.get())
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.YOGURT_CHOCOLATE.get())
		    .addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(Items.COCOA_BEANS)
		.requires(GrowthcraftMilkItems.STARTER_CULTURE.get())
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.YOGURT_GRAPE_PURPLE.get())
		    .addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(GrowthcraftCellarItems.GRAPE_PURPLE.get())
		.requires(GrowthcraftMilkItems.STARTER_CULTURE.get())
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.YOGURT_GRAPE_RED.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(GrowthcraftCellarItems.GRAPE_RED.get())
		.requires(GrowthcraftMilkItems.STARTER_CULTURE.get())
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.YOGURT_GRAPE_WHITE.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(GrowthcraftCellarItems.GRAPE_WHITE.get())
		.requires(GrowthcraftMilkItems.STARTER_CULTURE.get())
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.YOGURT_HONEY.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(Ingredient.of(GrowthcraftApiaryItems.HONEY_COMB_FULL.get(), Items.HONEYCOMB))
		.requires(GrowthcraftMilkItems.STARTER_CULTURE.get())
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.YOGURT_PLAIN.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(GrowthcraftMilkItems.STARTER_CULTURE.get())
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.YOGURT_PUMPKIN.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(Items.PUMPKIN)
		.requires(GrowthcraftMilkItems.STARTER_CULTURE.get())
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.YOGURT_WATERMELON.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.requires(Items.MELON_SLICE)
		.requires(GrowthcraftMilkItems.STARTER_CULTURE.get())
		.requires(Items.BOWL)
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);

		GrowthcraftRecipeBuilder.crafting_shapeless(RecipeCategory.FOOD, GrowthcraftMilkItems.CHEESE_RICOTTA_SLICE.get())
			.addCondition(new OptionalFeatureCondition(growthcraft.milk.shared.Reference.NAME_SHORT))
		.requires(Items.BOWL)
		.requires(GrowthcraftMilkItems.RICOTTA_CHEESE_CURDS_DRAINED.get())
		.requires(GrowthcraftMilkItems.RICOTTA_CHEESE_CURDS_DRAINED.get())
		.group(Reference.MODID)
		.unlockedBy(HAS_ITEM, InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS).build()))
		.save(consumer);
	}
	
    @Override
    public String getName() {
        return "Growthcraft Milk Recipes";
    }
}
