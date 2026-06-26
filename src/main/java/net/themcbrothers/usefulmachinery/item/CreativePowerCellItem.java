package net.themcbrothers.usefulmachinery.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class CreativePowerCellItem extends BlockItem {
    public CreativePowerCellItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xFF0000;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return 13;
    }
}
