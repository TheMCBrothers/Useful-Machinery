package themcbros.usefulmachinery.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import themcbros.usefulmachinery.container.MachineContainer;
import themcbros.usefulmachinery.machine.RedstoneMode;

public class RedstoneModeButton extends ExtendedButton {

    private MachineContainer container;

    public RedstoneModeButton(MachineContainer container, int x, int y, IPressable onPress) {
        super(x, y, 16, 16, "", button -> {
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
    public void renderButton(int mouseX, int mouseY, float partial) {
        super.renderButton(mouseX, mouseY, partial);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(container.getRedstoneMode().getIcon());
        RenderSystem.disableDepthTest();

        blit(this.x, this.y, 0, 0, this.width, this.height, 16, 16);
        RenderSystem.enableDepthTest();
    }
}
