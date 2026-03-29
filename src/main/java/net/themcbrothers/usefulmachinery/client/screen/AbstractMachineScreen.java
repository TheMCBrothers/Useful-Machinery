package net.themcbrothers.usefulmachinery.client.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.themcbrothers.lib.client.screen.widgets.EnergyBar;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.client.screen.widget.RedstoneModeButton;
import net.themcbrothers.usefulmachinery.machine.RedstoneMode;
import net.themcbrothers.usefulmachinery.menu.AbstractMachineMenu;
import net.themcbrothers.usefulmachinery.network.SetRedstoneModePacket;

import java.util.List;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public abstract class AbstractMachineScreen<T extends AbstractMachineMenu> extends AbstractContainerScreen<T> {
    private static final int UPGRADE_SLOT_TEXTURE_WIDTH = 256;
    private static final int UPGRADE_SLOT_TEXTURE_HEIGHT = 256;
    private static final Identifier UPGRADE_SLOT_TEXTURE = UsefulMachinery.id("textures/gui/container/upgrade_slot.png");
    protected static final int BACKGROUND_TEXTURE_WIDTH = 256;
    protected static final int BACKGROUND_TEXTURE_HEIGHT = 256;

    public AbstractMachineScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int x, int y, float partialTick) {
        super.extractRenderState(graphics, x, y, partialTick);

        this.extractTooltip(graphics, x, y);
    }

    @Override
    protected void init() {
        super.init();

        RedstoneModeButton redstoneModeButton = new RedstoneModeButton(this.menu, this.leftPos - 16, this.topPos, button -> {
            RedstoneMode mode = ((RedstoneModeButton) button).getMode();

            ClientPacketDistributor.sendToServer(new SetRedstoneModePacket(mode));
        });

        this.addRenderableWidget(redstoneModeButton);
        this.addRenderableWidget(new EnergyBar(this.leftPos + 155, this.topPos + 17, EnergyBar.Size._10x50, this.menu, this));

        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);

        for (Renderable renderable : this.renderables) {
            if (renderable instanceof RedstoneModeButton button && button.isHoveredOrFocused()) {
                RedstoneMode mode = button.getMode();

                Component redstoneMode = TEXT_UTILS.translate("misc", "redstoneMode", mode.name());
                ClientTooltipComponent tooltipComponent = ClientTooltipComponent.create(redstoneMode.getVisualOrderText());

                graphics.tooltip(this.font, List.of(tooltipComponent), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
            }

            if (renderable instanceof EnergyBar energyBar && energyBar.isHoveredOrFocused()) {
                energyBar.renderToolTip(graphics, mouseX, mouseY);
            }
        }
    }

    protected void extractUpgradeSlots(GuiGraphicsExtractor graphics) {
        int x = this.leftPos + 179;
        int y = this.topPos;
        int yOffset = 8;
        int upgradeSlotSize = this.menu.getUpgradeSlotSize();

        if (upgradeSlotSize != 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, UPGRADE_SLOT_TEXTURE, x, y, 0, 0, 34, 8, UPGRADE_SLOT_TEXTURE_WIDTH, UPGRADE_SLOT_TEXTURE_HEIGHT);

            for (int i = 0; i < upgradeSlotSize; i++) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, UPGRADE_SLOT_TEXTURE, x, y + yOffset, 0, 8, 34, 18, UPGRADE_SLOT_TEXTURE_WIDTH, UPGRADE_SLOT_TEXTURE_HEIGHT);

                yOffset += 18;
            }

            graphics.blit(RenderPipelines.GUI_TEXTURED, UPGRADE_SLOT_TEXTURE, x, y + yOffset, 0, 26, 34, 8, UPGRADE_SLOT_TEXTURE_WIDTH, UPGRADE_SLOT_TEXTURE_HEIGHT);
        }
    }
}
