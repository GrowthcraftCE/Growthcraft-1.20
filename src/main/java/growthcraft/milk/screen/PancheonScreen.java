package growthcraft.milk.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import growthcraft.lib.kaupenjoe.screen.renderer.FluidTankRenderer;
import growthcraft.milk.screen.container.PancheonMenu;
import growthcraft.milk.shared.Reference;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;

import java.util.Optional;

public class PancheonScreen extends AbstractContainerScreen<PancheonMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            Reference.MODID, "textures/gui/pancheon_screen.png"
    );

    private FluidTankRenderer fluidTankRenderer0;
    private FluidTankRenderer fluidTankRenderer1;
    private FluidTankRenderer fluidTankRenderer2;

    public PancheonScreen(PancheonMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        assignFluidRender();
    }

    private void assignFluidRender() {
        fluidTankRenderer0 = new FluidTankRenderer(2000, true, 16, 52);
        fluidTankRenderer1 = new FluidTankRenderer(1000, true, 16, 23);
        fluidTankRenderer2 = new FluidTankRenderer(1000, true, 16, 23);
    }

    @Override
    protected void renderBg(GuiGraphics poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        poseStack.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // Progress bar
        poseStack.blit(TEXTURE, x + 82, y + 29, 176, 42, 13, menu.getProgressionScaled(29));

        fluidTankRenderer0.render(poseStack.pose(), x + 62, y + 18, menu.getFluidStack(0));
        fluidTankRenderer1.render(poseStack.pose(), x + 98, y + 18, menu.getFluidStack(1));
        fluidTankRenderer2.render(poseStack.pose(), x + 98, y + 47, menu.getFluidStack(2));
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
        if (isMouseAboveArea(mouseX, mouseY, x + 62, y + 18, 16, 52, fluidTankRenderer0.getWidth(), fluidTankRenderer0.getHeight())) {
            poseStack.renderTooltip(
                    this.font,
                    fluidTankRenderer0.getTooltip(menu.getFluidStack(0), TooltipFlag.Default.NORMAL),
                    Optional.empty(),
                    mouseX - x,
                    mouseY - y
            );
        }

        if (isMouseAboveArea(mouseX, mouseY, x + 98, y + 18, 16, 23, fluidTankRenderer1.getWidth(), fluidTankRenderer1.getHeight())) {
            poseStack.renderTooltip(
                    this.font,
                    fluidTankRenderer1.getTooltip(menu.getFluidStack(1), TooltipFlag.Default.NORMAL),
                    Optional.empty(),
                    mouseX - x,
                    mouseY - y
            );
        }

        if (isMouseAboveArea(mouseX, mouseY, x + 98, y + 47, 16, 23, fluidTankRenderer2.getWidth(), fluidTankRenderer2.getHeight())) {
            poseStack.renderTooltip(
                    this.font,
                    fluidTankRenderer2.getTooltip(menu.getFluidStack(2), TooltipFlag.Default.NORMAL),
                    Optional.empty(),
                    mouseX - x,
                    mouseY - y
            );
        }

    }

    private boolean isMouseAboveArea(int mouseX, int mouseY, int baseX, int baseY, int offsetX, int offsetY, int width, int height) {
        return (mouseX >= baseX && mouseX <= (baseX + offsetX)) && (mouseY >= baseY && mouseY <= (baseY + offsetY));
    }

}
