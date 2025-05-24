package growthcraft.cellar.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.recipe.*;
import growthcraft.cellar.shared.Reference;
import growthcraft.core.init.GrowthcraftItems;
import growthcraft.lib.item.GrowthcraftItem;
import growthcraft.rice.init.GrowthcraftRiceBlocks;
import growthcraft.rice.init.GrowthcraftRiceItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EmiEntrypoint
public class EmiPlugin implements dev.emi.emi.api.EmiPlugin
{
    @Override
    public void register(EmiRegistry emiRegistry) {
        // press
        emiRegistry.addCategory(PRESS_RECIPE_CATEGORY);
        emiRegistry.addWorkstation(PRESS_RECIPE_CATEGORY, PRESS_RECIPE_WORKSTATION);
        for (FruitPressRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(FruitPressRecipe.Type.INSTANCE)) {
            emiRegistry.addRecipe(new EmiPressRecipe(recipe));
        }
        // barrel
        emiRegistry.addCategory(BARREL_RECIPE_CATEGORY);
        emiRegistry.addWorkstation(BARREL_RECIPE_CATEGORY, BARREL_RECIPE_WORKSTATION);
        for (FermentationBarrelRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(FermentationBarrelRecipe.Type.INSTANCE)) {
            emiRegistry.addRecipe(new EmiBarrelRecipe(recipe));
        }
        // kettle
        emiRegistry.addCategory(KETTLE_RECIPE_CATEGORY);
        emiRegistry.addWorkstation(KETTLE_RECIPE_CATEGORY, KETTLE_RECIPE_WORKSTATION);
        for (BrewKettleRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(BrewKettleRecipe.Type.INSTANCE)) {
            emiRegistry.addRecipe(new EmiKettleRecipe(recipe));  // maybe differentiate brewing and steaming later in the recipe class
        }
        // roaster
        emiRegistry.addCategory(ROASTER_RECIPE_CATEGORY);
        emiRegistry.addWorkstation(ROASTER_RECIPE_CATEGORY, ROASTER_RECIPE_WORKSTATION);
        for (RoasterRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(RoasterRecipe.Type.INSTANCE)) {
            emiRegistry.addRecipe(new EmiRoasterRecipe(recipe));
        }
        // jar
        emiRegistry.addCategory(JAR_RECIPE_CATEGORY1);
        emiRegistry.addCategory(JAR_RECIPE_CATEGORY2);
        emiRegistry.addWorkstation(JAR_RECIPE_CATEGORY1, JAR_RECIPE_WORKSTATION);
        emiRegistry.addWorkstation(JAR_RECIPE_CATEGORY2, JAR_RECIPE_WORKSTATION);
        for (CultureJarStarterRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(CultureJarStarterRecipe.Type.INSTANCE)) {
            emiRegistry.addRecipe(new EmiJarRecipe1(recipe));
        }
        for (CultureJarRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(CultureJarRecipe.Type.INSTANCE)) {
            emiRegistry.addRecipe(new EmiJarRecipe2(recipe));
        }
        // world interaction
        emiRegistry.addRecipe(new EmiFarmingRecipe(GrowthcraftCellarItems.GRAPE_RED_SEEDS.get(), GrowthcraftCellarItems.GRAPE_RED.get()));
        emiRegistry.addRecipe(new EmiFarmingRecipe(GrowthcraftCellarItems.GRAPE_PURPLE_SEED.get(), GrowthcraftCellarItems.GRAPE_PURPLE.get()));
        emiRegistry.addRecipe(new EmiFarmingRecipe(GrowthcraftCellarItems.GRAPE_WHITE_SEEDS.get(), GrowthcraftCellarItems.GRAPE_WHITE.get()));
        emiRegistry.addRecipe(new EmiFarmingRecipe(GrowthcraftCellarItems.HOPS_SEED.get(), GrowthcraftCellarItems.HOPS.get()));
        // manual book. maybe we should just delete this...
        addBookRecipe(emiRegistry, Blocks.GRASS_BLOCK, GrowthcraftCellarItems.YEAST_BAYANUS.get(), GrowthcraftCellarItems.YEAST_BREWERS.get(), GrowthcraftCellarItems.YEAST_LAGER.get());
        // add bottles. not a complete explanation but simple to add.
        addBottles(emiRegistry, GrowthcraftCellarItems.POTION_WINE.get(), GrowthcraftCellarItems.POTION_ALE.get(), GrowthcraftCellarItems.POTION_LAGER.get());
    }

