package net.themcbrothers.usefulmachinery.compat.top;

import mcjty.theoneprobe.api.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.themcbrothers.usefulmachinery.UsefulMachinery;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;

public enum MachineProbeProvider implements IProbeInfoProvider {
    INSTANCE;

    private static final ResourceLocation ID = UsefulMachinery.rl("machine_info");

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, BlockState state, IProbeHitData hitData) {
        if (level.getBlockEntity(hitData.getPos()) instanceof AbstractMachineBlockEntity machine) {
            // Progress bar
            int i = machine.getProcessTime();
            int j = machine.getProcessTimeTotal();

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

            // display tier on upgraded machines
            if (machine.getMachineTier(state).ordinal() > 0) {
                ITextStyle style = probeInfo.defaultTextStyle();

                probeInfo.text("Tier: " + machine.getMachineTier(state).getSerializedName(), style);
            }
        }
    }
}
