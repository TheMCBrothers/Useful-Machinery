package net.themcbrothers.usefulmachinery.client.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.menu.CoalGeneratorMenu;

public class CoalGeneratorScreen extends AbstractMachineScreen<CoalGeneratorMenu> {
    private static final Identifier TEXTURES = UsefulMachinery.id("textures/gui/container/coal_generator.png");

    public CoalGeneratorScreen(CoalGeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);

        int i = this.leftPos;
        int j = this.topPos;

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, i, j, 0, 0, this.imageWidth, this.imageHeight, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);

        // Render burning flame
        if (this.menu.isBurning()) {
            int l = this.menu.getBurnTimeScaled();

            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, 54 + i, 34 + j + 12 - l, 176, 12 - l, 14, l + 1, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
        }

        this.extractUpgradeSlots(graphics);
    }
}
