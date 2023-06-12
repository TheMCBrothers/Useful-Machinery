package themcbros.usefulmachinery.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.menu.CoalGeneratorMenu;

import javax.annotation.Nonnull;

public class CoalGeneratorScreen extends AbstractMachineScreen<CoalGeneratorMenu> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.getId("textures/gui/container/coal_generator.png");

    public CoalGeneratorScreen(CoalGeneratorMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        graphics.blit(TEXTURES, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // Render burning flame
        if (this.menu.isBurning()) {
            int l = this.menu.getBurnTimeScaled();
            graphics.blit(TEXTURES, 54 + i, 34 + j + 12 - l, 176, 12 - l, 14, l + 1);
        }

        this.renderUpgradeSlots(graphics);
    }
}
