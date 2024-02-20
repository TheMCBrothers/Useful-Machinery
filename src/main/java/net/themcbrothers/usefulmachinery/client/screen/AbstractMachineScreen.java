package net.themcbrothers.usefulmachinery.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.themcbrothers.lib.client.screen.widgets.EnergyBar;
import net.themcbrothers.lib.network.PacketUtils;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.client.screen.widget.RedstoneModeButton;
import net.themcbrothers.usefulmachinery.machine.RedstoneMode;
import net.themcbrothers.usefulmachinery.menu.AbstractMachineMenu;
import net.themcbrothers.usefulmachinery.network.SetRedstoneModePacket;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public abstract class AbstractMachineScreen<T extends AbstractMachineMenu> extends AbstractContainerScreen<T> {
    private static final ResourceLocation UPGRADE_SLOTS_TEXTURE = UsefulMachinery.rl("textures/gui/container/upgrade_slots.png");

    public AbstractMachineScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float partialTick) {
        super.render(guiGraphics, x, y, partialTick);

        this.renderTooltip(guiGraphics, x, y);
    }

    @Override
    protected void init() {
        super.init();

        RedstoneModeButton redstoneModeButton = new RedstoneModeButton(this.menu, this.leftPos - 16, this.topPos, button -> {
            RedstoneMode mode = ((RedstoneModeButton) button).getMode();

            PacketUtils.sendToServer(new SetRedstoneModePacket(mode));
        });

        this.addRenderableWidget(redstoneModeButton);
        this.addRenderableWidget(new EnergyBar(this.leftPos + 155, this.topPos + 17, EnergyBar.Size._10x50, this.menu, this));

        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);

        for (Renderable renderable : this.renderables) {
            if (renderable instanceof RedstoneModeButton button && button.isHoveredOrFocused()) {
                RedstoneMode mode = button.getMode();

                guiGraphics.renderTooltip(this.font, TEXT_UTILS.translate("misc", "redstoneMode", mode.name()), x, y);
            }

            if (renderable instanceof EnergyBar energyBar && energyBar.isHoveredOrFocused()) {
                energyBar.renderToolTip(guiGraphics, x, y);
            }
        }
    }

    protected void renderUpgradeSlots(GuiGraphics guiGraphics) {
        int x = this.leftPos + 179;
        int y = this.topPos;
        int yOffset = 8;
        int upgradeSlotSize = this.menu.getUpgradeSlotSize();

        if (upgradeSlotSize != 0) {
            guiGraphics.blit(UPGRADE_SLOTS_TEXTURE, x, y, 0, 0, 34, 8);

            for (int i = 0; i < upgradeSlotSize; i++) {
                guiGraphics.blit(UPGRADE_SLOTS_TEXTURE, x, y + yOffset, 0, 8, 34, 18);

                yOffset += 18;
            }

            guiGraphics.blit(UPGRADE_SLOTS_TEXTURE, x, y + yOffset, 0, 26, 34, 8);
        }
    }
}
