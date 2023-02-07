package themcbros.usefulmachinery.client.screen.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import themcbros.usefulmachinery.machine.CompactorMode;
import themcbros.usefulmachinery.menu.CompactorMenu;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class CompactorModeButton extends Button {
    private final CompactorMenu container;

    public CompactorModeButton(CompactorMenu container, int x, int y, int width, int height, OnPress onPress) {
        super(x, y, width, height, Component.empty(), button -> {
            ((CompactorModeButton) button).cycleMode();
            onPress.onPress(button);
        }, Supplier::get);

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
    public boolean changeFocus(boolean randomNumb) {
        return false;
    }

    @Override
    public void renderButton(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float p_renderButton_3_) {
        super.renderButton(poseStack, mouseX, mouseY, p_renderButton_3_);

        final Minecraft minecraft = Minecraft.getInstance();
        final ItemStack renderStack = this.container.getCompactorMode().getIconStack();

        minecraft.getItemRenderer().renderGuiItemDecorations(minecraft.font, renderStack, this.getX() + 2, this.getY() + 2, "");
        minecraft.getItemRenderer().renderGuiItem(renderStack, this.getX() + 2, this.getY() + 2);
    }

}
