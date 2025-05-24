package growthcraft.apples.world;

import java.util.List;

import growthcraft.apples.init.GrowthcraftApplesBlocks;
import growthcraft.apples.shared.Reference;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.CherryFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.CherryTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;

public class GrowthcraftApplesFeatures {
	private GrowthcraftApplesFeatures() {
		/* Prevent generation of public constructor */
	}
	
	public static class Configured {
		private Configured() {
			/* Prevent generation of public constructor */
		}
		
		public static final ResourceKey<ConfiguredFeature<?, ?>> APPLE_TREE = registerKey(Reference.UnlocalizedName.APPLE_TREE);
		public static final ResourceKey<ConfiguredFeature<?, ?>> BIG_APPLE_TREE = registerKey(Reference.UnlocalizedName.BIG_APPLE_TREE);
		public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_APPLE = registerKey("trees_apple");
		
		
		public static void init(BootstapContext<ConfiguredFeature<?, ?>> context) {

			FeatureUtils.register(context, APPLE_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
					// Trunk block
	                BlockStateProvider.simple(GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get()),
	                // Trunk placer (baseHeight, heightRandA, heightRandB)
	                new StraightTrunkPlacer(5, 2, 0),	                
	                // Leaves block
	                BlockStateProvider.simple(GrowthcraftApplesBlocks.APPLE_TREE_LEAVES.get()),
	                // Leaves placer (radius, offset, height)
	                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
	                // Spawn Area Saturation (limit, lowerSize, upperSize)
	                new TwoLayersFeatureSize(1, 0, 2)).build()
	        );
			
			FeatureUtils.register(context, BIG_APPLE_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
					// Trunk block
	                BlockStateProvider.simple(GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get()),
	                // Trunk placer (baseHeight, heightRandA, heightRandB)
	                new CherryTrunkPlacer(6, 1, 0, new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(1), 1).add(ConstantInt.of(2), 1).add(ConstantInt.of(3), 1).build()), UniformInt.of(2, 4), UniformInt.of(-4, -3), UniformInt.of(-1, 0)),
	                // Leaves block
	                BlockStateProvider.simple(GrowthcraftApplesBlocks.APPLE_TREE_LEAVES.get()),
	                // Leaves placer (radius, offset, height)
	                new CherryFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(5), 0.25F, 0.5F, 0F, 0F),
	                // Spawn Area Saturation (limit, lowerSize, upperSize)
	                new TwoLayersFeatureSize(1, 0, 2)).build()
	        );
			
			HolderGetter<PlacedFeature> placedFeatureHolderGetter = context.lookup(Registries.PLACED_FEATURE);
			Holder<PlacedFeature> appleTree = placedFeatureHolderGetter.getOrThrow(Placed.APPLE_TREE_CHECKED);
			Holder<PlacedFeature> bigAppleTree = placedFeatureHolderGetter.getOrThrow(Placed.BIG_APPLE_TREE_CHECKED);
            FeatureUtils.register(context, TREES_APPLE, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(bigAppleTree, 0.25F)), appleTree));
		}

		private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
			return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Reference.MODID, name));
		}
	}

	public static class Placed {
		private Placed() {
			/* Prevent generation of public constructor */
		}
		
		public static final ResourceKey<PlacedFeature> APPLE_TREE_CHECKED = createKey(Reference.UnlocalizedName.APPLE_TREE);
		public static final ResourceKey<PlacedFeature> BIG_APPLE_TREE_CHECKED = createKey(Reference.UnlocalizedName.BIG_APPLE_TREE);
		public static final ResourceKey<PlacedFeature> TREES_APPLE_PLACED = createKey("trees_apple");
		
		public static void init(BootstapContext<PlacedFeature> context) {
			HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
			
			context.register(APPLE_TREE_CHECKED, new PlacedFeature(features.getOrThrow(Configured.APPLE_TREE), List.of(PlacementUtils.filteredByBlockSurvival(GrowthcraftApplesBlocks.APPLE_TREE_SAPLING.get()))));
			context.register(BIG_APPLE_TREE_CHECKED, new PlacedFeature(features.getOrThrow(Configured.BIG_APPLE_TREE), List.of(PlacementUtils.filteredByBlockSurvival(GrowthcraftApplesBlocks.APPLE_TREE_SAPLING.get()))));
			context.register(TREES_APPLE_PLACED, new PlacedFeature(features.getOrThrow(Configured.TREES_APPLE), List.of(
					RarityFilter.onAverageOnceEvery(10),
					PlacementUtils.countExtra(0, 0.1f, 1),
                    InSquarePlacement.spread(),
                    SurfaceWaterDepthFilter.forMaxDepth(0),
                    PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                    BiomeFilter.biome())));

		}

	    private static ResourceKey<PlacedFeature> createKey(String name) {
	        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Reference.MODID, name));
	    }
	}

}
