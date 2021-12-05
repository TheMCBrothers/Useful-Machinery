package themcbros.usefulmachinery.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class EnergyUtils {
    @Nullable
    public static IEnergyStorage getEnergy(Level level, BlockPos pos, @Nullable Direction side) {
        BlockEntity tileEntity = level.getBlockEntity(pos);

        if (tileEntity != null) {
            return tileEntity.getCapability(CapabilityEnergy.ENERGY, side).orElse(null);
        }

        return null;
    }
}
