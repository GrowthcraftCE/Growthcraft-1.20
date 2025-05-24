package growthcraft.cellar.datagen.providers;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import growthcraft.cellar.shared.Reference;
import growthcraft.cellar.world.GrowthcraftCellarBiomeModifiers;
import growthcraft.cellar.world.GrowthcraftCellarFeatures;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class GrowthcraftCellarWorldGenProvider extends DatapackBuiltinEntriesProvider{
	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, GrowthcraftCellarFeatures.Configured::init)
			.add(Registries.PLACED_FEATURE, GrowthcraftCellarFeatures.Placed::init)
			.add(ForgeRegistries.Keys.BIOME_MODIFIERS, GrowthcraftCellarBiomeModifiers::bootstrap);
	
	public GrowthcraftCellarWorldGenProvider(PackOutput output, CompletableFuture<Provider> registries) {
		super(output, registries, BUILDER, Set.of(Reference.MODID));
	}
	
    @Override
    public String getName() {
        return "Growthcraft Cellar WorldGen";
    }
}
