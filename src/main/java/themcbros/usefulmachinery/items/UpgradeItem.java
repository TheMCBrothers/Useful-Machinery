package themcbros.usefulmachinery.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import themcbros.usefulmachinery.tileentity.MachineTileEntity;

import javax.annotation.Nonnull;

public class UpgradeItem extends Item {
    public UpgradeItem(Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        final Level level = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        final ItemStack stack = context.getItemInHand();
        final BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof MachineTileEntity machineTileEntity) {
            //Todo interaction upgrade per item logic
        }

        return InteractionResult.PASS;
    }
}
