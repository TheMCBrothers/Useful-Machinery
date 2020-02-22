package themcbros.usefulmachinery.compat.top;

import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.tileentity.MachineTileEntity;

public class MachineProbeProvider implements IProbeInfoProvider {

    static final MachineProbeProvider INSTANCE = new MachineProbeProvider();

    @Override
    public String getID() {
        return UsefulMachinery.MOD_ID + ":machine_info";
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity playerEntity, World world, BlockState blockState, IProbeHitData data) {
        TileEntity tileEntity = world.getTileEntity(data.getPos());
        if (tileEntity instanceof MachineTileEntity) {
            MachineTileEntity machineTileEntity = (MachineTileEntity) tileEntity;
            int i = machineTileEntity.processTime;
            int j = machineTileEntity.processTimeTotal;
            if (i != 0 && j != 0) {
                float progress = (float) i * 100 / (float) j;
                IElement progressElement = new ElementProgress((long) progress, 100, new ProgressStyle()
                        .prefix("Progress ")
                        .suffix("%")
                        .borderColor(0xff7A7A7A)
                        .filledColor(0xff808080));
                probeInfo.element(progressElement);
            }
        }
    }
}
