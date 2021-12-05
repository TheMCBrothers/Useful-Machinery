package themcbros.usefulmachinery.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.client.gui.widget.EnergyBar;
import themcbros.usefulmachinery.container.LavaGeneratorContainer;
import themcbros.usefulmachinery.util.TextUtils;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

public class LavaGeneratorScreen extends MachineFluidScreen<LavaGeneratorContainer> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.getId("textures/gui/container/lava_generator.png");

    private final Rectangle lavaTankRect = new Rectangle(11, 17, 10, 50);

    public LavaGeneratorScreen(LavaGeneratorContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn, screenContainer.getTankCapacity());
        this.energyBar = new EnergyBar(155, 17, 10, 50);
    }

    @Override
    protected void renderBg(@Nonnull PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURES);

        int i = this.leftPos;
        int j = this.topPos;

        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // Render Energy Bar
        Rectangle bar = this.energyBar.rect;
        int k = this.menu.getEnergyScaled(bar.height);
        this.blit(poseStack, bar.x + i, bar.y + j + bar.height - k, 246, bar.height - k, bar.width, k);

        // Render burning flame
        if (this.menu.isBurning()) {
            int l = this.menu.getBurnTimeScaled();
            this.blit(poseStack, 81 + i, 34 + j + 12 - l, 176, 12 - l, 14, l + 1);
        }

        // Render the fluid
        this.drawFluid(this.lavaTankRect.x + i, this.lavaTankRect.y + j, this.menu.getTankStack());
    }

    @Override
    protected void renderTooltip(@Nonnull PoseStack poseStack, int mouseX, int mouseY) {
        Rectangle bar = this.energyBar.rect;
        if (isHovering(bar.x, bar.y, bar.width, bar.height, mouseX, mouseY)) {
            this.renderTooltip(poseStack, TextUtils.energyWithMax(this.menu.getEnergyStored(), this.menu.getMaxEnergyStored()), mouseX, mouseY);
        }
        if (isHovering(lavaTankRect.x, lavaTankRect.y, lavaTankRect.width, lavaTankRect.height, mouseX, mouseY)) {
            List<Component> texts = Lists.newArrayList();
            texts.add(TextUtils.fluidName(this.menu.getTankStack()));
            texts.add(TextUtils.fluidWithMax(this.menu.getFluidTankHandler()));

            this.renderComponentTooltip(poseStack, texts, mouseX, mouseY);
        }
        super.renderTooltip(poseStack, mouseX, mouseY);
    }
}
