package themcbros.usefulmachinery.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.themcbrothers.lib.client.screen.widgets.EnergyBar;
import themcbros.usefulmachinery.client.screen.widget.RedstoneModeButton;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.menu.MachineMenu;
import themcbros.usefulmachinery.networking.Networking;
import themcbros.usefulmachinery.networking.SetRedstoneModePacket;

import javax.annotation.Nonnull;

import static themcbros.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public abstract class AbstractMachineScreen<T extends MachineMenu> extends AbstractContainerScreen<T> {
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
    public void render(@Nonnull PoseStack poseStack, int x, int y, float ticks) {
        this.renderBackground(poseStack);
        super.render(poseStack, x, y, ticks);
        this.renderTooltip(poseStack, x, y);
    }

    @Override
    protected void renderTooltip(@Nonnull PoseStack poseStack, int mouseX, int mouseY) {
        super.renderTooltip(poseStack, mouseX, mouseY);

        for (Renderable renderable : this.renderables) {
            if (renderable instanceof RedstoneModeButton button && button.isHoveredOrFocused()) {
                RedstoneMode mode = button.getMode();
                renderTooltip(poseStack, TEXT_UTILS.translate("misc", "redstoneMode", mode.name()), mouseX, mouseY);
            }

            if (renderable instanceof EnergyBar energyBar && energyBar.isHoveredOrFocused()) {
                energyBar.renderToolTip(poseStack, mouseX, mouseY);
            }
        }
    }
}
