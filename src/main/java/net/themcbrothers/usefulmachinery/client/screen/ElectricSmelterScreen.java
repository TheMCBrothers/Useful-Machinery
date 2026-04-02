package net.themcbrothers.usefulmachinery.client.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.menu.ElectricSmelterMenu;

public class ElectricSmelterScreen extends AbstractMachineScreen<ElectricSmelterMenu> {
    private static final Identifier TEXTURE = UsefulMachinery.id("textures/gui/container/electric_smelter.png");

    public ElectricSmelterScreen(ElectricSmelterMenu menu, Inventory playerInventory, Component title) {
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

        // Render arrow
        int scaledProgress = this.menu.getProgressScaled(24);

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, 58 + x, 32 + y, 176, 14, scaledProgress, 17, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
    }
}
