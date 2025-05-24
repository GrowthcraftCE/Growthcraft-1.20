package growthcraft.cellar.world;

import growthcraft.cellar.init.GrowthcraftCellarTags;
import growthcraft.cellar.shared.Reference;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import static net.minecraft.world.level.levelgen.GenerationStep.Decoration.VEGETAL_DECORATION;

public class GrowthcraftCellarBiomeModifiers {
	
	private GrowthcraftCellarBiomeModifiers() {
		/* Prevent generation of public constructor */
	}
	
    public static final ResourceKey<BiomeModifier> ADD_CORK_TREE = registerKey("add_cork_tree");
    public static final ResourceKey<BiomeModifier> ADD_CORK_TREE_RARE = registerKey("add_cork_tree_rare");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);
        
        context.register(ADD_CORK_TREE, new AddFeaturesBiomeModifier(
                biomes.getOrThrow(GrowthcraftCellarTags.Biomes.HAS_CORK_TREE),
                HolderSet.direct(placedFeatures.getOrThrow(GrowthcraftCellarFeatures.Placed.CORK_TREE_PLACED)),
                VEGETAL_DECORATION));
        
        context.register(ADD_CORK_TREE_RARE, new AddFeaturesBiomeModifier(
                biomes.getOrThrow(GrowthcraftCellarTags.Biomes.HAS_CORK_TREE_RARE),
                HolderSet.direct(placedFeatures.getOrThrow(GrowthcraftCellarFeatures.Placed.CORK_TREE_PLACED_RARE)),
                VEGETAL_DECORATION));
    }
	
    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(Reference.MODID, name));
    }
}
