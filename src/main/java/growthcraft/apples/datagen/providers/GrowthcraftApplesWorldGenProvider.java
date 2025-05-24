package growthcraft.apples.datagen.providers;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import growthcraft.apples.shared.Reference;
import growthcraft.apples.world.GrowthcraftApplesBiomeModifiers;
import growthcraft.apples.world.GrowthcraftApplesFeatures;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class GrowthcraftApplesWorldGenProvider extends DatapackBuiltinEntriesProvider{
	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, GrowthcraftApplesFeatures.Configured::init)
			.add(Registries.PLACED_FEATURE, GrowthcraftApplesFeatures.Placed::init)
			.add(ForgeRegistries.Keys.BIOME_MODIFIERS, GrowthcraftApplesBiomeModifiers::bootstrap);
	
	public GrowthcraftApplesWorldGenProvider(PackOutput output, CompletableFuture<Provider> registries) {
		super(output, registries, BUILDER, Set.of(Reference.MODID));
	}
	
    @Override
    public String getName() {
        return "Growthcraft Apples WorldGen";
    }
}
