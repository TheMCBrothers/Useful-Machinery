package themcbros.usefulmachinery.compat.computercraft;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import dan200.computercraft.api.redstone.IBundledRedstoneProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import themcbros.usefulmachinery.tileentity.FramedBundledCableTileEntity;
import themcbros.usefulmachinery.tileentity.MachineTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CCCompat implements IBundledRedstoneProvider, IPeripheralProvider {

    public CCCompat() {
    }

    @Override
    public int getBundledRedstoneOutput(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull Direction direction) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof FramedBundledCableTileEntity) {
            return ((FramedBundledCableTileEntity) tileEntity).getBundledOutput();
        }
        return -1;
    }

    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull Direction direction) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof MachineTileEntity) {
            return new MachinePeripheral((MachineTileEntity) tileEntity);
        }
        return null;
    }
}
