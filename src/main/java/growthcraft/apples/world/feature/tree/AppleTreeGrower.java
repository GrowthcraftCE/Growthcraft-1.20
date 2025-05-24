package growthcraft.apples.world.feature.tree;

import org.jetbrains.annotations.Nullable;

import growthcraft.apples.world.GrowthcraftApplesFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class AppleTreeGrower extends AbstractTreeGrower {

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean pHasFlowers) {
        return GrowthcraftApplesFeatures.Configured.TREES_APPLE;
    }
}
