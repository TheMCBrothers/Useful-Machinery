package net.themcbrothers.usefulmachinery.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;


public class UpgradeItem extends Item {
    public UpgradeItem(Properties props) {
        super(props);
    }

    @Override
    public final InteractionResult useOn(UseOnContext context) {
        return this.useOn(context.getPlayer(), context.getHand(), context.getItemInHand(), context.getLevel(), context.getClickedPos());
    }

    public InteractionResult useOn(@Nullable Player player, InteractionHand hand, ItemStack stack, Level level, BlockPos pos) {
        return InteractionResult.PASS;
    }
}
