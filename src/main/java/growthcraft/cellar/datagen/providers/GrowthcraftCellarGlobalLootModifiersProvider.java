package growthcraft.cellar.datagen.providers;

import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.shared.Reference;
import growthcraft.lib.loot.AddItemModifier;
import growthcraft.lib.loot.AddLootTableModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class GrowthcraftCellarGlobalLootModifiersProvider extends GlobalLootModifierProvider{
	public GrowthcraftCellarGlobalLootModifiersProvider(PackOutput output) {
		super(output, Reference.MODID);
	}


	// NOT USED.
	// see the similar class in core module.

	@Override
	protected void start() {
		add("add_loot_"+ Reference.LootTable.PILLAGER_OUTPOST_CHEST_LOOT , loot(name(Reference.LootTable.PILLAGER_OUTPOST_CHEST_LOOT), LootTableIdCondition.builder(BuiltInLootTables.PILLAGER_OUTPOST).build()));
		add("add_loot_"+ Reference.LootTable.UNDERWATER_RUIN_LOOT , loot(name(Reference.LootTable.UNDERWATER_RUIN_LOOT), LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_SMALL).or(LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_BIG)).build()));
		add("add_loot_"+ Reference.LootTable.SHIPWRECK_CHEST_LOOT , loot(name(Reference.LootTable.SHIPWRECK_CHEST_LOOT), LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_SUPPLY).or(LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_MAP)).build()));
		add("add_loot_"+ Reference.LootTable.VILLAGE_CHEST_LOOT , loot(name(Reference.LootTable.VILLAGE_CHEST_LOOT),  LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_BUTCHER).build()));
		add("add_loot_"+ Reference.LootTable.BURIED_TREASURE_CHEST_LOOT , loot(name(Reference.LootTable.BURIED_TREASURE_CHEST_LOOT), LootTableIdCondition.builder(BuiltInLootTables.BURIED_TREASURE).build()));
		add("add_loot_"+ Reference.LootTable.WOODLAND_MANSION_CHEST_LOOT , loot(name(Reference.LootTable.WOODLAND_MANSION_CHEST_LOOT), LootTableIdCondition.builder(BuiltInLootTables.WOODLAND_MANSION).build()));
		add("add_loot_"+ Reference.LootTable.STRONGHOLD_CHEST_LOOT , loot(name(Reference.LootTable.STRONGHOLD_CHEST_LOOT), LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CORRIDOR).or(LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CROSSING)).build()));
		
		add("grape_seeds_purple_from_grass", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.01f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()}, 
				GrowthcraftCellarItems.GRAPE_PURPLE_SEED.get().asItem()));
		
		add("grape_seeds_red_from_grass", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.01f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()}, 
				GrowthcraftCellarItems.GRAPE_RED_SEEDS.get().asItem()));
		
		add("grape_seeds_white_from_grass", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.01f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()}, 
				GrowthcraftCellarItems.GRAPE_WHITE_SEEDS.get().asItem()));
		
		add("hops_seeds_white_from_grass", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.01f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()}, 
				GrowthcraftCellarItems.HOPS_SEED.get().asItem()));
		
		add("yeast_bayanus_from_grass", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.01f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()}, 
				GrowthcraftCellarItems.YEAST_BAYANUS.get().asItem()));
		
		add("yeast_brewers_from_grass", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.01f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build()}, 
				GrowthcraftCellarItems.YEAST_BREWERS.get().asItem()));
		
		add("yeast_ethereal_from_chorus_flower", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.05f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.CHORUS_FLOWER).build()}, 
				GrowthcraftCellarItems.YEAST_ETHEREAL.get().asItem()));
		
		add("yeast_lager_from_snow", new AddItemModifier(new LootItemCondition[]{
				LootItemRandomChanceCondition.randomChance(0.02f).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SNOW).build()}, 
				GrowthcraftCellarItems.YEAST_LAGER.get().asItem()));
	}

	// again...
	// NOT USED.
	// see the similar class in core module.

	private static LootModifier loot(ResourceLocation id, LootItemCondition... cond) {
		return new AddLootTableModifier(cond, id);
	}
	
	private static ResourceLocation name(String lootTableName) {
		return new ResourceLocation(Reference.MODID, "chests/" + lootTableName);
	}
}
