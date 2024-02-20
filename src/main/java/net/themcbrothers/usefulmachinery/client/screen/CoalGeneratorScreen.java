package net.themcbrothers.usefulmachinery.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.menu.CoalGeneratorMenu;

public class CoalGeneratorScreen extends AbstractMachineScreen<CoalGeneratorMenu> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.rl("textures/gui/container/coal_generator.png");

    public CoalGeneratorScreen(CoalGeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int x, int y) {
        int i = this.leftPos;
        int j = this.topPos;

        guiGraphics.blit(TEXTURES, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // Render burning flame
        if (this.menu.isBurning()) {
            int l = this.menu.getBurnTimeScaled();

            guiGraphics.blit(TEXTURES, 54 + i, 34 + j + 12 - l, 176, 12 - l, 14, l + 1);
        }

        this.renderUpgradeSlots(guiGraphics);
    }
}
