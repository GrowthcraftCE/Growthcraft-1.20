package growthcraft.milk.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ListEmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.milk.recipe.MixingVatItemRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EmiMixingVatItemRecipe extends BasicEmiRecipe {
    public EmiMixingVatItemRecipe(MixingVatItemRecipe recipe) {
        super(EmiPlugin.MIXING_VAT_RECIPE_CATEGORY_1, recipe.getId(), 125, 94);
        this.inputs.add(EmiStack.of(recipe.getInputFluidStack().getFluid(), recipe.getInputFluidStack().getAmount()));
        this.inputs.add(EmiStack.of(recipe.getInputFluidStack().getFluid().getBucket()));
        this.inputs.add(recipe.getIngredients().size() >= 1 ? EmiIngredient.of(recipe.getIngredients().get(0)) : EmiStack.EMPTY);
        this.inputs.add(recipe.getIngredients().size() >= 2 ? EmiIngredient.of(recipe.getIngredients().get(1)) : EmiStack.EMPTY);
        this.inputs.add(recipe.getIngredients().size() >= 3 ? EmiIngredient.of(recipe.getIngredients().get(2)) : EmiStack.EMPTY);
        this.outputs.add(EmiStack.of(recipe.getResultItemStack()));
        this.activationTool = EmiStack.of(recipe.getActivationTool());
        this.collectionTool = EmiStack.of(recipe.getResultActivationTool());
    }
    private final EmiStack activationTool, collectionTool;

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        // Adds input slot 1
        widgetHolder.addSlot(inputs.get(0), 0, 20);
        // Add plus texture
        widgetHolder.addTexture(SMALL_PLUS, 20, 23);
        // Adds input slot 2
        widgetHolder.addSlot(inputs.get(2), 32, 0);
        widgetHolder.addSlot(inputs.get(3), 32, 20);
        widgetHolder.addSlot(inputs.get(4), 32, 40);
        // Add texture
        widgetHolder.addTexture(SMALL_ARROW, 52, 21);
        // Adds an output slot on the right
        widgetHolder.addSlot(outputs.get(0), 74, 20).recipeContext(this);
        widgetHolder.addText(Component.translatable("emi.caption.growthcraft_milk.mixing_vat_starter"), 2, 62, 0xFF444433, false);
        widgetHolder.addText(Component.translatable("emi.caption.growthcraft_milk.mixing_vat_collector"), 2, 79, 0xFF444433, false);
        //widgetHolder.addDrawable(100, 58, 16, 16, (graphics, mouseX, mouseY, delta) -> graphics.renderFakeItem(this.activationTool, 0, 0));
        widgetHolder.addSlot(this.activationTool, 106, 56);
        widgetHolder.addSlot(this.collectionTool, 106, 75);
    }
    private static final ResourceLocation WIDGETS = new ResourceLocation("growthcraft:textures/gui/widgets.png");
    private static final EmiTexture SMALL_PLUS = new EmiTexture(WIDGETS, 83, 1, 10, 10);
    private static final EmiTexture SMALL_ARROW = new EmiTexture(WIDGETS, 44, 0, 20, 15);
}
