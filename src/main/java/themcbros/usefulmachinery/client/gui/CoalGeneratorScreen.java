package themcbros.usefulmachinery.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.container.CoalGeneratorContainer;

import javax.annotation.Nonnull;

public class CoalGeneratorScreen extends AbstractMachineScreen<CoalGeneratorContainer> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.getId("textures/gui/container/coal_generator.png");

    public CoalGeneratorScreen(CoalGeneratorContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
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
            this.blit(poseStack, 54 + i, 34 + j + 12 - l, 176, 12 - l, 14, l + 1);
        }
    }
}
