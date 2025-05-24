package growthcraft.core.datagen.providers;

import java.util.concurrent.CompletableFuture;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import growthcraft.apiary.init.GrowthcraftApiaryItems;
import growthcraft.apiary.init.GrowthcraftApiaryTags;
import growthcraft.apples.init.GrowthcraftApplesBlocks;
import growthcraft.apples.init.GrowthcraftApplesItems;
import growthcraft.apples.init.GrowthcraftApplesTags;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.init.GrowthcraftCellarTags;
import growthcraft.core.init.GrowthcraftItems;
import growthcraft.core.init.GrowthcraftTags;
import growthcraft.core.shared.Reference;
import growthcraft.lib.item.GrowthcraftItem;
import growthcraft.milk.init.GrowthcraftMilkFluids;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.init.GrowthcraftMilkTags;
import growthcraft.rice.init.GrowthcraftRiceItems;
import growthcraft.rice.init.GrowthcraftRiceTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class GrowthcraftCoreItemTags extends ItemTagsProvider{

	public GrowthcraftCoreItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider pBlockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(packOutput, lookupProvider, pBlockTagsProvider.contentsGetter(), Reference.MODID, existingFileHelper);
	}
	
	@Override 
	protected void addTags(HolderLookup.Provider provider) {
		//GC Tags
		tag(GrowthcraftApiaryTags.Items.BEE)
		.add(GrowthcraftApiaryItems.BEE.get());
		tag(GrowthcraftApiaryTags.Items.HONEY_COMB)
		.add(GrowthcraftApiaryItems.HONEY_COMB_EMPTY.get())
		.add(GrowthcraftApiaryItems.HONEY_COMB_FULL.get())
		.add(Items.HONEYCOMB);
		tag(GrowthcraftApiaryTags.Items.BEES_WAX)
		.add(GrowthcraftApiaryItems.BEES_WAX.get());
		
		GrowthcraftApiaryItems.ITEMS.getEntries().forEach(item -> {
			if(item.getId().getPath().contains("bees_wax_")) {
				tag(GrowthcraftApiaryTags.Items.BEES_WAX).add(item.get());
				tag(((@NotNull GrowthcraftItem) item.get()).getDyeColor().getTag()).add(item.get());
			}
		});
		
		tag(GrowthcraftApplesTags.Items.APPLE_WOOD_LOG)
		.add(GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get().asItem())
		.add(GrowthcraftApplesBlocks.APPLE_WOOD_LOG_STRIPPED.get().asItem());
		
		tag(GrowthcraftTags.Items.ROASTER_WRENCH)
		.add(GrowthcraftItems.WRENCH.get())
		.add(TagEntry.optionalTag(new ResourceLocation("forge:tools/wrench")));
		tag(GrowthcraftTags.Items.SALT)
		.add(GrowthcraftItems.SALT.get());
		tag(GrowthcraftCellarTags.Items.TAG_GRAPE_FRUITS)
		.add(GrowthcraftCellarItems.GRAPE_PURPLE.get());
		tag(GrowthcraftCellarTags.Items.TAG_GRAPE_FRUITS)
		.add(GrowthcraftCellarItems.GRAPE_RED.get());
		tag(GrowthcraftCellarTags.Items.TAG_GRAPE_FRUITS)
		.add(GrowthcraftCellarItems.GRAPE_WHITE.get());
		tag(GrowthcraftCellarTags.Items.TAG_GRAPE_SEEDS)
		.add(GrowthcraftCellarItems.GRAPE_PURPLE_SEED.get());
		tag(GrowthcraftCellarTags.Items.TAG_GRAPE_SEEDS)
		.add(GrowthcraftCellarItems.GRAPE_RED_SEEDS.get());
		tag(GrowthcraftCellarTags.Items.TAG_GRAPE_SEEDS)
		.add(GrowthcraftCellarItems.GRAPE_WHITE_SEEDS.get());
		
		tag(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
		.add(GrowthcraftMilkFluids.MILK.bucket.get())
		.add(GrowthcraftMilkFluids.BUTTER_MILK.bucket.get());

		//Vanilla Tags
		tag(ItemTags.WOODEN_BUTTONS)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_BUTTON.get().asItem());
		tag(ItemTags.PLANKS)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK.get().asItem());
		tag(ItemTags.WOODEN_DOORS)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_DOOR.get().asItem());
		tag(ItemTags.WOODEN_SLABS)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_SLAB.get().asItem());
		tag(ItemTags.LOGS)
		.add(GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get().asItem())
		.add(GrowthcraftApplesBlocks.APPLE_WOOD_LOG_STRIPPED.get().asItem())
		.add(GrowthcraftApplesBlocks.APPLE_WOOD.get().asItem())
		.add(GrowthcraftApplesBlocks.APPLE_WOOD_STRIPPED.get().asItem())
		.add(GrowthcraftCellarBlocks.CORK_WOOD_LOG.get().asItem())
		.add(GrowthcraftCellarBlocks.CORK_WOOD_LOG_STRIPPED.get().asItem())
		.add(GrowthcraftCellarBlocks.CORK_WOOD.get().asItem())
		.add(GrowthcraftCellarBlocks.CORK_WOOD_STRIPPED.get().asItem());
		tag(ItemTags.WOODEN_STAIRS)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_STAIRS.get().asItem());
		tag(ItemTags.WOODEN_FENCES)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_FENCE.get().asItem());
		tag(Tags.Items.FENCE_GATES_WOODEN)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_FENCE_GATE.get().asItem());
		tag(ItemTags.WOODEN_PRESSURE_PLATES)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_PRESSURE_PLATE.get().asItem());
		tag(ItemTags.WOODEN_TRAPDOORS)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_TRAPDOOR.get().asItem());
		tag(ItemTags.LEAVES)
		.add(GrowthcraftApplesBlocks.APPLE_TREE_LEAVES.get().asItem());
		tag(ItemTags.LOGS_THAT_BURN)
		.add(GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get().asItem())
		.add(GrowthcraftApplesBlocks.APPLE_WOOD_LOG_STRIPPED.get().asItem())
		.add(GrowthcraftApplesBlocks.APPLE_WOOD.get().asItem())
		.add(GrowthcraftApplesBlocks.APPLE_WOOD_STRIPPED.get().asItem())
		.add(GrowthcraftCellarBlocks.CORK_WOOD_LOG.get().asItem())
		.add(GrowthcraftCellarBlocks.CORK_WOOD_LOG_STRIPPED.get().asItem())
		.add(GrowthcraftCellarBlocks.CORK_WOOD.get().asItem())
		.add(GrowthcraftCellarBlocks.CORK_WOOD_STRIPPED.get().asItem());
		
		GrowthcraftCellarItems.ITEMS.getEntries().forEach(grain -> {
			if (grain.getId().getPath().contains("grain") && ! grain.equals(GrowthcraftCellarItems.GRAIN)) {
				tag(GrowthcraftCellarTags.Items.TAG_ROASTED_GRAIN)
				.add(grain.get());
			}
		});
		//Forge Tags
		tag(GrowthcraftTags.Items.DUSTS_SALT)
		.add(GrowthcraftItems.SALT.get());
		tag(GrowthcraftRiceTags.Items.TAG_CROPS_RICE)
		.add(GrowthcraftRiceItems.RICE.get());
		tag(GrowthcraftRiceTags.Items.TAG_GRAIN_RICE)
		.add(GrowthcraftRiceItems.RICE.get());
		tag(GrowthcraftRiceTags.Items.TAG_SEEDS_RICE)
		.add(GrowthcraftRiceItems.RICE_GRAINS.get());
		tag(GrowthcraftTags.Items.TAG_KNIFE)
		.add(GrowthcraftRiceItems.KNIFE.get());
		
		//SereneSeasons Tags
		tag(GrowthcraftTags.Items.SPRING_CROPS)
		.add(GrowthcraftCellarItems.GRAPE_PURPLE_SEED.get())
		.add(GrowthcraftCellarItems.GRAPE_RED_SEEDS.get())
		.add(GrowthcraftCellarItems.GRAPE_WHITE_SEEDS.get())
		.add(GrowthcraftCellarItems.HOPS_SEED.get())
		.add(GrowthcraftApplesItems.APPLE_SEEDS.get());
		
		tag(GrowthcraftTags.Items.SUMMER_CROPS)
		.add(GrowthcraftCellarItems.GRAPE_PURPLE_SEED.get())
		.add(GrowthcraftCellarItems.GRAPE_RED_SEEDS.get())
		.add(GrowthcraftCellarItems.GRAPE_WHITE_SEEDS.get())
		.add(GrowthcraftCellarItems.HOPS_SEED.get())
		.add(GrowthcraftMilkItems.THISTLE_SEED.get())
		.add(GrowthcraftRiceItems.RICE_STALK.get())
		.add(GrowthcraftApplesItems.APPLE_SEEDS.get());
		
		tag(GrowthcraftTags.Items.AUTUMN_CROPS)
		.add(GrowthcraftMilkItems.THISTLE_SEED.get())
		.add(GrowthcraftRiceItems.RICE_STALK.get());

		
		tag(GrowthcraftTags.Items.WINTER_CROPS);
	}

    @Override
    public String getName() {
        return "Growthcraft Core Item Tags";
    }
}
