package themcbros.usefulmachinery.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.menu.MachineMenu;

public class RedstoneModeButton extends ExtendedButton {
    private final MachineMenu container;

    public RedstoneModeButton(MachineMenu container, int x, int y, OnPress onPress) {
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
    public boolean changeFocus(boolean randomNumb) {
        return false;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partial) {
        super.renderButton(poseStack, mouseX, mouseY, partial);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation icon = this.container.getRedstoneMode().getIcon();
        RenderSystem.setShaderTexture(0, icon);

        RenderSystem.disableDepthTest();

        blit(poseStack, this.getX(), this.getY(), 0, this.container.getRedstoneMode().getIndex() == 0 ? 0 : 2, this.width, this.height, 16, 16);

        RenderSystem.enableDepthTest();
    }
}
