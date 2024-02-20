package net.themcbrothers.usefulmachinery.block.entity.extension;

import net.minecraft.world.Container;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;

public interface Compactor extends Container {
    CompactorMode getMode();

    void setMode(CompactorMode mode);
}
