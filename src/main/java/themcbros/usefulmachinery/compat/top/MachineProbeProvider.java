package themcbros.usefulmachinery.compat.top;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.ITextStyle;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import themcbros.usefulmachinery.UsefulMachinery;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;

public class MachineProbeProvider implements IProbeInfoProvider {
    static final MachineProbeProvider INSTANCE = new MachineProbeProvider();

    @Override
    public ResourceLocation getID() {
        return UsefulMachinery.getId("machine_info");
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, BlockState blockState, IProbeHitData data) {
        if (level.getBlockEntity(data.getPos()) instanceof AbstractMachineBlockEntity machineBlockEntity) {
            int i = machineBlockEntity.getProcessTime();
            int j = machineBlockEntity.getProcessTimeTotal();
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

            if (machineBlockEntity.getMachineTier().ordinal() > 0) {
                ITextStyle style = probeInfo.defaultTextStyle();
                probeInfo.text("Tier: " + machineBlockEntity.getMachineTier().getSerializedName(), style);
            }
        }
    }
}
