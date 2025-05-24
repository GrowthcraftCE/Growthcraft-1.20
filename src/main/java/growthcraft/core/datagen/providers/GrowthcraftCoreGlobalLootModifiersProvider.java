package growthcraft.core.datagen.providers;

import growthcraft.apiary.init.GrowthcraftApiaryItems;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.core.datagen.shared.BiomeTagLootCondition;
import growthcraft.core.datagen.shared.ModuleLoadedLootCondition;
import growthcraft.core.shared.Reference;
import growthcraft.lib.loot.AddItemModifier;
import growthcraft.lib.loot.AddItemReplaceSeedsModifier;
import growthcraft.lib.loot.AddLootTableModifier;
import growthcraft.lib.loot.WeightedSeedReplacementModifier;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.rice.init.GrowthcraftRiceItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class GrowthcraftCoreGlobalLootModifiersProvider extends GlobalLootModifierProvider{
	public GrowthcraftCoreGlobalLootModifiersProvider(PackOutput output) {
		super(output, Reference.MODID);
	}
	
	@Override
	protected void start() {
		//GC Apiary
		add("bee_from_vanilla_bee_nest", new AddItemModifier(new LootItemCondition[] {
					LootItemRandomChanceCondition.randomChance(0.50f).build(),
					LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.BEE_NEST).build(),
					ModuleLoadedLootCondition.isLoaded("apiary")
				},
				GrowthcraftApiaryItems.BEE.get().asItem()));
		
		//GC Cellar
		add("add_loot_"+ growthcraft.cellar.shared.Reference.LootTable.PILLAGER_OUTPOST_CHEST_LOOT , loot(name(growthcraft.cellar.shared.Reference.LootTable.PILLAGER_OUTPOST_CHEST_LOOT), LootTableIdCondition.builder(BuiltInLootTables.PILLAGER_OUTPOST).build()));
		add("add_loot_"+ growthcraft.cellar.shared.Reference.LootTable.UNDERWATER_RUIN_LOOT , loot(name(growthcraft.cellar.shared.Reference.LootTable.UNDERWATER_RUIN_LOOT), LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_SMALL).or(LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_BIG)).build()));
		add("add_loot_"+ growthcraft.cellar.shared.Reference.LootTable.SHIPWRECK_CHEST_LOOT , loot(name(growthcraft.cellar.shared.Reference.LootTable.SHIPWRECK_CHEST_LOOT), LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_SUPPLY).or(LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_MAP)).build()));
		add("add_loot_"+ growthcraft.cellar.shared.Reference.LootTable.VILLAGE_CHEST_LOOT , loot(name(growthcraft.cellar.shared.Reference.LootTable.VILLAGE_CHEST_LOOT),  LootTableIdCondition.builder(BuiltInLootTables.VILLAGE_BUTCHER).build()));
		add("add_loot_"+ growthcraft.cellar.shared.Reference.LootTable.BURIED_TREASURE_CHEST_LOOT , loot(name(growthcraft.cellar.shared.Reference.LootTable.BURIED_TREASURE_CHEST_LOOT), LootTableIdCondition.builder(BuiltInLootTables.BURIED_TREASURE).build()));
		add("add_loot_"+ growthcraft.cellar.shared.Reference.LootTable.WOODLAND_MANSION_CHEST_LOOT , loot(name(growthcraft.cellar.shared.Reference.LootTable.WOODLAND_MANSION_CHEST_LOOT), LootTableIdCondition.builder(BuiltInLootTables.WOODLAND_MANSION).build()));
		add("add_loot_"+ growthcraft.cellar.shared.Reference.LootTable.STRONGHOLD_CHEST_LOOT , loot(name(growthcraft.cellar.shared.Reference.LootTable.STRONGHOLD_CHEST_LOOT), LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CORRIDOR).or(LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CROSSING)).build()));
		
		add("grape_seeds_purple_from_grass", new AddItemReplaceSeedsModifier(new LootItemCondition[] {
					LootItemRandomChanceCondition.randomChance(0.012f).build(),
					LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
					ModuleLoadedLootCondition.isLoaded("grapes")
				},
				GrowthcraftCellarItems.GRAPE_PURPLE_SEED.get().asItem()));
		
		add("grape_seeds_red_from_grass", new AddItemReplaceSeedsModifier(new LootItemCondition[] {
					LootItemRandomChanceCondition.randomChance(0.012f).build(),
					LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
					ModuleLoadedLootCondition.isLoaded("grapes")
				},
				GrowthcraftCellarItems.GRAPE_RED_SEEDS.get().asItem()));
		
		add("grape_seeds_white_from_grass", new AddItemReplaceSeedsModifier(new LootItemCondition[] {
					LootItemRandomChanceCondition.randomChance(0.012f).build(),
					LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
					ModuleLoadedLootCondition.isLoaded("grapes")
				},
				GrowthcraftCellarItems.GRAPE_WHITE_SEEDS.get().asItem()));
		
		add("hops_seeds_from_grass", new AddItemModifier(new LootItemCondition[] {
					LootItemRandomChanceCondition.randomChance(0.015f).build(),
					LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
					ModuleLoadedLootCondition.isLoaded("cellar")
				},
				GrowthcraftCellarItems.HOPS_SEED.get().asItem()));
		
		add("yeast_bayanus_from_grass", new AddItemReplaceSeedsModifier(new LootItemCondition[] {
					LootItemRandomChanceCondition.randomChance(0.020f).build(),
					LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
					ModuleLoadedLootCondition.isLoaded("cellar")
				},
				GrowthcraftCellarItems.YEAST_BAYANUS.get().asItem()));
		
		add("yeast_brewers_from_grass", new AddItemReplaceSeedsModifier(new LootItemCondition[] {
					LootItemRandomChanceCondition.randomChance(0.020f).build(),
					LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
					ModuleLoadedLootCondition.isLoaded("cellar")
				},
				GrowthcraftCellarItems.YEAST_BREWERS.get().asItem()));
		
		add("yeast_ethereal_from_chorus_flower", new AddItemModifier(new LootItemCondition[] {
					LootItemRandomChanceCondition.randomChance(0.075f).build(),
					LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.CHORUS_FLOWER).build(),
					ModuleLoadedLootCondition.isLoaded("cellar")
				},
				GrowthcraftCellarItems.YEAST_ETHEREAL.get().asItem()));
		
		add("yeast_lager_from_snow", new AddItemModifier(new LootItemCondition[] {
					LootItemRandomChanceCondition.randomChance(0.050f).build(),
					LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SNOW).build(),
					ModuleLoadedLootCondition.isLoaded("cellar")
				},
				GrowthcraftCellarItems.YEAST_LAGER.get().asItem()));
		
