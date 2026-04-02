package net.themcbrothers.usefulmachinery.client.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.themcbrothers.lib.client.screen.widgets.FluidTank;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.menu.LavaGeneratorMenu;

public class LavaGeneratorScreen extends AbstractMachineScreen<LavaGeneratorMenu> {
    private static final Identifier TEXTURE = UsefulMachinery.id("textures/gui/container/lava_generator.png");

    public LavaGeneratorScreen(LavaGeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected Identifier getBackgroundTexture() {
        return TEXTURE;
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableOnly(new FluidTank(this.leftPos + 11, this.topPos + 17, 10, 50, this.menu.getFluidTankHandler(), this));
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);

        int x = this.leftPos;
        int y = this.topPos;

        // Render burning flame
        if (this.menu.isProcessing()) {
            int scaledBurnTime = this.menu.getProgressScaled(13);

            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, 81 + x, 34 + y + 12 - scaledBurnTime, 176, 12 - scaledBurnTime, 14, scaledBurnTime + 1, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);

        for (Renderable renderable : this.renderables) {
            if (renderable instanceof FluidTank fluidTank && fluidTank.isHoveredOrFocused()) {
                fluidTank.renderToolTip(graphics, mouseX, mouseY);
            }
        }
    }
}
