package growthcraft.milk.init;

import growthcraft.lib.utils.CheeseUtils;
import growthcraft.milk.block.*;
import growthcraft.milk.block.CheeseWheelWaxableBlock;
import growthcraft.milk.block.signs.HangingSignBlock1;
import growthcraft.milk.block.signs.HangingSignBlock2;
import growthcraft.milk.item.CheeseCurdsBlockItem;
import growthcraft.milk.shared.Reference;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class GrowthcraftMilkBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            ForgeRegistries.BLOCKS, Reference.MODID
    );

    public static final RegistryObject<Block> CHEESE_PRESS = registerBlock(
            Reference.UnlocalizedName.CHEESE_PRESS,
            CheesePressBlock::new
    );

    public static final RegistryObject<Block> CHURN = registerBlock(
        Reference.UnlocalizedName.CHURN,
            ChurnBlock::new
    );

    public static final RegistryObject<Block> MIXING_VAT = registerBlock(
            Reference.UnlocalizedName.MIXING_VAT,
            MixingVatBlock::new
    );

    public static final RegistryObject<Block> PANCHEON = registerBlock(
            Reference.UnlocalizedName.PANCHEON,
            PancheonBlock::new
    );

    public static final RegistryObject<Block> THISTLE_CROP = registerBlock(
            Reference.UnlocalizedName.THISTLE_CROP,
            ThistleCropBlock::new,
            true
    );

    // WAXABLE CHEESES
    public static final RegistryObject<Block> CHEDDAR_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.CHEDDAR).get(CheeseUtils.CHEESE),
            () -> new CheeseWheelWaxableBlock(BaseCheeseWheel.Cheese.CHEDDAR)
    );
    public static final RegistryObject<Block> GOUDA_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.GOUDA).get(CheeseUtils.CHEESE),
            () -> new CheeseWheelWaxableBlock(BaseCheeseWheel.Cheese.GOUDA)
    );
    public static final RegistryObject<Block> MONTEREY_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.MONTEREY).get(CheeseUtils.CHEESE),
            () -> new CheeseWheelWaxableBlock(BaseCheeseWheel.Cheese.MONTEREY)
    );
    public static final RegistryObject<Block> PROVOLONE_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.PROVOLONE).get(CheeseUtils.CHEESE),
            () -> new CheeseWheelWaxableBlock(BaseCheeseWheel.Cheese.PROVOLONE)
    );


    // AGEABLE CHEESES
    public static final RegistryObject<Block> APPENZELLER_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.APPENZELLER).get(CheeseUtils.CHEESE),
            () -> new CheeseWheelAgeableBlock(BaseCheeseWheel.Cheese.APPENZELLER)
    );

    public static final RegistryObject<Block> ASIAGO_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.ASIAGO).get(CheeseUtils.CHEESE),
            () -> new CheeseWheelAgeableBlock(BaseCheeseWheel.Cheese.ASIAGO)
    );

    public static final RegistryObject<Block> CASU_MARZU_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.CASU_MARZU).get(CheeseUtils.CHEESE),
            () -> new CheeseWheelAgeableBlock(BaseCheeseWheel.Cheese.CASU_MARZU)
    );

    public static final RegistryObject<Block> WAXED_CHEDDAR_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.CHEDDAR).get(CheeseUtils.WAXED),
            () -> new CheeseWheelAgeableBlock(BaseCheeseWheel.Cheese.CHEDDAR)
    );

    public static final RegistryObject<Block> EMMENTALER_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.EMMENTALER).get(CheeseUtils.CHEESE),
            () -> new CheeseWheelAgeableBlock(BaseCheeseWheel.Cheese.EMMENTALER)
    );

    public static final RegistryObject<Block> GORGONZOLA_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.GORGONZOLA).get(CheeseUtils.CHEESE),
            () -> new CheeseWheelAgeableBlock(BaseCheeseWheel.Cheese.GORGONZOLA)
    );

    public static final RegistryObject<Block> WAXED_GOUDA_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.GOUDA).get(CheeseUtils.WAXED),
            () -> new CheeseWheelAgeableBlock(BaseCheeseWheel.Cheese.GOUDA)
    );

    public static final RegistryObject<Block> WAXED_MONTEREY_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.MONTEREY).get(CheeseUtils.WAXED),
            () -> new CheeseWheelAgeableBlock(BaseCheeseWheel.Cheese.MONTEREY)
    );

    public static final RegistryObject<Block> PARMESAN_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.PARMESAN).get(CheeseUtils.CHEESE),
            () -> new CheeseWheelAgeableBlock(BaseCheeseWheel.Cheese.PARMESAN)
    );

    public static final RegistryObject<Block> WAXED_PROVOLONE_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.PROVOLONE).get(CheeseUtils.WAXED),
            () -> new CheeseWheelAgeableBlock(BaseCheeseWheel.Cheese.PROVOLONE)
    );

    /////////////////////////////////////////////////////
    /////////////// PROCESSED CHEESES ///////////////////
    /////////////////////////////////////////////////////

    public static final RegistryObject<Block> AGED_APPENZELLER_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.APPENZELLER).get(CheeseUtils.AGED),
            () -> new CheeseWheelProcessedBlock(BaseCheeseWheel.Cheese.APPENZELLER)
    );

    public static final RegistryObject<Block> AGED_ASIAGO_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.ASIAGO).get(CheeseUtils.AGED),
            () -> new CheeseWheelProcessedBlock(BaseCheeseWheel.Cheese.ASIAGO)
    );

    public static final RegistryObject<Block> AGED_CASU_MARZU_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.CASU_MARZU).get(CheeseUtils.AGED),
            () -> new CheeseWheelProcessedBlock(BaseCheeseWheel.Cheese.CASU_MARZU)
    );

    public static final RegistryObject<Block> AGED_CHEDDAR_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.CHEDDAR).get(CheeseUtils.AGED),
            () -> new CheeseWheelProcessedBlock(BaseCheeseWheel.Cheese.CHEDDAR)
    );

    public static final RegistryObject<Block> AGED_EMMENTALER_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.EMMENTALER).get(CheeseUtils.AGED),
            () -> new CheeseWheelProcessedBlock(BaseCheeseWheel.Cheese.EMMENTALER)
    );

    public static final RegistryObject<Block> AGED_GORGONZOLA_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.GORGONZOLA).get(CheeseUtils.AGED),
            () -> new CheeseWheelProcessedBlock(BaseCheeseWheel.Cheese.GORGONZOLA)
    );

    public static final RegistryObject<Block> AGED_GOUDA_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.GOUDA).get(CheeseUtils.AGED),
            () -> new CheeseWheelProcessedBlock(BaseCheeseWheel.Cheese.GOUDA)
    );

    public static final RegistryObject<Block> AGED_MONTEREY_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.MONTEREY).get(CheeseUtils.AGED),
            () -> new CheeseWheelProcessedBlock(BaseCheeseWheel.Cheese.MONTEREY)
    );

    public static final RegistryObject<Block> AGED_PARMESAN_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.PARMESAN).get(CheeseUtils.AGED),
            () -> new CheeseWheelProcessedBlock(BaseCheeseWheel.Cheese.PARMESAN)
    );

    public static final RegistryObject<Block> AGED_PROVOLONE_CHEESE = registerBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.PROVOLONE).get(CheeseUtils.AGED),
            () -> new CheeseWheelProcessedBlock(BaseCheeseWheel.Cheese.PROVOLONE)
    );

    //////////////////////////////////
    /////////// Cheese Curds /////////
    //////////////////////////////////


    public static final RegistryObject<Block> APPENZELLER_CHEESE_CURDS = registerCheseCurdsBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.APPENZELLER).get(CheeseUtils.CURDS),
            () -> new CheeseCurdBlock(Reference.ItemColor.APPENZELLER_CHEESE.getColor())
    );

    public static final RegistryObject<Block> ASIAGO_CHEESE_CURDS = registerCheseCurdsBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.ASIAGO).get(CheeseUtils.CURDS),
            () -> new CheeseCurdBlock(Reference.ItemColor.ASIAGO_CHEESE.getColor())
    );

    public static final RegistryObject<Block> CASU_MARZU_CHEESE_CURDS = registerCheseCurdsBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.CASU_MARZU).get(CheeseUtils.CURDS),
            () -> new CheeseCurdBlock(Reference.ItemColor.CASU_MARZU_CHEESE.getColor())
    );

    public static final RegistryObject<Block> CHEDDAR_CHEESE_CURDS = registerCheseCurdsBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.CHEDDAR).get(CheeseUtils.CURDS),
            () -> new CheeseCurdBlock(Reference.ItemColor.CHEDDAR_CHEESE.getColor())
    );

    public static final RegistryObject<Block> EMMENTALER_CHEESE_CURDS = registerCheseCurdsBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.EMMENTALER).get(CheeseUtils.CURDS),
            () -> new CheeseCurdBlock(Reference.ItemColor.EMMENTALER_CHEESE.getColor())
    );

    public static final RegistryObject<Block> GORGONZOLA_CHEESE_CURDS = registerCheseCurdsBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.GORGONZOLA).get(CheeseUtils.CURDS),
            () -> new CheeseCurdBlock(Reference.ItemColor.GORGONZOLA_CHEESE.getColor())
    );

    public static final RegistryObject<Block> GOUDA_CHEESE_CURDS = registerCheseCurdsBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.GOUDA).get(CheeseUtils.CURDS),
            () -> new CheeseCurdBlock(Reference.ItemColor.GOUDA_CHEESE.getColor())
    );

    public static final RegistryObject<Block> MONTEREY_CHEESE_CURDS = registerCheseCurdsBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.MONTEREY).get(CheeseUtils.CURDS),
            () -> new CheeseCurdBlock(Reference.ItemColor.MONTEREY_CHEESE.getColor())
    );

    public static final RegistryObject<Block> PARMESAN_CHEESE_CURDS = registerCheseCurdsBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.PARMESAN).get(CheeseUtils.CURDS),
            () -> new CheeseCurdBlock(Reference.ItemColor.PARMESAN_CHEESE.getColor())
    );

    public static final RegistryObject<Block> RICOTTA_CHEESE_CURDS = registerCheseCurdsBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.RICOTTA).get(CheeseUtils.CURDS),
            () -> new CheeseCurdBlock(Reference.ItemColor.RICOTTA_CHEESE.getColor())
    );

    public static final RegistryObject<Block> PROVOLONE_CHEESE_CURDS = registerCheseCurdsBlock(
            CheeseUtils.getCheeseNames(Reference.UnlocalizedName.PROVOLONE).get(CheeseUtils.CURDS),
            () -> new CheeseCurdBlock(Reference.ItemColor.PROVOLONE_CHEESE.getColor())
    );

    /////////// shop signs ////////////

    private static final RegistryObject<Block> SHOP_SIGN_1_OAK = registerBlock("hanging_sign_1_oak", () -> makeSign(Blocks.OAK_HANGING_SIGN), true);
    private static final RegistryObject<Block> SHOP_SIGN_2_OAK = registerBlock("hanging_sign_2_oak", () -> makeSign(Blocks.OAK_WALL_HANGING_SIGN), true);
    private static final RegistryObject<Block> SHOP_SIGN_1_SPRUCE = registerBlock("hanging_sign_1_spruce", () -> makeSign(Blocks.SPRUCE_HANGING_SIGN), true);
    private static final RegistryObject<Block> SHOP_SIGN_2_SPRUCE = registerBlock("hanging_sign_2_spruce", () -> makeSign(Blocks.SPRUCE_WALL_HANGING_SIGN), true);

    ///////////////////////////////////

    private static RegistryObject<Block> registerCheseCurdsBlock(String name, Supplier<Block> block) {
        RegistryObject<Block> registryObject = BLOCKS.register(name, block);
        GrowthcraftMilkItems.ITEMS.register(name, () -> new CheeseCurdsBlockItem(registryObject.get()));
        return registryObject;
    }

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> block) {
        return registerBlock(name, block, false);
    }

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> block, boolean excludeBlockItemRegistry) {
        RegistryObject<Block> registryObject = BLOCKS.register(name, block);
        if (!excludeBlockItemRegistry) {
            registerBlockItem(name, registryObject);
        }
        return registryObject;
    }

    private static void registerBlockItem(String name, RegistryObject<Block> blockRegistryObject) {
        GrowthcraftMilkItems.ITEMS.register(
                name,
                () -> new BlockItem(blockRegistryObject.get(), getDefaultItemProperties())
        );
    }

    private static Item.Properties getDefaultItemProperties() {
        Item.Properties properties = new Item.Properties();
        return properties;
    }

    //////// sign transforming support //////////////
    private static Block makeSign(Block original) {
        SignBlock originalCast = (SignBlock) original;
        Block ourBlock;
        if (! (original instanceof WallHangingSignBlock)) {
            ourBlock = new HangingSignBlock1(originalCast);
        }
        else {
            ourBlock = new HangingSignBlock2(originalCast);
        }
        signBlocksByOriginal.put(original, ourBlock);
        return ourBlock;
    }
    private static final Map<Block, Block> signBlocksByOriginal = new HashMap<>(); // for transformation

    public static Block getSignFromOriginal(Block original) {
        return signBlocksByOriginal.getOrDefault(original, null);
    }

    public static Block[] getSignsAsArray() { // for block entity
        return signBlocksByOriginal.values().toArray(new Block[0]);
    }

    /////////////////////////////////////////////////

    private GrowthcraftMilkBlocks() {
        /* Disable default public constructor */
    }
}
