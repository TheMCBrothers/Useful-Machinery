package themcbros.usefulmachinery.caps;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * @author TheMCLoveMan
 */
public class CapabilityProviderEnergy<HANDLER> implements ICapabilityProvider {
    protected final HANDLER instance;
    protected final Capability<HANDLER> capability;
    protected final Direction facing;

    public CapabilityProviderEnergy(final HANDLER instance, final Capability<HANDLER> capability, @Nullable final Direction facing) {
        this.instance = instance;
        this.capability = capability;
        this.facing = facing;
    }

    private final LazyOptional<HANDLER> holder = LazyOptional.of(this::getInstance);

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        return cap == getCapability() ? holder.cast() : LazyOptional.empty();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return ICapabilityProvider.super.getCapability(cap);
    }

    public final Capability<HANDLER> getCapability() {
        return capability;
    }

    public final @NotNull HANDLER getInstance() {
        return instance;
    }

    @Nullable
    public Direction getFacing() {
        return facing;
    }
}
