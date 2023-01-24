package themcbros.usefulmachinery.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.client.screen.widget.CompactorModeButton;
import themcbros.usefulmachinery.machine.CompactorMode;
import themcbros.usefulmachinery.menu.CompactorMenu;
import themcbros.usefulmachinery.networking.Networking;
import themcbros.usefulmachinery.networking.SetCompactorModePacket;

import javax.annotation.Nonnull;

import static themcbros.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public class CompactorScreen extends AbstractMachineScreen<CompactorMenu> {
    private static final ResourceLocation TEXTURES = UsefulMachinery.getId("textures/gui/container/electric_smelter.png");

    public CompactorScreen(CompactorMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();

        CompactorModeButton compactorModeButton = new CompactorModeButton(this.menu, this.leftPos + 9, this.topPos + 31, 20, 20, button -> {
            CompactorMode mode = ((CompactorModeButton) button).getMode();
            Networking.channel.sendToServer(new SetCompactorModePacket(mode));
        });

        this.addRenderableWidget(compactorModeButton);
    }

    @Override
    protected void renderBg(@Nonnull PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURES);

        int i = this.leftPos;
        int j = this.topPos;
        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // Render arrow
        int l = this.menu.getProgressScaled(24);
        this.blit(poseStack, 58 + i, 32 + j, 176, 14, l, 17);

    }

    @Override
    protected void renderTooltip(@Nonnull PoseStack poseStack, int mouseX, int mouseY) {
        for (Renderable renderable : this.renderables) {
            if (renderable instanceof CompactorModeButton button && button.isHoveredOrFocused()) {
                CompactorMode mode = button.getMode();
                renderTooltip(poseStack, TEXT_UTILS.translate("misc", "compact_" + mode.getSerializedName()), mouseX, mouseY);
            }
        }

        super.renderTooltip(poseStack, mouseX, mouseY);
    }
}
