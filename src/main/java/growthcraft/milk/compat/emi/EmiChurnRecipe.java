package growthcraft.milk.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.milk.recipe.ChurnRecipe;
import growthcraft.milk.recipe.PancheonRecipe;
import net.minecraft.world.item.crafting.Ingredient;

public class EmiChurnRecipe extends BasicEmiRecipe {
    public EmiChurnRecipe(ChurnRecipe recipe) {
        super(EmiPlugin.CHURN_RECIPE_CATEGORY, recipe.getId(), 125, 18);
        this.inputs.add(EmiStack.of(recipe.getInputFluidStack().getFluid()));
        this.inputs.add(EmiStack.of(recipe.getInputFluidStack().getFluid().getBucket()));
        this.outputs.add(EmiStack.of(recipe.getResultItemStack()));
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        // Add an arrow texture to indicate processing
        widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
        // Adds an input slot on the left
        widgetHolder.addSlot(inputs.get(0), 0, 0);
        // Adds an output slot on the right
        // Note that output slots need to call `recipeContext` to inform EMI about their recipe context
        // This includes being able to resolve recipe trees, favorite stacks with recipe context, and more
        widgetHolder.addSlot(outputs.get(0), 58, 0).recipeContext(this);
    }
}
