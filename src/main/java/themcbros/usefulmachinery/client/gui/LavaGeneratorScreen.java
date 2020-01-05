package themcbros.usefulmachinery.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.client.gui.widget.EnergyBar;
import themcbros.usefulmachinery.container.LavaGeneratorContainer;
import themcbros.usefulmachinery.util.TextUtils;

import java.awt.*;
import java.util.List;

public class LavaGeneratorScreen extends MachineFluidScreen<LavaGeneratorContainer> {

    private static final ResourceLocation TEXTURES = UsefulMachinery.getId("textures/gui/container/lava_generator.png");

    private Rectangle lavaTankRect = new Rectangle(11, 17, 10, 50);

    public LavaGeneratorScreen(LavaGeneratorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn, screenContainer.getTankCapacity());
        this.energyBar = new EnergyBar(155, 17, 10, 50);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bindTexture(TEXTURES);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);

        // Render Energy Bar
        Rectangle bar = this.energyBar.rect;
        int k = this.container.getEnergyScaled(bar.height);
        this.blit(bar.x + i, bar.y + j + bar.height - k, 246, bar.height - k, bar.width, k);

        // Render burning flame
        if (this.container.isBurning()) {
            int l = this.container.getBurnTimeScaled();
            this.blit(81 + i, 34 + j + 12 - l, 176, 12 - l, 14, l + 1);
        }

        // Render the fluid
        this.drawFluid(this.lavaTankRect.x + i, this.lavaTankRect.y + j, this.container.getTankStack());
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        Rectangle bar = this.energyBar.rect;
        if (isPointInRegion(bar.x, bar.y, bar.width, bar.height, mouseX, mouseY)) {
            this.renderTooltip(TextUtils.energyWithMax(this.container.getEnergyStored(), this.container.getMaxEnergyStored()).getFormattedText(), mouseX, mouseY);
        }
        if (isPointInRegion(lavaTankRect.x, lavaTankRect.y, lavaTankRect.width, lavaTankRect.height, mouseX, mouseY)) {
            List<String> texts = Lists.newArrayList();
            texts.add(TextUtils.fluidName(this.container.getTankStack()).getFormattedText());
            texts.add(TextUtils.fluidWithMax(this.container.getFluidTankHandler()).getFormattedText());
            this.renderTooltip(texts, mouseX, mouseY);
        }
        super.renderHoveredToolTip(mouseX, mouseY);
    }
}
