package growthcraft.milk.datagen.providers;

import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.init.GrowthcraftMilkTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class GrowthcraftMilkItemTagsProvider extends ItemTagsProvider
{
	// constructor
	public GrowthcraftMilkItemTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, String modId, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(pOutput, pLookupProvider, pBlockTags, modId, existingFileHelper);
	}

	@Override
    public String getName() {
        return "Growthcraft Milk Tags";
    }

	@Override
	protected void addTags(HolderLookup.Provider provider)
	{
		tag(GrowthcraftMilkTags.Items.CHEESE_SLICES).add(GrowthcraftMilkItems.CHEESE_APPENZELLER_SLICE.get());
		tag(GrowthcraftMilkTags.Items.CHEESE_SLICES).add(GrowthcraftMilkItems.CHEESE_ASIAGO_SLICE.get());
		tag(GrowthcraftMilkTags.Items.CHEESE_SLICES).add(GrowthcraftMilkItems.CHEESE_CASU_MARZU_SLICE.get());
		tag(GrowthcraftMilkTags.Items.CHEESE_SLICES).add(GrowthcraftMilkItems.CHEESE_CHEDDAR_SLICE.get());
		tag(GrowthcraftMilkTags.Items.CHEESE_SLICES).add(GrowthcraftMilkItems.CHEESE_EMMENTALER_SLICE.get());
		tag(GrowthcraftMilkTags.Items.CHEESE_SLICES).add(GrowthcraftMilkItems.CHEESE_GORGONZOLA_SLICE.get());
		tag(GrowthcraftMilkTags.Items.CHEESE_SLICES).add(GrowthcraftMilkItems.CHEESE_GOUDA_SLICE.get());
		tag(GrowthcraftMilkTags.Items.CHEESE_SLICES).add(GrowthcraftMilkItems.CHEESE_MONTEREY_SLICE.get());
		tag(GrowthcraftMilkTags.Items.CHEESE_SLICES).add(GrowthcraftMilkItems.CHEESE_PARMESAN_SLICE.get());
		tag(GrowthcraftMilkTags.Items.CHEESE_SLICES).add(GrowthcraftMilkItems.CHEESE_PROVOLONE_SLICE.get());
	}
}
