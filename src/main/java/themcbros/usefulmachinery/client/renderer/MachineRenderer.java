package themcbros.usefulmachinery.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;
import themcbros.usefulmachinery.machine.MachineTier;

public class MachineRenderer implements BlockEntityRenderer<AbstractMachineBlockEntity> {
    public static final float OFFSET = 0.0001F;

    public MachineRenderer() {
    }

    private void add(VertexConsumer builder, PoseStack poseStack, float x, float y, float z, float u, float v, int color) {
        float[] colors = getRGBA(color);

        builder.vertex(poseStack.last().pose(), x, y, z)
                .color(colors[0], colors[1], colors[2], 1)
                .uv(u, v)
                .uv2(0, 240)
                .normal(1, 0, 0)
                .endVertex();
    }

    @Override
    public void render(AbstractMachineBlockEntity machine, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        MachineTier tier = machine.machineTier;

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                .apply(UsefulMachinery.getId("block/machine_side_tier_overlay"));
        VertexConsumer builder = buffer.getBuffer(RenderType.cutout());

        int color = tier.getColor();

        poseStack.pushPose();

        // South face
        add(builder, poseStack, -OFFSET, -OFFSET, 1f + OFFSET, sprite.getU0(), sprite.getV0(), color);
        add(builder, poseStack, 1f + OFFSET, -OFFSET, 1f + OFFSET, sprite.getU1(), sprite.getV0(), color);
        add(builder, poseStack, 1f + OFFSET, 1f + OFFSET, 1f + OFFSET, sprite.getU1(), sprite.getV1(), color);
        add(builder, poseStack, -OFFSET, 1f + OFFSET, 1f + OFFSET, sprite.getU0(), sprite.getV1(), color);

        // North face
        add(builder, poseStack, -OFFSET, 1f + OFFSET, -OFFSET, sprite.getU0(), sprite.getV1(), color);
        add(builder, poseStack, 1f + OFFSET, 1f + OFFSET, -OFFSET, sprite.getU1(), sprite.getV1(), color);
        add(builder, poseStack, 1f + OFFSET, -OFFSET, -OFFSET, sprite.getU1(), sprite.getV0(), color);
        add(builder, poseStack, -OFFSET, -OFFSET, -OFFSET, sprite.getU0(), sprite.getV0(), color);

        // East face
        add(builder, poseStack, 1 + OFFSET, 1f + OFFSET, -OFFSET, sprite.getU0(), sprite.getV0(), color);
        add(builder, poseStack, 1 + OFFSET, 1f + OFFSET, 1f + OFFSET, sprite.getU1(), sprite.getV0(), color);
        add(builder, poseStack, 1 + OFFSET, -OFFSET, 1f + OFFSET, sprite.getU1(), sprite.getV1(), color);
        add(builder, poseStack, 1 + OFFSET, -OFFSET, -OFFSET, sprite.getU0(), sprite.getV1(), color);

        // West face
        add(builder, poseStack, -OFFSET, -OFFSET, -OFFSET, sprite.getU0(), sprite.getV1(), color);
        add(builder, poseStack, -OFFSET, -OFFSET, 1f + OFFSET, sprite.getU1(), sprite.getV1(), color);
        add(builder, poseStack, -OFFSET, 1f + OFFSET, 1f + OFFSET, sprite.getU1(), sprite.getV0(), color);
        add(builder, poseStack, -OFFSET, 1f + OFFSET, -OFFSET, sprite.getU0(), sprite.getV0(), color);

        // Down face
        add(builder, poseStack, -OFFSET, -OFFSET, -OFFSET, sprite.getU0(), sprite.getV0(), color);
        add(builder, poseStack, 1f + OFFSET, -OFFSET, -OFFSET, sprite.getU1(), sprite.getV0(), color);
        add(builder, poseStack, 1f + OFFSET, -OFFSET, 1f + OFFSET, sprite.getU1(), sprite.getV1(), color);
        add(builder, poseStack, -OFFSET, -OFFSET, 1f + OFFSET, sprite.getU0(), sprite.getV1(), color);

        // Up face
        add(builder, poseStack, -OFFSET, 1f + OFFSET, 1f + OFFSET, sprite.getU0(), sprite.getV1(), color);
        add(builder, poseStack, 1f + OFFSET, 1f + OFFSET, 1f + OFFSET, sprite.getU1(), sprite.getV1(), color);
        add(builder, poseStack, 1f + OFFSET, 1f + OFFSET, -OFFSET, sprite.getU1(), sprite.getV0(), color);
        add(builder, poseStack, -OFFSET, 1f + OFFSET, -OFFSET, sprite.getU0(), sprite.getV0(), color);

        poseStack.popPose();
    }

    private static float[] getRGBA(int color) {
        float red = (color >> 16 & 0xFF) / 255F;
        float green = (color >> 8 & 0xFF) / 255F;
        float blue = (color & 0xFF) / 255F;
        float alpha = (color >> 24 & 0xFF) / 255F;

        return new float[]{red, green, blue, alpha};
    }
}
