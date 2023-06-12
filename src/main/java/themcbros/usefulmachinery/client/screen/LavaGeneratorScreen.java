package themcbros.usefulmachinery.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.themcbrothers.lib.client.screen.widgets.FluidTank;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.menu.LavaGeneratorMenu;

import javax.annotation.Nonnull;

public class LavaGeneratorScreen extends AbstractMachineScreen<LavaGeneratorMenu> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.getId("textures/gui/container/lava_generator.png");

    public LavaGeneratorScreen(LavaGeneratorMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableOnly(new FluidTank(this.leftPos + 11, this.topPos + 17, 10, 50, this.menu.getFluidTankHandler(), this));
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;

        graphics.blit(TEXTURES, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // Render burning flame
        if (this.menu.isBurning()) {
            int l = this.menu.getBurnTimeScaled();
            graphics.blit(TEXTURES, 81 + i, 34 + j + 12 - l, 176, 12 - l, 14, l + 1);
        }

        this.renderUpgradeSlots(graphics);
    }

    @Override
    protected void renderTooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
        for (Renderable renderable : this.renderables) {
            if (renderable instanceof FluidTank fluidTank && fluidTank.isHoveredOrFocused()) {
                fluidTank.renderToolTip(graphics, mouseX, mouseY);
            }
        }

        super.renderTooltip(graphics, mouseX, mouseY);
    }
}
