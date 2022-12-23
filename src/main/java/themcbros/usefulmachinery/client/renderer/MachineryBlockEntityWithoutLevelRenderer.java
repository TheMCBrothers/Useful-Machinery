package themcbros.usefulmachinery.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import themcbros.usefulmachinery.blocks.AbstractMachineBlock;

import java.util.Objects;

public class MachineryBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {
    public MachineryBlockEntityWithoutLevelRenderer() {
        super(null, null);
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager manager) {
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.@NotNull TransformType type, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (stack.getItem() instanceof BlockItem item) {
            Block block = item.getBlock();
            BlockState blockState = block.defaultBlockState();
            BlockEntity blockEntity;

            if (block instanceof AbstractMachineBlock machine) {
                blockEntity = machine.newBlockEntity(BlockPos.ZERO, blockState);

                Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockState, poseStack, buffer, combinedLight, combinedOverlay, ModelData.EMPTY, RenderType.cutout());
                Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(Objects.requireNonNull(blockEntity), poseStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }
}
