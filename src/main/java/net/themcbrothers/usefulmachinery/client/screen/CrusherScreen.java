package net.themcbrothers.usefulmachinery.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.menu.CrusherMenu;

public class CrusherScreen extends AbstractMachineScreen<CrusherMenu> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.rl("textures/gui/container/crusher.png");

    public CrusherScreen(CrusherMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int x, int y) {
        int i = this.leftPos;
        int j = this.topPos;

        guiGraphics.blit(TEXTURES, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // Render arrow
        int l = this.menu.getProgressScaled(24);

        guiGraphics.blit(TEXTURES, 58 + i, 34 + j, 176, 14, l, 17);

        this.renderUpgradeSlots(guiGraphics);
    }
}
