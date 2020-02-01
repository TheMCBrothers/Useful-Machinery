package themcbros.usefulmachinery.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import themcbros.usefulmachinery.container.CompactorContainer;
import themcbros.usefulmachinery.machine.CompactorMode;

public class CompactorModeButton extends Button {

    private CompactorContainer container;

    public CompactorModeButton(CompactorContainer container, int x, int y, int width, int height, IPressable onPress) {
        super(x, y, width, height, "", button -> {
            ((CompactorModeButton) button).cycleMode();
            onPress.onPress(button);
        });
        this.container = container;
    }

    private void cycleMode() {
        int ordinal = container.getCompactorMode().getIndex() + 1;
        if (ordinal >= CompactorMode.values().length)
            ordinal = 0;
        container.setCompactorMode(CompactorMode.byIndex(ordinal));
    }

    public CompactorMode getMode() {
        return this.container.getCompactorMode();
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float p_renderButton_3_) {
        super.renderButton(mouseX, mouseY, p_renderButton_3_);

        final Minecraft minecraft = Minecraft.getInstance();
        final ItemStack renderStack = this.container.getCompactorMode().getIconStack();
        minecraft.getItemRenderer().renderItemOverlayIntoGUI(minecraft.fontRenderer, renderStack, this.x + 2, this.y + 2, "");
        minecraft.getItemRenderer().renderItemAndEffectIntoGUI(renderStack, this.x + 2, this.y + 2);
    }

}
