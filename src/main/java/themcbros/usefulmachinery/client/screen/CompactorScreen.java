package themcbros.usefulmachinery.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.client.screen.widget.CompactorModeButton;
import themcbros.usefulmachinery.machine.CompactorMode;
import themcbros.usefulmachinery.menu.CompactorMenu;
import themcbros.usefulmachinery.networking.Networking;
import themcbros.usefulmachinery.networking.SetCompactorModePacket;

import javax.annotation.Nonnull;

import static themcbros.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public class CompactorScreen extends AbstractMachineScreen<CompactorMenu> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.getId("textures/gui/container/electric_smelter.png");

    public CompactorScreen(CompactorMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();

        CompactorModeButton compactorModeButton = new CompactorModeButton(this.menu, this.leftPos + 9, this.topPos + 31, 20, 20, button -> {
            CompactorMode mode = ((CompactorModeButton) button).getMode();
            Networking.channel.sendToServer(new SetCompactorModePacket(mode));
        });

        this.addRenderableWidget(compactorModeButton);
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        graphics.blit(TEXTURES, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // Render arrow
        int l = this.menu.getProgressScaled(24);
        graphics.blit(TEXTURES, 58 + i, 32 + j, 176, 14, l, 17);

        this.renderUpgradeSlots(graphics);
    }

    @Override
    protected void renderTooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
        for (Renderable renderable : this.renderables) {
            if (renderable instanceof CompactorModeButton button && button.isHoveredOrFocused()) {
                CompactorMode mode = button.getMode();
                graphics.renderTooltip(Minecraft.getInstance().font, TEXT_UTILS.translate("misc", "compact_" + mode.getSerializedName()), mouseX, mouseY);
            }
        }

        super.renderTooltip(graphics, mouseX, mouseY);
    }
}
