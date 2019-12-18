package themcbros.usefulmachinery.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class EnergyUtils {

    @Nullable
    public static IEnergyStorage getEnergy(World world, BlockPos pos, @Nullable Direction side) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            return tileEntity.getCapability(CapabilityEnergy.ENERGY, side).orElse(null);
        }
        return null;
    }

}
