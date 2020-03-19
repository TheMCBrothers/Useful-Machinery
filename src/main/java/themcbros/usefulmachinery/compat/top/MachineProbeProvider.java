package themcbros.usefulmachinery.compat.top;

import mcjty.theoneprobe.api.*;
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
                IProgressStyle style = probeInfo.defaultProgressStyle()
                        .prefix("Progress ")
                        .suffix("%")
                        .backgroundColor(0x00000000)
                        .borderColor(0xff7A7A7A)
                        .filledColor(0xff808080);
                probeInfo.progress((long) progress, 100, style);
            }

            if (machineTileEntity.machineTier.ordinal() > 0) {
                ITextStyle style = probeInfo.defaultTextStyle();
                probeInfo.text("Tier: " + machineTileEntity.machineTier.getName(), style);
            }
        }
    }
}
