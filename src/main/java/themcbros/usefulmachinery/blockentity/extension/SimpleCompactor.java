package themcbros.usefulmachinery.blockentity.extension;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import themcbros.usefulmachinery.machine.CompactorMode;

public class SimpleCompactor extends SimpleContainer implements Compactor {
    private final CompactorMode mode;

    public SimpleCompactor(CompactorMode mode, ItemStack... itemStack) {
        super(itemStack);
        this.mode = mode;
    }

    @Override
    public CompactorMode getMode() {
        return this.mode;
    }
}
