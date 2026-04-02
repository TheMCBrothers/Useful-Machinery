package net.themcbrothers.usefulmachinery.client.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.client.screen.widget.CompactorModeButton;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;
import net.themcbrothers.usefulmachinery.menu.CompactorMenu;
import net.themcbrothers.usefulmachinery.network.SetCompactorModePacket;

import java.util.List;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public class CompactorScreen extends AbstractMachineScreen<CompactorMenu> {
    private static final Identifier TEXTURE = UsefulMachinery.id("textures/gui/container/compactor.png");

    public CompactorScreen(CompactorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected Identifier getBackgroundTexture() {
        return TEXTURE;
    }

    @Override
    protected void init() {
        super.init();

        CompactorModeButton compactorModeButton = new CompactorModeButton(this.menu, this.leftPos + 9, this.topPos + 31, 20, 20, button -> {
            CompactorMode mode = ((CompactorModeButton) button).getMode();

            ClientPacketDistributor.sendToServer(new SetCompactorModePacket(mode));
        });

        this.addRenderableWidget(compactorModeButton);
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

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);

        for (Renderable renderable : this.renderables) {
            if (renderable instanceof CompactorModeButton button && button.isHoveredOrFocused()) {
                CompactorMode mode = button.getMode();

                Component compactorMode = TEXT_UTILS.translate("misc", "compact_" + mode.getSerializedName(), mode.name());
                ClientTooltipComponent tooltipComponent = ClientTooltipComponent.create(compactorMode.getVisualOrderText());

                graphics.tooltip(this.font, List.of(tooltipComponent), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
            }
        }
    }
}
