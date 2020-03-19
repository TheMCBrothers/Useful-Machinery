package themcbros.usefulmachinery.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import themcbros.usefulmachinery.tileentity.MachineTileEntity;

public class UpgradeItem extends Item {

    public UpgradeItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        final World world = context.getWorld();
        final BlockPos pos = context.getPos();
        final ItemStack stack = context.getItem();
        final TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof MachineTileEntity) {
            MachineTileEntity machineTileEntity = (MachineTileEntity) tileEntity;
        }
        return ActionResultType.PASS;
    }
}
