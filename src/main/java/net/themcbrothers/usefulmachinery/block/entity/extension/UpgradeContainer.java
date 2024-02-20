package net.themcbrothers.usefulmachinery.block.entity.extension;

import net.minecraft.world.SimpleContainer;

public class UpgradeContainer extends SimpleContainer {
    public UpgradeContainer(int size) {
        super(size);
    }

    @Override
    public int getMaxStackSize() {
        return 4;
    }
}
