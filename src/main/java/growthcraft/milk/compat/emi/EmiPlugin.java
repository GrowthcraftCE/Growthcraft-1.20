package growthcraft.milk.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import growthcraft.core.init.GrowthcraftTags;
import growthcraft.milk.block.BaseCheeseWheel;
import growthcraft.milk.init.GrowthcraftMilkBlocks;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.recipe.*;
import growthcraft.milk.shared.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.Tags;

@EmiEntrypoint
public class EmiPlugin implements dev.emi.emi.api.EmiPlugin {
    @Override
    public void register(EmiRegistry emiRegistry) {
        // pancheon
        emiRegistry.addCategory(PANCHEON_RECIPE_CATEGORY);
        emiRegistry.addWorkstation(PANCHEON_RECIPE_CATEGORY, PANCHEON_RECIPE_WORKSTATION);
        for (PancheonRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(PancheonRecipe.Type.INSTANCE)) {
            emiRegistry.addRecipe(new EmiPancheonRecipe(recipe));
        }
        // drying
        emiRegistry.addCategory(DRYING_RECIPE_CATEGORY);
        emiRegistry.addRecipe(new EmiCurdsRecipe(GrowthcraftMilkBlocks.ASIAGO_CHEESE_CURDS.get(), GrowthcraftMilkItems.ASIAGO_CHEESE_CURDS_DRAINED.get()));
        emiRegistry.addRecipe(new EmiCurdsRecipe(GrowthcraftMilkBlocks.APPENZELLER_CHEESE_CURDS.get(), GrowthcraftMilkItems.APPENZELLER_CHEESE_CURDS_DRAINED.get()));
        emiRegistry.addRecipe(new EmiCurdsRecipe(GrowthcraftMilkBlocks.CASU_MARZU_CHEESE_CURDS.get(), GrowthcraftMilkItems.CASU_MARZU_CHEESE_CURDS_DRAINED.get()));
        emiRegistry.addRecipe(new EmiCurdsRecipe(GrowthcraftMilkBlocks.CHEDDAR_CHEESE_CURDS.get(), GrowthcraftMilkItems.CHEDDAR_CHEESE_CURDS_DRAINED.get()));
        emiRegistry.addRecipe(new EmiCurdsRecipe(GrowthcraftMilkBlocks.EMMENTALER_CHEESE_CURDS.get(), GrowthcraftMilkItems.EMMENTALER_CHEESE_CURDS_DRAINED.get()));
        emiRegistry.addRecipe(new EmiCurdsRecipe(GrowthcraftMilkBlocks.GORGONZOLA_CHEESE_CURDS.get(), GrowthcraftMilkItems.GORGONZOLA_CHEESE_CURDS_DRAINED.get()));
        emiRegistry.addRecipe(new EmiCurdsRecipe(GrowthcraftMilkBlocks.GOUDA_CHEESE_CURDS.get(), GrowthcraftMilkItems.GOUDA_CHEESE_CURDS_DRAINED.get()));
        emiRegistry.addRecipe(new EmiCurdsRecipe(GrowthcraftMilkBlocks.MONTEREY_CHEESE_CURDS.get(), GrowthcraftMilkItems.MONTEREY_CHEESE_CURDS_DRAINED.get()));
        emiRegistry.addRecipe(new EmiCurdsRecipe(GrowthcraftMilkBlocks.PARMESAN_CHEESE_CURDS.get(), GrowthcraftMilkItems.PARMESAN_CHEESE_CURDS_DRAINED.get()));
        emiRegistry.addRecipe(new EmiCurdsRecipe(GrowthcraftMilkBlocks.PROVOLONE_CHEESE_CURDS.get(), GrowthcraftMilkItems.PROVOLONE_CHEESE_CURDS_DRAINED.get()));
        emiRegistry.addRecipe(new EmiCurdsRecipe(GrowthcraftMilkBlocks.RICOTTA_CHEESE_CURDS.get(), GrowthcraftMilkItems.RICOTTA_CHEESE_CURDS_DRAINED.get()));
        // churn
        emiRegistry.addCategory(CHURN_RECIPE_CATEGORY);
        emiRegistry.addWorkstation(CHURN_RECIPE_CATEGORY, CHURN_RECIPE_WORKSTATION);
        for (ChurnRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(ChurnRecipe.Type.INSTANCE)) {
            emiRegistry.addRecipe(new EmiChurnRecipe(recipe));
        }
        // cheese press
        emiRegistry.addCategory(CHEESE_PRESS_RECIPE_CATEGORY);
        emiRegistry.addWorkstation(CHEESE_PRESS_RECIPE_CATEGORY, CHEESE_PRESS_RECIPE_WORKSTATION);
        for (CheesePressRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(CheesePressRecipe.Type.INSTANCE)) {
            emiRegistry.addRecipe(new EmiCheesePressRecipe(recipe));
        }
        // mixing to get a fluid
        emiRegistry.addCategory(MIXING_VAT_RECIPE_CATEGORY_1);
        emiRegistry.addWorkstation(MIXING_VAT_RECIPE_CATEGORY_1, MIXING_VAT_RECIPE_WORKSTATION);
        for (MixingVatFluidRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(MixingVatFluidRecipe.Type.INSTANCE)) {
            emiRegistry.addRecipe(new EmiMixingVatFluidRecipe(recipe));
        }
        // mixing to get an item
        emiRegistry.addCategory(MIXING_VAT_RECIPE_CATEGORY_2);
        emiRegistry.addWorkstation(MIXING_VAT_RECIPE_CATEGORY_2, MIXING_VAT_RECIPE_WORKSTATION);
        for (MixingVatItemRecipe recipe : emiRegistry.getRecipeManager().getAllRecipesFor(MixingVatItemRecipe.Type.INSTANCE)) {
            emiRegistry.addRecipe(new EmiMixingVatItemRecipe(recipe));
        }
        // cheese slicing
        for (BaseCheeseWheel.Cheese cheese : BaseCheeseWheel.Cheese.values()) {
            if (cheese.isAgeable() || cheese.isWaxable()) { // because cheese.isAgeable() is broken - returns false if the cheese is also waxable
                emiRegistry.addRecipe(EmiWorldInteractionRecipe.builder()
                                                               .leftInput(EmiStack.of(cheese.getAged()))
                                                               .rightInput(EmiIngredient.of(GrowthcraftTags.Items.TAG_KNIFE), true)
                                                               .output(EmiStack.of(cheese.getSlices(1)))
                                                               .build()
                );
            }
        }
        // cheese waxing
        for (BaseCheeseWheel.Cheese cheese : BaseCheeseWheel.Cheese.values()) {
            if (cheese.isWaxable()) {
                emiRegistry.addRecipe(EmiWorldInteractionRecipe.builder()
                                                               .leftInput(EmiStack.of(cheese.getUnprocessed()))
                                                               .rightInput(EmiStack.of(cheese.getWax()), false)
                                                               .output(EmiStack.of(cheese.getWaxed()))
                                                               .build()
                );
            }
        }
        // cheese aging
        emiRegistry.addCategory(CHEESE_AGING_CATEGORY);
        for (BaseCheeseWheel.Cheese cheese : BaseCheeseWheel.Cheese.values()) {
            if (cheese.isWaxable()) { // because cheese.isAgeable() is broken - returns false if the cheese is also waxable
                emiRegistry.addRecipe(new EmiCheeseAgingRecipe(cheese.getWaxed(), cheese.getAged()));
            }
            else if (cheese.isAgeable()) {
                emiRegistry.addRecipe(new EmiCheeseAgingRecipe(cheese.getUnprocessed(), cheese.getAged()));
            }
        }
    }

