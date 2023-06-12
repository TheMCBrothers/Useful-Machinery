package themcbros.usefulmachinery.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.themcbrothers.lib.client.screen.widgets.EnergyBar;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.client.screen.widget.RedstoneModeButton;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.menu.AbstractMachineMenu;
import themcbros.usefulmachinery.networking.Networking;
import themcbros.usefulmachinery.networking.SetRedstoneModePacket;

import javax.annotation.Nonnull;

import static themcbros.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public abstract class AbstractMachineScreen<T extends AbstractMachineMenu> extends AbstractContainerScreen<T> {
    private static final ResourceLocation UPGRADE_SLOTS_TEXTURE = UsefulMachinery.getId("textures/gui/container/upgrade_slots.png");

    AbstractMachineScreen(T screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();

        RedstoneModeButton redstoneModeButton = new RedstoneModeButton(this.menu, this.leftPos - 16, this.topPos, button -> {
            RedstoneMode mode = ((RedstoneModeButton) button).getMode();
            Networking.channel.sendToServer(new SetRedstoneModePacket(mode));
        });

        this.addRenderableWidget(redstoneModeButton);
        this.addRenderableOnly(new EnergyBar(this.leftPos + 155, this.topPos + 17, EnergyBar.Size._10x50, this.menu, this));

        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int x, int y, float ticks) {
        this.renderBackground(graphics);
        super.render(graphics, x, y, ticks);
        this.renderTooltip(graphics, x, y);
    }

    protected void renderUpgradeSlots(@Nonnull GuiGraphics graphics) {
        int xPos = this.leftPos + 179;
        int yPos = this.topPos;
        int yOffset = 8;

        if (this.menu.getUpgradeSlotSize() != 0) {
            graphics.blit(UPGRADE_SLOTS_TEXTURE, xPos, yPos, 0, 0, 34, 8);

            for (int i = 1; i <= this.menu.getUpgradeSlotSize(); i++) {
                graphics.blit(UPGRADE_SLOTS_TEXTURE, xPos, yPos + yOffset, 0, 8, 34, 18);
                yOffset += 18;
            }

            graphics.blit(UPGRADE_SLOTS_TEXTURE, xPos, yPos + yOffset, 0, 26, 34, 8);
        }
    }

    @Override
    protected void renderTooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);

        for (Renderable renderable : this.renderables) {
            if (renderable instanceof RedstoneModeButton button && button.isHoveredOrFocused()) {
                RedstoneMode mode = button.getMode();
                graphics.renderTooltip(Minecraft.getInstance().font, TEXT_UTILS.translate("misc", "redstoneMode", mode.name()), mouseX, mouseY);
            }

            if (renderable instanceof EnergyBar energyBar && energyBar.isHoveredOrFocused()) {
                energyBar.renderToolTip(graphics, mouseX, mouseY);
            }
        }
    }
}
