package growthcraft.milk.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import growthcraft.lib.kaupenjoe.screen.renderer.FluidTankRenderer;
import growthcraft.milk.block.entity.MixingVatBlockEntity;
import growthcraft.milk.screen.container.MixingVatMenu;
import growthcraft.milk.shared.Reference;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Optional;

public class MixingVatScreen extends AbstractContainerScreen<MixingVatMenu> {
    //TODO[5]: Implement MixingVatScreen
    //
    private static final ResourceLocation TEXTURE = new ResourceLocation(
            Reference.MODID, "textures/gui/mixing_vat_screen.png"
    );

    private FluidTankRenderer fluidTankRenderer0;
    private FluidTankRenderer fluidTankRenderer1;

    public MixingVatScreen(MixingVatMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        assignFluidRender();
    }

    private void assignFluidRender() {
        fluidTankRenderer0 = new FluidTankRenderer(4000, true, 16, 38);
        fluidTankRenderer1 = new FluidTankRenderer(1000, true, 16, 10);
    }

    @Override
    protected void renderBg(GuiGraphics poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        poseStack.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        poseStack.blit(TEXTURE,
                x + 99, y + 21,
                176, 43,
                13, menu.getProgressionScaled(29)
        );

        if (this.menu.isHeated()) {
            poseStack.blit(TEXTURE,
                    x + 98, y + 56,
                    176, 28,
                    13, 13
            );
        }

        fluidTankRenderer0.render(poseStack.pose(), x + 49, y + 32, menu.getFluidStack(0));
        fluidTankRenderer1.render(poseStack.pose(), x + 49, y + 18, menu.getFluidStack(1));
    }

    @Override
    public void render(GuiGraphics poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        // Render any tooltips for this mouse over location.
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics poseStack, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Screen Title
        poseStack.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        // Inventory Title
        poseStack.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);

        // FluidTank Tooltips
        renderFluidTankTooltips(poseStack, mouseX, mouseY, x, y);
    }

    private void renderFluidTankTooltips(GuiGraphics poseStack, int mouseX, int mouseY, int x, int y) {

        // FluidTank0
        if (isMouseAboveArea(mouseX, mouseY, x + 49, y + 32, 16, 38, fluidTankRenderer0.getWidth(), fluidTankRenderer0.getHeight())) {
            poseStack.renderTooltip(
                    this.font,
                    fluidTankRenderer0.getTooltip(menu.getFluidStack(0), TooltipFlag.Default.NORMAL),
                    Optional.empty(),
                    mouseX - x,
                    mouseY - y
            );
        }

        if (isMouseAboveArea(mouseX, mouseY, x + 49, y + 18, 16, 10, fluidTankRenderer1.getWidth(), fluidTankRenderer1.getHeight())) {
            poseStack.renderTooltip(
                    this.font,
                    fluidTankRenderer1.getTooltip(menu.getFluidStack(1), TooltipFlag.Default.NORMAL),
                    Optional.empty(),
                    mouseX - x,
                    mouseY - y
            );
        }

    }

    private boolean isMouseAboveArea(int mouseX, int mouseY, int baseX, int baseY, int offsetX, int offsetY, int width, int height) {
        return (mouseX >= baseX && mouseX <= (baseX + offsetX)) && (mouseY >= baseY && mouseY <= (baseY + offsetY));
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getSlotIndex() == 3 && this.hoveredSlot.hasItem()) {
            // result slot
            ItemStack resultItemStack = this.hoveredSlot.getItem();
            List<Component> tooltipLines = this.getTooltipFromContainerItem(resultItemStack);
            if (this.lastResultItem != resultItemStack.hashCode()) {
                ItemStack finalizerItem = ((MixingVatBlockEntity) this.getMenu().getBlockEntity()).getResultActivationTool();
                Component finalizerText = finalizerItem.isEmpty() ? Component.translatable("message.growthcraft_milk.get_using_item_empty_hand").withStyle(Style.EMPTY.withColor(0xffffff88)) : finalizerItem.getHoverName().copy().withStyle(Style.EMPTY.withColor(0xffffff88));
                this.lastComponent = Component.translatable("message.growthcraft_milk.get_using_item", finalizerText).withStyle(Style.EMPTY.withColor(0xffddbb44));
                this.lastResultItem = resultItemStack.hashCode();
            }
            tooltipLines.add(this.lastComponent);
            pGuiGraphics.renderTooltip(this.font, tooltipLines, resultItemStack.getTooltipImage(), resultItemStack, pX, pY);
        }
        else {
            // not a result slot
            super.renderTooltip(pGuiGraphics, pX, pY);
        }
    }
    private int lastResultItem = 0;
    private Component lastComponent = null;
}
