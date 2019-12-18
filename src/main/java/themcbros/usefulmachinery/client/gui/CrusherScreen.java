package themcbros.usefulmachinery.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.client.gui.widget.EnergyBar;
import themcbros.usefulmachinery.container.CrusherContainer;
import themcbros.usefulmachinery.util.TextUtils;

import java.awt.*;

public class CrusherScreen extends MachineScreen<CrusherContainer> {

    private static final ResourceLocation TEXTURES = UsefulMachinery.getId("textures/gui/container/crusher.png");

    public CrusherScreen(CrusherContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.energyBar = new EnergyBar(155, 17, 10, 50);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.func_227673_b_(1.0F, 1.0F, 1.0F, 1.0F);
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bindTexture(TEXTURES);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);

        // Render Energy Bar
        int k = this.container.getEnergyScaled((int) this.energyBar.rect.getHeight());
        Rectangle bar = this.energyBar.rect;
        this.blit(bar.x + i, bar.y + j + bar.height - k, 246, bar.height - k, bar.width, k);

        // Render arrow
        int l = this.container.getProgressScaled(24);
        this.blit(58 + i, 34 + j, 176, 14, l, 17);

    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        Rectangle bar = this.energyBar.rect;
        if (isPointInRegion(bar.x, bar.y, bar.width, bar.height, mouseX, mouseY)) {
            this.renderTooltip(TextUtils.energyWithMax(this.container.getEnergyStored(), this.container.getMaxEnergyStored()).getFormattedText(), mouseX, mouseY);
        }
        super.renderHoveredToolTip(mouseX, mouseY);
    }
}
