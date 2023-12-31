package growthcraft.milk.datagen.providers;

import growthcraft.milk.init.GrowthcraftMilkBlocks;
import growthcraft.milk.shared.Reference;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static growthcraft.milk.block.BaseCheeseWheel.SLICE_COUNT_BOTTOM;
import static growthcraft.milk.block.BaseCheeseWheel.SLICE_COUNT_TOP;

public class GrowthcraftMilkBlockStateProvider extends BlockStateProvider {

    private final ModelFile empty_model = models().getExistingFile(new ResourceLocation("growthcraft", "block/empty"));

    public GrowthcraftMilkBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Reference.MODID, exFileHelper);
    }

    // templates
    private final List<ResourceLocation> lower = List.of(
            modLoc("block/cheese_wheel/cheese_slab_slices_bottom_1"),
            modLoc("block/cheese_wheel/cheese_slab_slices_bottom_2"),
            modLoc("block/cheese_wheel/cheese_slab_slices_bottom_3"),
            modLoc("block/cheese_wheel/cheese_slab_slices_bottom_4")
    );
    private final List<ResourceLocation> upper = List.of(
            modLoc("block/cheese_wheel/cheese_slab_slices_top_1"),
            modLoc("block/cheese_wheel/cheese_slab_slices_top_2"),
            modLoc("block/cheese_wheel/cheese_slab_slices_top_3"),
            modLoc("block/cheese_wheel/cheese_slab_slices_top_4")
    );

    private final ResourceLocation emptyModel = new ResourceLocation("growthcraft", "block/empty");

    @Override
    protected void registerStatesAndModels() {
        aged_cheese(GrowthcraftMilkBlocks.APPENZELLER_CHEESE, GrowthcraftMilkBlocks.AGED_APPENZELLER_CHEESE, "appenzeller");
        aged_cheese(GrowthcraftMilkBlocks.ASIAGO_CHEESE, GrowthcraftMilkBlocks.AGED_ASIAGO_CHEESE,  "asiago");
        aged_cheese(GrowthcraftMilkBlocks.CASU_MARZU_CHEESE, GrowthcraftMilkBlocks.AGED_CASU_MARZU_CHEESE, "casu_marzu");
        waxed_cheese(
                GrowthcraftMilkBlocks.CHEDDAR_CHEESE,
                GrowthcraftMilkBlocks.WAXED_CHEDDAR_CHEESE,
                GrowthcraftMilkBlocks.AGED_CHEDDAR_CHEESE,
                "cheddar"
        );
        aged_cheese(GrowthcraftMilkBlocks.EMMENTALER_CHEESE, GrowthcraftMilkBlocks.AGED_EMMENTALER_CHEESE, "emmentaler");
        aged_cheese(GrowthcraftMilkBlocks.GORGONZOLA_CHEESE, GrowthcraftMilkBlocks.AGED_GORGONZOLA_CHEESE, "gorgonzola");
        waxed_cheese(
                GrowthcraftMilkBlocks.GOUDA_CHEESE,
                GrowthcraftMilkBlocks.WAXED_GOUDA_CHEESE,
                GrowthcraftMilkBlocks.AGED_GOUDA_CHEESE,
                "gouda"
        );
        waxed_cheese(GrowthcraftMilkBlocks.MONTEREY_CHEESE,
                GrowthcraftMilkBlocks.WAXED_MONTEREY_CHEESE,
                GrowthcraftMilkBlocks.AGED_MONTEREY_CHEESE,
                "monterey"
        );
        aged_cheese(GrowthcraftMilkBlocks.PARMESAN_CHEESE, GrowthcraftMilkBlocks.AGED_PARMESAN_CHEESE, "parmesan");
        waxed_cheese(
                GrowthcraftMilkBlocks.PROVOLONE_CHEESE,
                GrowthcraftMilkBlocks.WAXED_PROVOLONE_CHEESE,
                GrowthcraftMilkBlocks.AGED_PROVOLONE_CHEESE,
                "provolone"
        );
    }

    private void cheese(Block block, String modelName, String sideTexture, String topTexture) {
        // Generate block models for the cheese
        cheeseWheelModel(lower, modelName, "_lower_", topTexture, sideTexture);
        cheeseWheelModel(upper, modelName, "_upper_", topTexture, sideTexture);

        // Generate blockstates for the cheese
        horizontalBlock(block,
                state -> {
                    if (state.getValue(SLICE_COUNT_TOP) == 0 && state.getValue(SLICE_COUNT_BOTTOM) == 0) {
                        return empty_model;
                    } else if (state.getValue(SLICE_COUNT_TOP) == 0) {
                        return models().getExistingFile(modLoc("block/cheese_wheel/" + modelName + "_lower_" + state.getValue(SLICE_COUNT_BOTTOM)));
                    } else {
                        return models().getExistingFile(modLoc("block/cheese_wheel/" + modelName + "_upper_" + state.getValue(SLICE_COUNT_TOP)));
                    }
                }
        );


        // use block model
        //ModelFile model =  models().getExistingFile(modLoc("block/cheese_wheel/" + modelName + "_lower_4"));
        //simpleBlockItem(block, model);
    }

    private void cheeseWheelModel(List<ResourceLocation> sliceModels, String modelName, String slabType, String topTexture, String sideTexture) {
        for (int sliceIndex = 0; sliceIndex < sliceModels.size(); sliceIndex++) {
            models().withExistingParent("block/cheese_wheel/" + modelName + slabType + (sliceIndex + 1), sliceModels.get(sliceIndex))
                    .texture("0", "block/cheese/" + topTexture)
                    .texture("1", "block/cheese/" + sideTexture)
                    .texture("particle", "block/cheese/" + topTexture);
        }
    }

    private ModelFile cheeseItemModel(RegistryObject<Block> block, String itemTexture) {
        return itemModels().getBuilder(block.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc("item/cheese/" + itemTexture));
    }

    private ModelFile cheeseItemModel(RegistryObject<Block> block, String itemTexture, String slicedTexture) {
        ModelFile sliced_model = itemModels().getBuilder(slicedTexture)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc("item/cheese/" + slicedTexture));

        return itemModels().getBuilder(block.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc("item/cheese/" + itemTexture))
                .override()
                .predicate(modLoc("cheese_sliced"), 1)
                .model(sliced_model)
                .end();
    }


    private void aged_cheese(RegistryObject<Block> unagedBlock, RegistryObject<Block> agedBlock, String modelName) {
        cheese(unagedBlock.get(),modelName + "_unaged", modelName + "_unaged_side", modelName + "_unaged_top");
        cheeseItemModel(unagedBlock, modelName + "_unaged");

        cheese(agedBlock.get(),modelName + "_aged", modelName + "_aged_side", modelName + "_aged_top");
        cheeseItemModel(agedBlock, modelName + "_aged", modelName + "_cut");
    }

    private void waxed_cheese(RegistryObject<Block> unwaxedBlock, RegistryObject<Block> waxedBlock,
                              RegistryObject<Block> agedBlock, String modelName) {
        cheese(unwaxedBlock.get(),modelName + "_unwaxed", modelName + "_unwaxed_side", modelName + "_unwaxed_top");
        cheeseItemModel(unwaxedBlock, modelName + "_unwaxed");

        cheese(waxedBlock.get(),modelName + "_waxed", modelName + "_waxed_side", modelName + "_waxed_top");
        cheeseItemModel(waxedBlock, modelName + "_waxed");

        cheese(agedBlock.get(),modelName + "_aged", modelName + "_waxed_side", modelName + "_waxed_top");
        cheeseItemModel(agedBlock, modelName + "_waxed", modelName + "_cut");
    }


    /**
     * Generates the blockstate file for a cheese using a multipart file for separate control over the top and bottom
     * wheel of cheese
     * Kept here in case we ever want to datagen the old style of cheese blockstates
     * @param lowerModels List of 4 ModelFiles for each stage of the lower wheel of cheese
     * @param upperModels List of 4 Modelfiles for each stage of the upper wheel of cheese
     */
    @SuppressWarnings("unused")
    private void generateMultipartCheese(List<ModelFile> lowerModels, List<ModelFile> upperModels) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(GrowthcraftMilkBlocks.WAXED_PROVOLONE_CHEESE.get());

        builder.part()
                .modelFile(models().getExistingFile(emptyModel))
                .addModel()
                .useOr()
                .condition(SLICE_COUNT_TOP, 0)
                .condition(SLICE_COUNT_BOTTOM, 0)
                .end();

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            for (ModelFile model : lowerModels) {
                builder.part()
                        .modelFile(model)
                        .rotationY((int) direction.toYRot())
                        .addModel()
                        .condition(BlockStateProperties.HORIZONTAL_FACING, direction)
                        .condition(SLICE_COUNT_BOTTOM, lowerModels.indexOf(model) + 1)
                        .end();
            }
            for (ModelFile model : upperModels) {
                builder.part()
                        .modelFile(model)
                        .rotationY((int) direction.toYRot())
                        .addModel()
                        .condition(BlockStateProperties.HORIZONTAL_FACING, direction)
                        .condition(SLICE_COUNT_TOP, upperModels.indexOf(model) + 1)
                        .end();
            }
        }
    }
}
