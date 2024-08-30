package net.themcbrothers.usefulmachinery.block.entity.extension;

import net.minecraft.world.item.ItemStack;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;

import javax.annotation.Nonnull;
import java.util.List;

public class SimpleCompactor implements Compactor {
    private final List<ItemStack> stacks;
    private CompactorMode mode;

    public SimpleCompactor(CompactorMode mode, ItemStack... stacks) {
        this.stacks = List.of(stacks);

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

    @Override
    @Nonnull
    public ItemStack getItem(int index) {
        return this.stacks.get(index);
    }

    @Override
    public int size() {
        return this.stacks.size();
    }

    @Override
    public boolean isEmpty() {
        return this.stacks.isEmpty();
    }
}
