package net.themcbrothers.usefulmachinery.block.entity.extension;

import net.minecraft.world.item.crafting.RecipeInput;
import net.themcbrothers.usefulmachinery.machine.CompactorMode;

public interface Compactor extends RecipeInput {
    CompactorMode getMode();

    void setMode(CompactorMode mode);
}
