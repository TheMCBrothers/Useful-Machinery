package themcbros.usefulmachinery.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.menu.CrusherMenu;

import javax.annotation.Nonnull;

public class CrusherScreen extends AbstractMachineScreen<CrusherMenu> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.getId("textures/gui/container/crusher.png");

    public CrusherScreen(CrusherMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        graphics.blit(TEXTURES, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // Render arrow
        int l = this.menu.getProgressScaled(24);
        graphics.blit(TEXTURES, 58 + i, 34 + j, 176, 14, l, 17);

        this.renderUpgradeSlots(graphics);
    }
}
