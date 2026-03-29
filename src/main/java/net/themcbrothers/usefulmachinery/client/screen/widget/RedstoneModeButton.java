package net.themcbrothers.usefulmachinery.client.screen.widget;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.themcbrothers.usefulmachinery.machine.RedstoneMode;
import net.themcbrothers.usefulmachinery.menu.AbstractMachineMenu;

public class RedstoneModeButton extends ExtendedButton {
    private final AbstractMachineMenu menu;

    public RedstoneModeButton(AbstractMachineMenu menu, int x, int y, OnPress handler) {
        super(x, y, 16, 16, Component.empty(), button -> {
            ((RedstoneModeButton) button).cycleMode();
            handler.onPress(button);
        });

        this.menu = menu;
    }

    @Override
    public void setFocused(boolean pFocused) {
        super.setFocused(false);
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractContents(graphics, mouseX, mouseY, partialTick);

        RedstoneMode mode = this.getMode();
        Identifier icon = mode.getIcon();

        graphics.blit(RenderPipelines.GUI_TEXTURED, icon, this.getX(), this.getY(), 0, mode.ordinal() == 0 ? 0 : 2, this.width, this.height, 16, 16);
    }

    private void cycleMode() {
        int ordinal = this.menu.getRedstoneMode().ordinal() + 1;

        if (ordinal >= RedstoneMode.values().length) {
            ordinal = 0;
        }

        this.menu.setRedstoneMode(RedstoneMode.byOrdinal(ordinal));
    }

    public RedstoneMode getMode() {
        return this.menu.getRedstoneMode();
    }
}
