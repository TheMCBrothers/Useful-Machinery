package net.themcbrothers.usefulmachinery.block.entity.extension;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;

public class SimpleCompactor extends SimpleContainer implements Compactor {
    private CompactorMode mode;

    public SimpleCompactor(CompactorMode mode, ItemStack... stacks) {
        super(stacks);

        this.mode = mode;
    }

    @Override
    public CompactorMode getMode() {
        return this.mode;
    }

    @Override
    public void setMode(CompactorMode mode) {
        this.mode = mode;
    }
}
