package themcbros.usefulmachinery.recipes;

import com.google.common.base.Preconditions;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public class LavaGeneratingRecipe implements IJeiFuelingRecipe {
    private final List<ItemStack> inputs;
    private final int burnTime;

    public LavaGeneratingRecipe(List<ItemStack> inputs, int burnTime) {
        Preconditions.checkArgument(burnTime == 20000);
        this.inputs = inputs;
        this.burnTime = burnTime;
    }

    @Override
    public @Unmodifiable List<ItemStack> getInputs() {
        return inputs;
    }

    @Override
    public int getBurnTime() {
        return burnTime;
    }
}
