package growthcraft.core.datagen.providers;

import java.util.concurrent.CompletableFuture;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import org.jetbrains.annotations.Nullable;

import growthcraft.apiary.init.GrowthcraftApiaryBlocks;
import growthcraft.apples.init.GrowthcraftApplesBlocks;
import growthcraft.apples.init.GrowthcraftApplesTags;
import growthcraft.bamboo.init.GrowthcraftBambooBlocks;
import growthcraft.bamboo.init.GrowthcraftBambooTags;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.core.init.GrowthcraftBlocks;
import growthcraft.core.init.GrowthcraftTags;
import growthcraft.core.shared.Reference;
import growthcraft.milk.init.GrowthcraftMilkBlocks;
import growthcraft.rice.init.GrowthcraftRiceBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class GrowthcraftCoreBlockTags extends BlockTagsProvider {

	public GrowthcraftCoreBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, Reference.MODID, existingFileHelper);
	}
	
	@Override 
	protected void addTags(HolderLookup.Provider provider) {
		//GC Tags
		GrowthcraftApiaryBlocks.BLOCKS.getEntries().forEach(block -> {
			tag(BlockTags.BEEHIVES).add(block.get());
			tag(BlockTags.MINEABLE_WITH_AXE).add(block.get());
		});
		
		tag(GrowthcraftApplesTags.Blocks.APPLE_WOOD_LOG)
		.add(GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get())
		.add(GrowthcraftApplesBlocks.APPLE_WOOD_LOG_STRIPPED.get());
		
		tag(GrowthcraftBambooTags.Blocks.BAMBOO_WOOD_LOGS)
		.add(GrowthcraftBambooBlocks.BAMBOO_WOOD_LOG.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_WOOD.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_WOOD_LOG_STRIPPED.get());
		tag(GrowthcraftBambooTags.Blocks.BAMBOO_WOOD)
		.add(GrowthcraftBambooBlocks.BAMBOO_WOOD_LOG.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_WOOD.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_WOOD_STRIPPED.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_WOOD_LOG_STRIPPED.get());
		
		tag(GrowthcraftTags.Blocks.HEATSOURCES)
		.add(Blocks.LAVA)
		.add(Blocks.MAGMA_BLOCK)
		.add(TagEntry.optionalElement(new ResourceLocation("farmersdelight", "stove")))
		.add(TagEntry.optionalElement(new ResourceLocation("decorative_blocks", "bonfire")))
		.add(TagEntry.optionalElement(new ResourceLocation("decorative_blocks", "brazier")))  // would have liked to support additional_lights but their firepits aren't tagged and there's a ton of them.
		.add(TagEntry.tag(BlockTags.CAMPFIRES.location()))
		.add(TagEntry.tag(BlockTags.FIRE.location()));

		tag(GrowthcraftTags.Blocks.ROPE)
		.add(GrowthcraftBlocks.ROPE_LINEN.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_ACACIA_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_BIRCH_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_CRIMSON_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_DARK_OAK_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_JUNGLE_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_NETHER_BRICK_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_OAK_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_SPRUCE_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_WARPED_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_BAMBOO_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_CHERRY_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_MANGROVE_FENCE.get())
		.add(GrowthcraftCellarBlocks.HOPS_VINE.get())
		.add(GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE.get())
		.add(GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE_LEAVES.get())
		.add(GrowthcraftCellarBlocks.RED_GRAPE_VINE.get())
		.add(GrowthcraftCellarBlocks.RED_GRAPE_VINE_LEAVES.get())
		.add(GrowthcraftCellarBlocks.WHITE_GRAPE_VINE.get())
		.add(GrowthcraftCellarBlocks.WHITE_GRAPE_VINE_LEAVES.get())
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_FENCE_ROPE_LINEN.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_PLANK_FENCE_ROPE_LINEN.get());
		tag(GrowthcraftTags.Blocks.SALT)
		.add(GrowthcraftBlocks.SALT_BLOCK.get());
		

		
		//Vanilla Tags
		tag(BlockTags.WOODEN_BUTTONS)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_BUTTON.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_PLANK_BUTTON.get());
		tag(BlockTags.PLANKS)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_PLANK.get());
		tag(BlockTags.WOODEN_DOORS)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_DOOR.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_PLANK_DOOR.get());
		tag(BlockTags.WOODEN_SLABS)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_SLAB.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_PLANK_SLAB.get());
		tag(BlockTags.LOGS)
		.add(GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get())
		.add(GrowthcraftApplesBlocks.APPLE_WOOD_LOG_STRIPPED.get())
		.add(GrowthcraftApplesBlocks.APPLE_WOOD.get())
		.add(GrowthcraftApplesBlocks.APPLE_WOOD_STRIPPED.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_WOOD.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_WOOD_STRIPPED.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_WOOD_LOG.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_WOOD_LOG_STRIPPED.get());
		tag(BlockTags.WOODEN_STAIRS)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_STAIRS.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_PLANK_STAIRS.get());
		tag(BlockTags.WOODEN_FENCES)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_FENCE.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_PLANK_FENCE.get());
		tag(BlockTags.FENCE_GATES)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_FENCE_GATE.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_PLANK_FENCE_GATE.get());
		tag(BlockTags.WOODEN_PRESSURE_PLATES)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_PRESSURE_PLATE.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_PLANK_PRESSURE_PLATE.get());
		tag(BlockTags.WOODEN_TRAPDOORS)
		.add(GrowthcraftApplesBlocks.APPLE_PLANK_TRAPDOOR.get())
		.add(GrowthcraftBambooBlocks.BAMBOO_PLANK_TRAPDOOR.get());
		tag(BlockTags.LEAVES)
		.add(GrowthcraftApplesBlocks.APPLE_TREE_LEAVES.get());
		tag(BlockTags.MINEABLE_WITH_AXE)
		.add(GrowthcraftBambooBlocks.BAMBOO_PLANK_BEE_BOX.get())
		.add(GrowthcraftCellarBlocks.FERMENTATION_BARREL_OAK.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_ACACIA_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_BIRCH_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_CRIMSON_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_DARK_OAK_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_JUNGLE_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_NETHER_BRICK_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_OAK_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_SPRUCE_FENCE.get())
		.add(GrowthcraftBlocks.ROPE_LINEN_WARPED_FENCE.get())
		.add(GrowthcraftMilkBlocks.CHURN.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE)
		.add(GrowthcraftCellarBlocks.BREW_KETTLE.get())
		.add(GrowthcraftCellarBlocks.FRUIT_PRESS.get())
		.add(GrowthcraftCellarBlocks.FRUIT_PRESS_PISTON.get())
		.add(GrowthcraftCellarBlocks.ROASTER.get())
		.add(GrowthcraftBlocks.SALT_ORE.get())
		.add(GrowthcraftBlocks.SALT_ORE_DEEPSLATE.get())
		.add(GrowthcraftBlocks.SALT_ORE_END.get())
		.add(GrowthcraftBlocks.SALT_ORE_NETHER.get())
		.add(GrowthcraftMilkBlocks.CHEESE_PRESS.get())
		.add(GrowthcraftMilkBlocks.MIXING_VAT.get())
		.add(GrowthcraftMilkBlocks.PANCHEON.get());
		tag(BlockTags.MINEABLE_WITH_SHOVEL)
		.add(GrowthcraftRiceBlocks.CULTIVATED_FARMLAND.get());
		tag(BlockTags.NEEDS_STONE_TOOL)
		.add(GrowthcraftBlocks.SALT_ORE.get())
		.add(GrowthcraftBlocks.SALT_ORE_DEEPSLATE.get())
		.add(GrowthcraftBlocks.SALT_ORE_END.get())
		.add(GrowthcraftBlocks.SALT_ORE_NETHER.get());
		
		//Forge Tags
		tag(Tags.Blocks.ORES)
		.add(GrowthcraftBlocks.SALT_ORE.get())
		.add(GrowthcraftBlocks.SALT_ORE_DEEPSLATE.get())
		.add(GrowthcraftBlocks.SALT_ORE_NETHER.get())
		.add(GrowthcraftBlocks.SALT_ORE_END.get());
		tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE)
		.add(GrowthcraftBlocks.SALT_ORE_DEEPSLATE.get());
		tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK)
		.add(GrowthcraftBlocks.SALT_ORE_NETHER.get());
		tag(Tags.Blocks.ORES_IN_GROUND_STONE)
		.add(GrowthcraftBlocks.SALT_ORE.get());
		
	
	}

    @Override
    public String getName() {
        return "Growthcraft Core Block Tags";
    }
}
