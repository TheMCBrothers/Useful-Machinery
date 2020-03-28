package themcbros.usefulmachinery.compat.top;

import mcjty.theoneprobe.api.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.tileentity.FramedBundledCableTileEntity;
import themcbros.usefulmachinery.tileentity.MachineTileEntity;

public class CableProbeProvider implements IProbeInfoProvider {

    static final CableProbeProvider INSTANCE = new CableProbeProvider();

    @Override
    public String getID() {
        return UsefulMachinery.MOD_ID + ":machine_info";
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity playerEntity, World world, BlockState blockState, IProbeHitData data) {
        TileEntity tileEntity = world.getTileEntity(data.getPos());
        if (tileEntity instanceof FramedBundledCableTileEntity) {
            FramedBundledCableTileEntity cableTileEntity = (FramedBundledCableTileEntity) tileEntity;
            if (probeMode == ProbeMode.DEBUG) {
                probeInfo.text(cableTileEntity.getNetworkInfos());
            }
        }
    }
}
