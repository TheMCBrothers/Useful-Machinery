package net.themcbrothers.usefulmachinery.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.themcbrothers.lib.client.screen.widgets.FluidTank;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.menu.LavaGeneratorMenu;

public class LavaGeneratorScreen extends AbstractMachineScreen<LavaGeneratorMenu> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.rl("textures/gui/container/lava_generator.png");

    public LavaGeneratorScreen(LavaGeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableOnly(new FluidTank(this.leftPos + 11, this.topPos + 17, 10, 50, this.menu.getFluidTankHandler(), this));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int x, int y) {
        int i = this.leftPos;
        int j = this.topPos;

        guiGraphics.blit(TEXTURES, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // Render burning flame
        if (this.menu.isBurning()) {
            int l = this.menu.getBurnTimeScaled();

            guiGraphics.blit(TEXTURES, 81 + i, 34 + j + 12 - l, 176, 12 - l, 14, l + 1);
        }

        this.renderUpgradeSlots(guiGraphics);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);

        for (Renderable renderable : this.renderables) {
            if (renderable instanceof FluidTank fluidTank && fluidTank.isHoveredOrFocused()) {
                fluidTank.renderToolTip(guiGraphics, x, y);
            }
        }
    }
}
