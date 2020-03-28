package themcbros.usefulmachinery.compat.computercraft;

import dan200.computercraft.api.lua.ArgumentHelper;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.machine.RedstoneMode;
import themcbros.usefulmachinery.tileentity.MachineTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MachinePeripheral implements IPeripheral {

    private static final String TYPE = UsefulMachinery.getId("machine").toString();

    private final MachineTileEntity machine;

    public MachinePeripheral(MachineTileEntity machine) {
        this.machine = machine;
    }

    @Nonnull
    @Override
    public String getType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public String[] getMethodNames() {
        return new String[] {
                "getEnergyStored",
                "getMaxEnergyStored",
                "getRedstoneMode",
                "setRedstoneMode"
        };
    }

    @Nullable
    @Override
    public Object[] callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext context, int method, @Nonnull Object[] arguments) throws LuaException {
        switch (method) {
            case 0:
                return new Object[]{machine.getEnergyStored()};
            case 1:
                return new Object[]{machine.getMaxEnergyStored()};
            case 2:
                return new Object[]{machine.redstoneMode.name()};
            case 3:
                String name = ArgumentHelper.getString(arguments, 0);
                RedstoneMode mode = RedstoneMode.byName(name);
                if (mode == null) {
                    throw new LuaException("Unknown redstone mode: " + name);
                }
                machine.redstoneMode = mode;
                machine.markDirty();
                return new Object[]{mode.name()};
            default:
                throw new IllegalStateException("Method index out of range: " + method);
        }
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        if (other instanceof MachinePeripheral)
            return machine.equals(((MachinePeripheral) other).machine);
        return false;
    }
}
