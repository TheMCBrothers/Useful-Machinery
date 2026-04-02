package net.themcbrothers.usefulmachinery.client.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.menu.CoalGeneratorMenu;

public class CoalGeneratorScreen extends AbstractMachineScreen<CoalGeneratorMenu> {
    private static final Identifier TEXTURE = UsefulMachinery.id("textures/gui/container/coal_generator.png");

    public CoalGeneratorScreen(CoalGeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected Identifier getBackgroundTexture() {
        return TEXTURE;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);

        int x = this.leftPos;
        int y = this.topPos;

        // Render burning flame
        if (this.menu.isProcessing()) {
            int scaledBurnTime = this.menu.getProgressScaled(13);

            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, 54 + x, 34 + y + 12 - scaledBurnTime, 176, 12 - scaledBurnTime, 14, scaledBurnTime + 1, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
        }
    }
}
