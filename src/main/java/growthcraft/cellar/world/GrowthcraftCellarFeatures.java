package growthcraft.cellar.world;

import java.util.List;
import java.util.OptionalInt;

import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.shared.Reference;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;

public class GrowthcraftCellarFeatures {
	private GrowthcraftCellarFeatures() {
		/* Prevent generation of public constructor */
	}

	public static class Configured {
		private Configured() {
			/* Prevent generation of public constructor */
		}
		
		public static final ResourceKey<ConfiguredFeature<?, ?>> CORK_TREE = registerKey(Reference.UnlocalizedName.CORK_TREE);
		public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_RUBBERWOOD = registerKey("trees_cork");
		
		
		public static void init(BootstapContext<ConfiguredFeature<?, ?>> context) {

			FeatureUtils.register(context, CORK_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
	                // Trunk block
	                BlockStateProvider.simple(GrowthcraftCellarBlocks.CORK_WOOD_LOG.get()),
	                // Trunk placer (baseHeight, heightRandA, heightRandB)
	                new FancyTrunkPlacer(9, 4, 0),
	                // Leaves block
	                BlockStateProvider.simple(GrowthcraftCellarBlocks.CORK_TREE_LEAVES.get()),
	                // Leaves placer (radius, offset, height)
	                new FancyFoliagePlacer(ConstantInt.of(3), ConstantInt.of(3), 3),
	                // Spawn Area Saturation (limit, lowerSize, upperSize)
	                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))).build()
	        );
			HolderGetter<PlacedFeature> placedFeatureHolderGetter = context.lookup(Registries.PLACED_FEATURE);
			Holder<PlacedFeature> rubberwood = placedFeatureHolderGetter.getOrThrow(Placed.CORK_TREE_CHECKED);
            FeatureUtils.register(context, TREES_RUBBERWOOD, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(rubberwood, 0.1F)), rubberwood));
		}

		private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
			return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Reference.MODID, name));
		}
	}

	public static class Placed {
		private Placed() {
			/* Prevent generation of public constructor */
		}
		
		public static final ResourceKey<PlacedFeature> CORK_TREE_CHECKED = createKey(Reference.UnlocalizedName.CORK_TREE);
		public static final ResourceKey<PlacedFeature> CORK_TREE_PLACED = createKey("trees_cork");
		public static final ResourceKey<PlacedFeature> CORK_TREE_PLACED_RARE = createKey("trees_cork_rare");
		
		public static void init(BootstapContext<PlacedFeature> context) {
			HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
			
			context.register(CORK_TREE_CHECKED, new PlacedFeature(features.getOrThrow(Configured.CORK_TREE), List.of(PlacementUtils.filteredByBlockSurvival(GrowthcraftCellarBlocks.CORK_TREE_SAPLING.get()))));
			context.register(CORK_TREE_PLACED, new PlacedFeature(features.getOrThrow(Configured.TREES_RUBBERWOOD), List.of(
					RarityFilter.onAverageOnceEvery(10),
					PlacementUtils.countExtra(0, 0.25f, 1),
                    InSquarePlacement.spread(),
                    SurfaceWaterDepthFilter.forMaxDepth(0),
                    PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                    BiomeFilter.biome())));
			
			context.register(CORK_TREE_PLACED_RARE, new PlacedFeature(features.getOrThrow(Configured.TREES_RUBBERWOOD), List.of(
					RarityFilter.onAverageOnceEvery(30),
					PlacementUtils.countExtra(0, 0.2f, 1),
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
