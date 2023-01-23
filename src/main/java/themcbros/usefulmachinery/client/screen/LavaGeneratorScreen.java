package themcbros.usefulmachinery.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.themcbrothers.lib.client.screen.widgets.FluidTank;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.menu.LavaGeneratorMenu;

import javax.annotation.Nonnull;

public class LavaGeneratorScreen extends AbstractMachineScreen<LavaGeneratorMenu> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.getId("textures/gui/container/lava_generator.png");

    public LavaGeneratorScreen(LavaGeneratorMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableOnly(new FluidTank(this.leftPos + 11, this.topPos + 17, 10, 50, this.menu.getFluidTankHandler(), this));
    }

    @Override
    protected void renderBg(@Nonnull PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURES);

        int i = this.leftPos;
        int j = this.topPos;

        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // Render burning flame
        if (this.menu.isBurning()) {
            int l = this.menu.getBurnTimeScaled();
            this.blit(poseStack, 81 + i, 34 + j + 12 - l, 176, 12 - l, 14, l + 1);
        }
    }

    @Override
    protected void renderTooltip(@Nonnull PoseStack poseStack, int mouseX, int mouseY) {
        for (Widget widget : this.renderables) {
            if (widget instanceof FluidTank fluidTank && fluidTank.isHoveredOrFocused()) {
                fluidTank.renderToolTip(poseStack, mouseX, mouseY);
            }
        }

        super.renderTooltip(poseStack, mouseX, mouseY);
    }
}
