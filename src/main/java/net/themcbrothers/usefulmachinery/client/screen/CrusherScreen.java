package net.themcbrothers.usefulmachinery.client.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.menu.CrusherMenu;

public class CrusherScreen extends AbstractMachineScreen<CrusherMenu> {
    private static final Identifier TEXTURE = UsefulMachinery.id("textures/gui/container/crusher.png");

    public CrusherScreen(CrusherMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);

        int i = this.leftPos;
        int j = this.topPos;

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);

        // Render arrow
        int l = this.menu.getProgressScaled(24);

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, 58 + i, 34 + j, 176, 14, l, 17, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);

        this.extractUpgradeSlots(graphics);
    }
}
