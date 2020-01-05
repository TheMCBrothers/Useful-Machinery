package themcbros.usefulmachinery.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import themcbros.usefulmachinery.container.MachineContainer;

import javax.annotation.Nullable;

public abstract class MachineFluidScreen<T extends MachineContainer> extends MachineScreen<T> {

    private static final int DEFAULT_TANK_WIDTH = 10;
    private static final int DEFAULT_TANK_HEIGHT = 50;

    private static final int TEX_WIDTH = 16;
    private static final int TEX_HEIGHT = 16;
    private static final int MIN_FLUID_HEIGHT = 1; // ensure tiny amounts of fluid are still visible

    private final int capacityMb;
    private final int width;
    private final int height;

    MachineFluidScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn, int capacityMb) {
        super(screenContainer, inv, titleIn);
        this.capacityMb = capacityMb;
        this.width = DEFAULT_TANK_WIDTH;
        this.height = DEFAULT_TANK_HEIGHT;
    }

    // Rendering methods

    protected void drawFluid(final int xPosition, final int yPosition, @Nullable FluidStack fluidStack) {
        if (fluidStack == null) {
            return;
        }
        Fluid fluid = fluidStack.getFluid();
        if (fluid == null) {
            return;
        }

        TextureAtlasSprite fluidStillSprite = getStillFluidSprite(fluidStack);

        FluidAttributes attributes = fluid.getAttributes();
        int fluidColor = attributes.getColor(fluidStack);

        int amount = fluidStack.getAmount();
        int scaledAmount = (amount * height) / capacityMb;
        if (amount > 0 && scaledAmount < MIN_FLUID_HEIGHT) {
            scaledAmount = MIN_FLUID_HEIGHT;
        }
        if (scaledAmount > height) {
            scaledAmount = height;
        }

        drawTiledSprite(xPosition, yPosition, width, height, fluidColor, scaledAmount, fluidStillSprite);
    }

    protected void drawTiledSprite(final int xPosition, final int yPosition, final int tiledWidth, final int tiledHeight, int color, int scaledAmount, TextureAtlasSprite sprite) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(PlayerContainer.field_226615_c_);
        setGLColorFromInt(color);

        final int xTileCount = tiledWidth / TEX_WIDTH;
        final int xRemainder = tiledWidth - (xTileCount * TEX_WIDTH);
        final int yTileCount = scaledAmount / TEX_HEIGHT;
        final int yRemainder = scaledAmount - (yTileCount * TEX_HEIGHT);

        final int yStart = yPosition + tiledHeight;

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int width = (xTile == xTileCount) ? xRemainder : TEX_WIDTH;
                int height = (yTile == yTileCount) ? yRemainder : TEX_HEIGHT;
                int x = xPosition + (xTile * TEX_WIDTH);
                int y = yStart - ((yTile + 1) * TEX_HEIGHT);
                if (width > 0 && height > 0) {
                    int maskTop = TEX_HEIGHT - height;
                    int maskRight = TEX_WIDTH - width;

                    drawTextureWithMasking(x, y, sprite, maskTop, maskRight, 100);
                }
            }
        }
    }

    private static TextureAtlasSprite getStillFluidSprite(FluidStack fluidStack) {
        Minecraft minecraft = Minecraft.getInstance();
        Fluid fluid = fluidStack.getFluid();
        FluidAttributes attributes = fluid.getAttributes();
        ResourceLocation fluidStill = attributes.getStillTexture(fluidStack);
        return minecraft.func_228015_a_(PlayerContainer.field_226615_c_).apply(fluidStill);
    }

    private static void setGLColorFromInt(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = ((color >> 24) & 0xFF) / 255F;

        RenderSystem.color4f(red, green, blue, alpha);
    }

    private static void drawTextureWithMasking(double xCoord, double yCoord, TextureAtlasSprite textureSprite, int maskTop, int maskRight, double zLevel) {
        double uMin = textureSprite.getMinU();
        double uMax = textureSprite.getMaxU();
        double vMin = textureSprite.getMinV();
        double vMax = textureSprite.getMaxV();
        uMax = uMax - (maskRight / 16.0 * (uMax - uMin));
        vMax = vMax - (maskTop / 16.0 * (vMax - vMin));

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.func_225582_a_(xCoord, yCoord + 16, zLevel).func_225583_a_((float) uMin, (float) vMax).endVertex();
        bufferBuilder.func_225582_a_(xCoord + 16 - maskRight, yCoord + 16, zLevel).func_225583_a_((float) uMax, (float) vMax).endVertex();
        bufferBuilder.func_225582_a_(xCoord + 16 - maskRight, yCoord + maskTop, zLevel).func_225583_a_((float) uMax, (float) vMin).endVertex();
        bufferBuilder.func_225582_a_(xCoord, yCoord + maskTop, zLevel).func_225583_a_((float) uMin, (float) vMin).endVertex();

        tessellator.draw();
    }

}
