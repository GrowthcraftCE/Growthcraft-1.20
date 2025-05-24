package growthcraft.apples.world;

import static net.minecraft.world.level.levelgen.GenerationStep.Decoration.VEGETAL_DECORATION;

import growthcraft.apples.init.GrowthcraftApplesTags;
import growthcraft.apples.shared.Reference;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class GrowthcraftApplesBiomeModifiers {
	
	private GrowthcraftApplesBiomeModifiers() {
		/* Prevent generation of public constructor */
	}
	
    public static final ResourceKey<BiomeModifier> ADD_APPLE_TREE = registerKey("add_apple_tree");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);
        
        context.register(ADD_APPLE_TREE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(GrowthcraftApplesTags.Biomes.HAS_APPLE_TREE),
                HolderSet.direct(placedFeatures.getOrThrow(GrowthcraftApplesFeatures.Placed.TREES_APPLE_PLACED)),
                VEGETAL_DECORATION));
    }
	
    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(Reference.MODID, name));
    }
}
