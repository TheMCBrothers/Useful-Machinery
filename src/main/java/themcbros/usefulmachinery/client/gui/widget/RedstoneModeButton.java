package themcbros.usefulmachinery.client.gui.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import themcbros.usefulmachinery.container.MachineContainer;
import themcbros.usefulmachinery.machine.RedstoneMode;

public class RedstoneModeButton extends Button {

    private MachineContainer container;

    public RedstoneModeButton(MachineContainer container, int x, int y, int width, int height, IPressable onPress) {
        super(x, y, width, height, "", button -> {
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
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(container.getRedstoneMode().getIcon());
//        GlStateManager.disableDepthTest(); TODO

        blit(this.x, this.y, 0, 0, this.width, this.height, 16, 16);
//        GlStateManager.enableDepthTest(); TODO
    }

}
