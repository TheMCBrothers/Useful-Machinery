package net.themcbrothers.usefulmachinery.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

        RedstoneMode mode = this.getMode();
        ResourceLocation icon = mode.getIcon();

        RenderSystem.disableDepthTest();

        guiGraphics.blit(icon, this.getX(), this.getY(), 0, mode.ordinal() == 0 ? 0 : 2, this.width, this.height, 16, 16);
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