//		GC Milk
		add("thistle_seeds_from_grass", new AddItemReplaceSeedsModifier(new LootItemCondition[]{
					LootItemRandomChanceCondition.randomChance(0.075f).build(),
					LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
					BiomeTagLootCondition.forTag(Tags.Biomes.IS_COLD_OVERWORLD),
					ModuleLoadedLootCondition.isLoaded("milk")
				},
				GrowthcraftMilkItems.THISTLE_SEED.get().asItem()));
		
//		GC Rice
		add("rice_from_grass", new AddItemModifier(new LootItemCondition[] {
					LootItemRandomChanceCondition.randomChance(0.005f).build(),
					// 3% (0.03 gives us about 20% compared to wheat... a 25x25 area full of grass (625 blocks) gives about 75 wheat and 15 rice seeds)
					// we'll try 0.5% for now in any biome. way more given by other modifier below.        (0.5% still gives 5-10 rice seeds from a 25x25 testbed)
					AnyOfCondition.anyOf(
							LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS),
							LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS)
					).build(),
					ModuleLoadedLootCondition.isLoaded("rice")
				},
				GrowthcraftRiceItems.RICE_GRAINS.get().asItem()));
		add("rice_from_grass_wet", new WeightedSeedReplacementModifier(new LootItemCondition[] {
					// 100%; chance condition removed.
					AnyOfCondition.anyOf(
							LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS),
							LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS)
					).build(),
					AnyOfCondition.anyOf(
							BiomeTagLootCondition.getBuilder().forTag(Tags.Biomes.IS_SWAMP),
							BiomeTagLootCondition.getBuilder().forTag(BiomeTags.IS_RIVER)
					).build(),
					ModuleLoadedLootCondition.isLoaded("rice")
				},
				GrowthcraftRiceItems.RICE_GRAINS.get().asItem(),
				40, 50)); // 10 stays intact
	}
	
	private static LootModifier loot(ResourceLocation id, LootItemCondition... cond) {
		return new AddLootTableModifier(cond, id);
	}
	
	private static ResourceLocation name(String lootTableName) {
		return new ResourceLocation(growthcraft.cellar.shared.Reference.MODID, "chests/" + lootTableName);
	}
}
