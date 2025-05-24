package growthcraft.milk.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ListEmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.milk.recipe.MixingVatFluidRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EmiMixingVatFluidRecipe extends BasicEmiRecipe {
    private boolean hasByproduct = false, hasSecondary = false;

    public EmiMixingVatFluidRecipe(MixingVatFluidRecipe recipe) {
        super(EmiPlugin.MIXING_VAT_RECIPE_CATEGORY_2, recipe.getId(), 125, 65);
        this.inputs.add(EmiStack.of(recipe.getInputFluidStack().getFluid(), recipe.getInputFluidStack().getAmount()));
        this.inputs.add(EmiStack.of(recipe.getInputFluidStack().getFluid().getBucket()));
        if (! recipe.getReagentFluidStack().isEmpty()) {
            this.inputs.add(EmiStack.of(recipe.getReagentFluidStack().getFluid(), recipe.getReagentFluidStack().getAmount()));
            this.inputs.add(EmiStack.of(recipe.getReagentFluidStack().getFluid().getBucket()));
            this.hasSecondary = true;
        }
        this.extraIndex = this.hasSecondary ? 4 : 2;
        this.extraCount = recipe.getIngredients().size();
        recipe.getIngredients().forEach(i -> this.inputs.add(EmiIngredient.of(i)));
        this.outputs.add(EmiStack.of(recipe.getOutputFluidStack().getFluid(), recipe.getOutputFluidStack().getAmount()));
        this.outputs.add(EmiStack.of(recipe.getOutputFluidStack().getFluid().getBucket()));
        if (! recipe.getWasteFluidStack().isEmpty()) {
            this.outputs.add(EmiStack.of(recipe.getWasteFluidStack().getFluid(), recipe.getWasteFluidStack().getAmount()));
            this.outputs.add(EmiStack.of(recipe.getWasteFluidStack().getFluid().getBucket()));
            this.hasByproduct = true;
        }
        this.activationTool = EmiStack.of(recipe.getActivationTool());
    }
    private final EmiStack activationTool;
    private final int extraCount, extraIndex;

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        // Adds input slot 1
        widgetHolder.addSlot(inputs.get(0), 0, 0);
        if (this.hasSecondary) {
            // Add plus texture
            widgetHolder.addTexture(SMALL_PLUS, 20, 3);
            // Adds input slot 2
            widgetHolder.addSlot(inputs.get(2), 32, 0);
            // Add texture
        }
        widgetHolder.addTexture(SMALL_ARROW, 52, 1);
        // Adds an output slot on the right
        widgetHolder.addSlot(outputs.get(0), 74, 0).recipeContext(this);
        if (this.hasByproduct) {
            // Add texture
            widgetHolder.addTexture(SMALL_PLUS, 94, 3);
            // Adds output slot 2
            widgetHolder.addSlot(outputs.get(2), 107, 0).recipeContext(this);
        }
        widgetHolder.addText(Component.translatable("emi.caption.growthcraft_milk.mixing_vat_starter"), 4, 51, 0xFF444433, false);
        widgetHolder.addSlot(this.activationTool, 102, 45);
        widgetHolder.addText(Component.translatable("emi.caption.growthcraft_milk.mixing_vat_additions"), 4, 26, 0xFF444433, false);
        for (int i = 0; i < this.extraCount; i++) {
            widgetHolder.addSlot(this.inputs.get(this.extraIndex + i), 60 + i * 20, 20);
        }
    }
    private static final ResourceLocation WIDGETS = new ResourceLocation("growthcraft:textures/gui/widgets.png");
    private static final EmiTexture SMALL_PLUS = new EmiTexture(WIDGETS, 83, 1, 10, 10);
    private static final EmiTexture SMALL_ARROW = new EmiTexture(WIDGETS, 44, 0, 20, 15);
}
