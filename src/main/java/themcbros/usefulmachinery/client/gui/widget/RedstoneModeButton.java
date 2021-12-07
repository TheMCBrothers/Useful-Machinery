package themcbros.usefulmachinery.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import themcbros.usefulmachinery.container.MachineContainer;
import themcbros.usefulmachinery.machine.RedstoneMode;

public class RedstoneModeButton extends ExtendedButton {
    private final MachineContainer container;

    public RedstoneModeButton(MachineContainer container, int x, int y, OnPress onPress) {
        super(x, y, 16, 16, TextComponent.EMPTY, button -> {
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
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partial) {
        super.renderButton(poseStack, mouseX, mouseY, partial);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.container.getRedstoneMode().getIcon());

        RenderSystem.disableDepthTest();

        blit(poseStack, this.x, this.y, 0, 0, this.width, this.height, 16, 16);

        RenderSystem.enableDepthTest();
    }
}
