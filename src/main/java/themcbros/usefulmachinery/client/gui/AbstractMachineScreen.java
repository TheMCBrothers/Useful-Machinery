package themcbros.usefulmachinery.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.themcbrothers.lib.client.screen.widgets.EnergyBar;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.client.gui.widget.RedstoneModeButton;
import themcbros.usefulmachinery.container.MachineContainer;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.networking.Networking;
import themcbros.usefulmachinery.networking.SetRedstoneModePacket;

import javax.annotation.Nonnull;

public abstract class AbstractMachineScreen<T extends MachineContainer> extends AbstractContainerScreen<T> {
    protected EnergyBar energyBar = null;

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

        for (Widget widget : this.renderables) {
            if (widget instanceof RedstoneModeButton button && button.isHoveredOrFocused()) {
                RedstoneMode mode = button.getMode();
                renderTooltip(poseStack, UsefulMachinery.TEXT_UTILS.translate("misc", "redstoneMode", mode.name()), mouseX, mouseY);
            }
        }
    }
}
