package growthcraft.milk.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.milk.recipe.PancheonRecipe;
import net.minecraft.world.item.crafting.Ingredient;

public class EmiPancheonRecipe extends BasicEmiRecipe {
    public EmiPancheonRecipe(PancheonRecipe recipe) {
        super(EmiPlugin.PANCHEON_RECIPE_CATEGORY, recipe.getId(), 125, 18);
        this.inputs.add(EmiStack.of(recipe.getInput().getFluid(), 2000));
        this.inputs.add(EmiIngredient.of(Ingredient.of(recipe.getInput().getFluid().getBucket())));
        this.outputs.add(EmiStack.of(recipe.getOutput1().getFluid().getBucket()));
        this.outputs.add(EmiStack.of(recipe.getOutput2().getFluid().getBucket()));
        this.outputs.add(EmiStack.of(recipe.getOutput1().getFluid(), 1000));
        this.outputs.add(EmiStack.of(recipe.getOutput2().getFluid(), 1000));
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        // Add plus and arrow textures
        widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 27, 1);
        widgetHolder.addTexture(EmiTexture.PLUS, 85, 3);
        // Adds an input slot 1
        widgetHolder.addSlot(inputs.get(0), 0, 0);
        // Adds an output slot 1
        widgetHolder.addSlot(outputs.get(2), 59, 0).recipeContext(this);
        // Adds an output slot 2
        widgetHolder.addSlot(outputs.get(3), 107, 0).recipeContext(this);
    }
}
