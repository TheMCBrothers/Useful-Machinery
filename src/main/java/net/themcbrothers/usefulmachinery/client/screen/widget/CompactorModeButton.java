package net.themcbrothers.usefulmachinery.client.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;
import net.themcbrothers.usefulmachinery.menu.CompactorMenu;


public class CompactorModeButton extends ExtendedButton {
    private final CompactorMenu menu;


    public CompactorModeButton(CompactorMenu menu, int x, int y, int width, int height, OnPress onPress) {
        super(x, y, width, height, Component.empty(), button -> {
            ((CompactorModeButton) button).cycleMode();

            onPress.onPress(button);
        });

        this.menu = menu;
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(false);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

        Font font = Minecraft.getInstance().font;
        ItemStack renderStack = this.getMode().getItemProvider().asItem().getDefaultInstance();

        guiGraphics.renderItemDecorations(font, renderStack, this.getX() + 2, this.getY() + 2);
        guiGraphics.renderItem(renderStack, this.getX() + 2, this.getY() + 2);
    }

    private void cycleMode() {
        int ordinal = this.menu.getCompactorMode().ordinal() + 1;

        if (ordinal >= CompactorMode.values().length) {
            ordinal = 0;
        }

       this.menu.setCompactorMode(CompactorMode.byOrdinal(ordinal));
    }

    public CompactorMode getMode() {
        return this.menu.getCompactorMode();
    }
}
