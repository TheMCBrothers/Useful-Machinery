package themcbros.usefulmachinery.blockentity.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;
import themcbros.usefulmachinery.machine.MachineTier;

public class MachineRenderer implements BlockEntityRenderer<AbstractMachineBlockEntity> {
    public MachineRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
    }

    @Override
    public void render(AbstractMachineBlockEntity tileEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        MachineTier machineTier = tileEntityIn.machineTier;

        poseStack.pushPose();

        setGLColorFromInt(machineTier.getColor());

        poseStack.translate(0f, 1f, 0f);

        ItemStack stack = tileEntityIn.getItem(0);

        if (!stack.isEmpty()) {
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, poseStack, bufferIn, 0);
        }

        poseStack.popPose();
    }

    private static void setGLColorFromInt(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = ((color >> 24) & 0xFF) / 255F;

        RenderSystem.clearColor(red, green, blue, alpha);
    }
}
