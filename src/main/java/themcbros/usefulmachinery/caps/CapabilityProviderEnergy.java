package themcbros.usefulmachinery.caps;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

/**
 * @author TheMCLoveMan
 */
public class CapabilityProviderEnergy<HANDLER> implements ICapabilityProvider {

	protected final HANDLER instance;
	protected final Capability<HANDLER> capability;
	protected final Direction facing;
	
	public CapabilityProviderEnergy(final Capability<HANDLER> capability, @Nullable final Direction facing) {
		this(capability.getDefaultInstance(), capability, facing);
	}
	
	public CapabilityProviderEnergy(final HANDLER instance, final Capability<HANDLER> capability, @Nullable final Direction facing) {
		this.instance = instance;
		this.capability = capability;
		this.facing = facing;
	}
	
	private final LazyOptional<HANDLER> holder = LazyOptional.of(() -> getInstance());
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == getCapability()) return holder.cast();
		return LazyOptional.empty();
	}
	
	public final Capability<HANDLER> getCapability() {
		return capability;
	}
	
	public final HANDLER getInstance() {
		return instance;
	}
	
	@Nullable
	public Direction getFacing() {
		return facing;
	}

}
