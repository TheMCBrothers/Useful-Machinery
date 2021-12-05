package themcbros.usefulmachinery.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.client.gui.widget.EnergyBar;
import themcbros.usefulmachinery.container.CrusherContainer;
import themcbros.usefulmachinery.util.TextUtils;

import javax.annotation.Nonnull;
import java.awt.*;

public class CrusherScreen extends MachineScreen<CrusherContainer> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.getId("textures/gui/container/crusher.png");

    public CrusherScreen(CrusherContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.energyBar = new EnergyBar(155, 17, 10, 50);
    }

    @Override
    protected void renderBg(@Nonnull PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURES);

        int i = this.leftPos;
        int j = this.topPos;
        this.blit(poseStack, i, j, 0, 0, this.width, this.height);

        // Render Energy Bar
        int k = this.menu.getEnergyScaled((int) this.energyBar.rect.getHeight());
        Rectangle bar = this.energyBar.rect;
        this.blit(poseStack, bar.x + i, bar.y + j + bar.height - k, 246, bar.height - k, bar.width, k);

        // Render arrow
        int l = this.menu.getProgressScaled(24);
        this.blit(poseStack, 58 + i, 34 + j, 176, 14, l, 17);
    }

    @Override
    protected void renderTooltip(@Nonnull PoseStack poseStack, int mouseX, int mouseY) {
        Rectangle bar = this.energyBar.rect;

        if (isHovering(bar.x, bar.y, bar.width, bar.height, mouseX, mouseY)) {
            this.renderTooltip(poseStack, TextUtils.energyWithMax(this.menu.getEnergyStored(), this.menu.getMaxEnergyStored()), mouseX, mouseY);
        }

        super.renderTooltip(poseStack, mouseX, mouseY);
    }
}
