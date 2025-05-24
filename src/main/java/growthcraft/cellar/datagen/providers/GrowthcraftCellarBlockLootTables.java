package growthcraft.cellar.datagen.providers;


import java.util.List;
import java.util.stream.Collectors;

import growthcraft.apples.init.GrowthcraftApplesBlocks;
import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarFluids;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.lib.utils.FormatUtils;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;

public class GrowthcraftCellarBlockLootTables extends VanillaBlockLoot{
	
	@Override
	protected void generate() {
		createStandardTable(GrowthcraftCellarBlocks.BREW_KETTLE.get(), GrowthcraftCellarBlockEntities.BREW_KETTLE_BLOCK_ENTITY.get(), "inventory", "fluid_tank_input_0", "fluid_tank_output_0");
		createStandardTable(GrowthcraftCellarBlocks.CULTURE_JAR.get(), GrowthcraftCellarBlockEntities.CULTURE_JAR_BLOCK_ENTITY.get(), "fluid_tank_input_0");
		createStandardTable(GrowthcraftCellarBlocks.FERMENTATION_BARREL_OAK.get(), GrowthcraftCellarBlockEntities.FERMENTATION_BARREL_BLOCK_ENTITY.get(), "fluid_tank_input_0");
		createStandardTable(GrowthcraftCellarBlocks.FRUIT_PRESS.get(), GrowthcraftCellarBlockEntities.FRUIT_PRESS_BLOCK_ENTITY.get(), "fluid_tank_input_0");
		createStandardTable(GrowthcraftCellarBlocks.FRUIT_PRESS_PISTON.get(), GrowthcraftCellarBlockEntities.FRUIT_PRESS_BLOCK_ENTITY.get(), "fluid_tank_input_0");
		createStandardTable(GrowthcraftCellarBlocks.ROASTER.get(), GrowthcraftCellarBlockEntities.ROASTER_BLOCK_ENTITY.get(), "inventory");
		add(GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE_LEAVES.get(), createMangroveLeavesDrops(GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE_LEAVES.get()));
		add(GrowthcraftCellarBlocks.RED_GRAPE_VINE_LEAVES.get(), createMangroveLeavesDrops(GrowthcraftCellarBlocks.RED_GRAPE_VINE_LEAVES.get()));
		add(GrowthcraftCellarBlocks.WHITE_GRAPE_VINE_LEAVES.get(), createMangroveLeavesDrops(GrowthcraftCellarBlocks.WHITE_GRAPE_VINE_LEAVES.get()));
		add(GrowthcraftCellarBlocks.CORK_TREE_LEAVES.get(), createLeavesDrops(GrowthcraftCellarBlocks.CORK_TREE_LEAVES.get(), GrowthcraftCellarBlocks.CORK_TREE_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
		dropSelf(GrowthcraftCellarBlocks.CORK_TREE_SAPLING.get());
		dropSelf(GrowthcraftCellarBlocks.CORK_WOOD.get());
		dropSelf(GrowthcraftCellarBlocks.CORK_WOOD_LOG.get());
		dropSelf(GrowthcraftCellarBlocks.CORK_WOOD_LOG_STRIPPED.get());
		dropSelf(GrowthcraftCellarBlocks.CORK_WOOD_STRIPPED.get());
		dropSelf(GrowthcraftCellarBlocks.CORK_COASTER.get());
		dropOther(GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE.get(), GrowthcraftCellarItems.GRAPE_PURPLE_SEED.get());
		dropOther(GrowthcraftCellarBlocks.WHITE_GRAPE_VINE.get(), GrowthcraftCellarItems.GRAPE_WHITE_SEEDS.get());
		dropOther(GrowthcraftCellarBlocks.RED_GRAPE_VINE.get(), GrowthcraftCellarItems.GRAPE_RED_SEEDS.get());
	}
	
    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> allBlocks = GrowthcraftCellarBlocks.BLOCKS.getEntries().stream()
                .filter(e -> !e.getKey().location().getPath().contains("fluid"))
                .map(RegistryObject::get)
                .collect(Collectors.toList());

        // Exclude any blocks that do not need loot table generator.
        allBlocks.remove(GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE_FRUIT.get());
        allBlocks.remove(GrowthcraftCellarBlocks.RED_GRAPE_VINE_FRUIT.get());
        allBlocks.remove(GrowthcraftCellarBlocks.WHITE_GRAPE_VINE_FRUIT.get());
        allBlocks.remove(GrowthcraftCellarBlocks.HOPS_VINE.get());
        allBlocks.removeAll(GrowthcraftCellarFluids.FLUIDS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList()));

        return allBlocks;
    }

    /**
     * Creates a standard loot table for a block with optional additional tags.
     *
     * @param block The block to create a loot table for
     * @param type The block entity type associated with the block
     * @param tags Additional tags to be applied to the loot table
     */
    private void createStandardTable(Block block, BlockEntityType<?> type, String... tags) {
        LootPoolSingletonContainer.Builder<?> lti = LootItem.lootTableItem(block);
        lti.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY));
        for (String tag : tags) {
            lti.apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy(tag, "BlockEntityTag." + tag, CopyNbtFunction.MergeStrategy.REPLACE));
        }
        lti.apply(SetContainerContents.setContents(type).withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("minecraft", "contents"))));

        LootPool.Builder builder = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(lti);
        add(block, LootTable.lootTable().withPool(builder));
    }
}
