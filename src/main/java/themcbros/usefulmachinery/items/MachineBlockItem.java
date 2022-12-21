package themcbros.usefulmachinery.items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;
import themcbros.usefulmachinery.client.renderer.MachineryBlockEntityWithoutLevelRenderer;

import java.util.function.Consumer;

public class MachineBlockItem extends BlockItem {
    public MachineBlockItem(Block block, Properties props) {
        super(block, props);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return new MachineryBlockEntityWithoutLevelRenderer();
            }
        });
    }
}
