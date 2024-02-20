package net.themcbrothers.usefulmachinery.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.themcbrothers.lib.network.PacketUtils;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.client.screen.widget.CompactorModeButton;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;
import net.themcbrothers.usefulmachinery.menu.CompactorMenu;
import net.themcbrothers.usefulmachinery.network.SetCompactorModePacket;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public class CompactorScreen extends AbstractMachineScreen<CompactorMenu> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.rl("textures/gui/container/compactor.png");

    public CompactorScreen(CompactorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        CompactorModeButton compactorModeButton = new CompactorModeButton(this.menu, this.leftPos + 9, this.topPos + 31, 20, 20, button -> {
            CompactorMode mode = ((CompactorModeButton) button).getMode();

            PacketUtils.sendToServer(new SetCompactorModePacket(mode));
        });

        this.addRenderableWidget(compactorModeButton);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int x, int y) {
        int i = this.leftPos;
        int j = this.topPos;

        guiGraphics.blit(TEXTURES, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // Render arrow
        int l = this.menu.getProgressScaled(24);

        guiGraphics.blit(TEXTURES, 58 + i, 32 + j, 176, 14, l, 17);

        this.renderUpgradeSlots(guiGraphics);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);

        for (Renderable renderable : this.renderables) {
            if (renderable instanceof CompactorModeButton button && button.isHoveredOrFocused()) {
                CompactorMode mode = button.getMode();

                guiGraphics.renderTooltip(this.font, TEXT_UTILS.translate("misc", "compact_" + mode.getSerializedName()), x, y);
            }
        }
    }
}
