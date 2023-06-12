package themcbros.usefulmachinery.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.menu.AbstractMachineMenu;

public class RedstoneModeButton extends ExtendedButton {
    private final AbstractMachineMenu container;

    public RedstoneModeButton(AbstractMachineMenu container, int x, int y, OnPress onPress) {
        super(x, y, 16, 16, Component.empty(), button -> {
            ((RedstoneModeButton) button).cycleMode();
            onPress.onPress(button);
        });
        this.container = container;
    }

    private void cycleMode() {
        int ordinal = container.getRedstoneMode().ordinal() + 1;

        if (ordinal >= RedstoneMode.values().length)
            ordinal = 0;

        container.setRedstoneMode(RedstoneMode.byIndex(ordinal));
    }

    public RedstoneMode getMode() {
        return this.container.getRedstoneMode();
    }

    @Override
    public void setFocused(boolean focus) {
        super.setFocused(false);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        super.renderWidget(graphics, mouseX, mouseY, partial);
        ResourceLocation icon = this.container.getRedstoneMode().getIcon();

        RenderSystem.disableDepthTest();

        graphics.blit(icon, this.getX(), this.getY(), 0, this.container.getRedstoneMode().getIndex() == 0 ? 0 : 2, this.width, this.height, 16, 16);

        RenderSystem.enableDepthTest();
    }
}
