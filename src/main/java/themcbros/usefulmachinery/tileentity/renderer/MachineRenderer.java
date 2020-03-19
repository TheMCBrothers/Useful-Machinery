package themcbros.usefulmachinery.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

import net.minecraft.item.ItemStack;
import themcbros.usefulmachinery.machine.MachineTier;
import themcbros.usefulmachinery.tileentity.MachineTileEntity;

public class MachineRenderer extends TileEntityRenderer<MachineTileEntity> {

    public MachineRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(MachineTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

        MachineTier machineTier = tileEntityIn.machineTier;


        RenderSystem.pushMatrix();

        setGLColorFromInt(machineTier.getColor());
        RenderSystem.translatef(0f, 1f, 0f);

        matrixStackIn.push();
        ItemStack stack = tileEntityIn.getStackInSlot(0);
        if (!stack.isEmpty())
            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);

        matrixStackIn.pop();
        RenderSystem.popMatrix();

    }

    private static void setGLColorFromInt(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = ((color >> 24) & 0xFF) / 255F;

        RenderSystem.color4f(red, green, blue, alpha);
    }

}
