package net.themcbrothers.usefulmachinery.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.themcbrothers.lib.energy.EnergyContainerItem;

public class CreativePowerCellItem extends BlockItem implements EnergyContainerItem {
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


    @Override
    public int getCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxReceive() {
        return 0;
    }

    @Override
    public int getMaxExtract() {
        return Integer.MAX_VALUE;
    }
}
