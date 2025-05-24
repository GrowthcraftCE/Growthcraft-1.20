package growthcraft.milk.recipe;

import com.google.gson.JsonObject;
import growthcraft.lib.utils.CraftingUtils;
import growthcraft.lib.utils.RecipeUtils;
import growthcraft.milk.GrowthcraftMilk;
import growthcraft.milk.shared.Reference;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class MixingVatItemRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation recipeId;
    private final RecipeUtils.Category category;
    private final ItemStack activationTool;
    private final NonNullList<Ingredient> ingredients;
    private final FluidStack inputFluidStack;
    private final int processingTime;
    private final ItemStack resultItemStack;
    private final ItemStack resultActivationTool;
    private final boolean requiresHeat;


    public MixingVatItemRecipe(ResourceLocation recipeId, RecipeUtils.Category category,
                               FluidStack inputFluidStack, NonNullList<Ingredient> ingredients, int processingTime,
                               ItemStack resultItemStack, ItemStack activationTool,
                               ItemStack resultActivationTool,
                               boolean requiresHeat) {
        this.recipeId = recipeId;
        this.category = category;
        this.inputFluidStack = inputFluidStack;
        this.ingredients = ingredients;
        this.processingTime = processingTime;
        this.resultItemStack = resultItemStack.copy();
        this.activationTool = activationTool.copy();
        this.resultActivationTool = resultActivationTool.copy();
        this.requiresHeat = requiresHeat;
    }


    @Override
    public boolean matches(@NotNull SimpleContainer p_44002_, @NotNull Level p_44003_) {
        return false;
    }

    /**
     * Checks if the given test fluid stack, test ingredients, and heat source match the recipe criteria.
     *
     * @param testFluidStack  The fluid stack to test against the recipe input fluid stack.
     * @param testIngredients The list of item stacks to test against the recipe ingredients.
     * @param hasHeatSource   Indicates whether a heat source is present.
     * @return {@code true} if the test criteria match the recipe criteria, {@code false} otherwise.
     */
    public boolean matches(FluidStack testFluidStack, List<ItemStack> testIngredients, boolean hasHeatSource) {
        // Check if the input fluid matches the required fluid
        if (!CraftingUtils.doesFluidMatch(testFluidStack, getInputFluidStack())) {
            return false;
        }

        List<Ingredient> requiredIngredients = getIngredients();

        // If the number of test ingredients doesn't match the recipe, it's not a match
        if (requiredIngredients.size() != testIngredients.size()) {
            return false;
        }

        // Compare each ingredient slot by slot
        for (int i = 0; i < requiredIngredients.size(); i++) {
            Ingredient required = requiredIngredients.get(i);
            ItemStack testItem = testIngredients.get(i);

            // Check if the test item matches any of the items in the ingredient, including count
            boolean match = Arrays.stream(required.getItems())
                    .anyMatch(item -> item.is(testItem.getItem()) && item.getCount() == testItem.getCount());

            // If any ingredient doesn't match, the whole recipe doesn't match
            if (!match) {
                return false;
            }
        }

        // Finally, check if the heat source requirement is satisfied
        return hasHeatSource == isHeatRequired();
    }

    public boolean matchResult(ItemStack itemStack) {
        return this.resultItemStack.getItem() == itemStack.getItem() && this.resultItemStack.getCount() == itemStack.getCount();
    }

    public FluidStack getInputFluidStack() {
        return this.inputFluidStack;
    }

    public ItemStack getActivationTool() {
        return this.activationTool;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public ItemStack getResultItemStack() {
        return this.resultItemStack.copy();
    }

    public ItemStack getResultActivationTool() {
        return this.resultActivationTool;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return resultItemStack;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return resultItemStack;
    }

    public boolean isHeatRequired() {
        return this.requiresHeat;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.recipeId;
    }

    public RecipeUtils.Category getCategory() {
        return this.category;
    }

    public boolean is(RecipeUtils.Category category) {
        return this.category == category;
    }

    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Type implements RecipeType<MixingVatItemRecipe> {
        public static final MixingVatItemRecipe.Type INSTANCE = new MixingVatItemRecipe.Type();
        public static final String ID = Reference.UnlocalizedName.MIXING_VAT_ITEM_RECIPE;

        private Type() { /* Prevent default constructor */ }
    }


    public static class Serializer implements RecipeSerializer<MixingVatItemRecipe> {

        public static final MixingVatItemRecipe.Serializer INSTANCE = new MixingVatItemRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(
                Reference.MODID,
                Reference.UnlocalizedName.MIXING_VAT_RECIPE);

        private static final int maxIngredients = 3;

        @Override
        public MixingVatItemRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            int processingTime = GsonHelper.getAsInt(json, "processing_time", 1200);
            boolean requiresHeat = GsonHelper.getAsBoolean(json, "requires_heat");

            FluidStack inputFluid = CraftingUtils.getFluidStack(
                    GsonHelper.getAsJsonObject(json, "input_fluid"));

            ItemStack activationTool = CraftingHelper.getItemStack(
                    GsonHelper.getAsJsonObject(json, "activation_tool"), false);

            NonNullList<Ingredient> ingredients = CraftingUtils.readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));

            ItemStack resultItemStack = CraftingHelper.getItemStack(
                    GsonHelper.getAsJsonObject(json, "result_item"), false);
            ItemStack resultActivationTool = CraftingHelper.getItemStack(
                    GsonHelper.getAsJsonObject(json, "result_activation_tool"), false);

            return new MixingVatItemRecipe(recipeId, RecipeUtils.Category.ITEM,
                    inputFluid, ingredients, processingTime, resultItemStack, activationTool, resultActivationTool, requiresHeat);

        }

        @Override
        public @Nullable MixingVatItemRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            try {
                int processingTime = buffer.readVarInt();
                boolean requiresHeat = buffer.readBoolean();

                FluidStack inputFluidStack = buffer.readFluidStack();
                ItemStack activationTool = buffer.readItem();

                int ingredientSize = buffer.readVarInt();
                NonNullList<Ingredient> inputIngredients = NonNullList.withSize(ingredientSize, Ingredient.EMPTY);

                for (int i = 0; i < ingredientSize; i++) {
                    inputIngredients.set(i, Ingredient.fromNetwork(buffer));
                }

                ItemStack resultingItemStack = buffer.readItem();
                ItemStack resultActivationTool = buffer.readItem();

                return new MixingVatItemRecipe(recipeId, RecipeUtils.Category.ITEM, inputFluidStack, inputIngredients,
                        processingTime, resultingItemStack, activationTool, resultActivationTool, requiresHeat);
            } catch (Exception ex) {
                String message = String.format("Unable to read recipe (%s) from network buffer.", recipeId);
                GrowthcraftMilk.LOGGER.error(message);
                throw ex;
            }
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, MixingVatItemRecipe recipe) {
            buffer.writeVarInt(recipe.getProcessingTime());
            buffer.writeBoolean(recipe.isHeatRequired());

            buffer.writeFluidStack(recipe.getInputFluidStack());
            buffer.writeItemStack(recipe.getActivationTool(), false);

            buffer.writeVarInt(recipe.ingredients.size());
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItemStack(recipe.getResultItemStack(), false);
            buffer.writeItemStack(recipe.getResultActivationTool(), false);
        }

    }
}