    private static final EmiStack PRESS_RECIPE_WORKSTATION = EmiStack.of(GrowthcraftCellarBlocks.FRUIT_PRESS.get());
    private static final EmiStack BARREL_RECIPE_WORKSTATION = EmiStack.of(GrowthcraftCellarBlocks.FERMENTATION_BARREL_OAK.get());
    private static final EmiStack KETTLE_RECIPE_WORKSTATION = EmiStack.of(GrowthcraftCellarBlocks.BREW_KETTLE.get());
    private static final EmiStack JAR_RECIPE_WORKSTATION = EmiStack.of(GrowthcraftCellarBlocks.CULTURE_JAR.get());
    private static final EmiStack ROASTER_RECIPE_WORKSTATION = EmiStack.of(GrowthcraftCellarBlocks.ROASTER.get());

    private static final ResourceLocation DUMMY_SPRITE_LOCATION = new ResourceLocation("emi", "textures/gui/widgets.png");  // for the tree-screen. i won't bother using separate icons now.
    private static final EmiTexture DUMMY_SPRITE = new EmiTexture(DUMMY_SPRITE_LOCATION, 64, 148, 16, 16);  // for the tree-screen. i won't bother using separate icons now.

    public static final EmiRecipeCategory PRESS_RECIPE_CATEGORY = new EmiRecipeCategory(new ResourceLocation(Reference.MODID, "press"), PRESS_RECIPE_WORKSTATION, DUMMY_SPRITE);
    public static final EmiRecipeCategory BARREL_RECIPE_CATEGORY = new EmiRecipeCategory(new ResourceLocation(Reference.MODID, "barrel"), BARREL_RECIPE_WORKSTATION, DUMMY_SPRITE);
    public static final EmiRecipeCategory KETTLE_RECIPE_CATEGORY = new EmiRecipeCategory(new ResourceLocation(Reference.MODID, "kettle"), KETTLE_RECIPE_WORKSTATION, DUMMY_SPRITE);
    public static final EmiRecipeCategory JAR_RECIPE_CATEGORY1 = new EmiRecipeCategory(new ResourceLocation(Reference.MODID, "jar1"), JAR_RECIPE_WORKSTATION, DUMMY_SPRITE);
    public static final EmiRecipeCategory JAR_RECIPE_CATEGORY2 = new EmiRecipeCategory(new ResourceLocation(Reference.MODID, "jar2"), JAR_RECIPE_WORKSTATION, DUMMY_SPRITE);
    public static final EmiRecipeCategory ROASTER_RECIPE_CATEGORY = new EmiRecipeCategory(new ResourceLocation(Reference.MODID, "roaster"), ROASTER_RECIPE_WORKSTATION, DUMMY_SPRITE);

    // simple recipes telling player to read the book. unusual but we could make it work. maybe.
    private void addBookRecipe(EmiRegistry emiRegistry, ItemLike baseBlock, ItemLike... items)
    {
        Item book = ForgeRegistries.ITEMS.getValue(new ResourceLocation("patchouli:guide_book"));
        if (book == null) { return; }
        ItemStack bookStack = book.getDefaultInstance();
        bookStack.getOrCreateTag().putString("patchouli:book", "growthcraft:growthcraft");
        int counter = 1;
        for (ItemLike item: items)
        {
            emiRegistry.addRecipe(EmiWorldInteractionRecipe.builder()
                                                           .leftInput(EmiIngredient.of(Ingredient.of(baseBlock)))
                                                           .rightInput(EmiIngredient.of(Ingredient.of(bookStack)), true)
                                                           .output(EmiStack.of(item))
                                                           .id(new ResourceLocation(growthcraft.cellar.shared.Reference.MODID, "book_recipe_" + (counter++)))
                                                           .build());
        }
    }

    private void addBottles(EmiRegistry emiRegistry, Item... bottles)
    {
        int counter = 1;
        for (ItemLike bottle: bottles)
        {
            emiRegistry.addRecipe(EmiWorldInteractionRecipe.builder()
                                                           .leftInput(EmiIngredient.of(Ingredient.of(GrowthcraftCellarBlocks.FERMENTATION_BARREL_OAK.get())))
                                                           .rightInput(EmiIngredient.of(Ingredient.of(Items.GLASS_BOTTLE)), false)
                                                           .output(EmiStack.of(bottle))
                                                           .id(new ResourceLocation(growthcraft.cellar.shared.Reference.MODID, "bottle_recipe_" + (counter++)))
                                                           .build());
        }
    }
}