    private static final ResourceLocation DUMMY_SPRITE_LOCATION = new ResourceLocation("emi", "textures/gui/widgets.png");  // for the tree-screen. i won't bother using separate icons now.
    private static final EmiTexture DUMMY_SPRITE = new EmiTexture(DUMMY_SPRITE_LOCATION, 64, 148, 16, 16);  // for the tree-screen. i won't bother using separate icons now.

    private static final EmiStack PANCHEON_RECIPE_WORKSTATION = EmiStack.of(GrowthcraftMilkBlocks.PANCHEON.get());
    private static final EmiStack CHURN_RECIPE_WORKSTATION = EmiStack.of(GrowthcraftMilkBlocks.CHURN.get());
    private static final EmiStack CHEESE_PRESS_RECIPE_WORKSTATION = EmiStack.of(GrowthcraftMilkBlocks.CHEESE_PRESS.get());
    private static final EmiStack MIXING_VAT_RECIPE_WORKSTATION = EmiStack.of(GrowthcraftMilkBlocks.MIXING_VAT.get());

    public static final EmiRecipeCategory PANCHEON_RECIPE_CATEGORY = new EmiRecipeCategory(new ResourceLocation(Reference.MODID, "pancheon"), PANCHEON_RECIPE_WORKSTATION, DUMMY_SPRITE);
    public static final EmiRecipeCategory DRYING_RECIPE_CATEGORY = new EmiRecipeCategory(new ResourceLocation(Reference.MODID, "drying"), EmiStack.of(GrowthcraftMilkBlocks.ASIAGO_CHEESE_CURDS.get()));
    public static final EmiRecipeCategory CHURN_RECIPE_CATEGORY = new EmiRecipeCategory(new ResourceLocation(Reference.MODID, "churn"), CHURN_RECIPE_WORKSTATION, DUMMY_SPRITE);
    public static final EmiRecipeCategory CHEESE_PRESS_RECIPE_CATEGORY = new EmiRecipeCategory(new ResourceLocation(Reference.MODID, "cheese_press"), CHEESE_PRESS_RECIPE_WORKSTATION, DUMMY_SPRITE);
    public static final EmiRecipeCategory MIXING_VAT_RECIPE_CATEGORY_1 = new EmiRecipeCategory(new ResourceLocation(Reference.MODID, "mixing_vat_1"), MIXING_VAT_RECIPE_WORKSTATION, DUMMY_SPRITE);
    public static final EmiRecipeCategory MIXING_VAT_RECIPE_CATEGORY_2 = new EmiRecipeCategory(new ResourceLocation(Reference.MODID, "mixing_vat_2"), MIXING_VAT_RECIPE_WORKSTATION, DUMMY_SPRITE);
    public static final EmiRecipeCategory CHEESE_AGING_CATEGORY = new EmiRecipeCategory(new ResourceLocation(Reference.MODID, "aging"), EmiStack.of(GrowthcraftMilkBlocks.GOUDA_CHEESE.get()));
}
