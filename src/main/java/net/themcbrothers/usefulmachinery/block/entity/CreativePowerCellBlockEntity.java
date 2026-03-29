package net.themcbrothers.usefulmachinery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.themcbrothers.lib.energy.ExtendedEnergyStorage;
import net.themcbrothers.lib.util.EnergyUtils;
import net.themcbrothers.usefulmachinery.core.MachineryBlockEntities;

public class CreativePowerCellBlockEntity extends BlockEntity {
    private final SimpleEnergyHandler energyStorage;


    public CreativePowerCellBlockEntity(BlockPos pos, BlockState state) {
        super(MachineryBlockEntities.CREATIVE_POWER_CELL.get(), pos, state);

        this.energyStorage = new SimpleEnergyHandler(Integer.MAX_VALUE, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CreativePowerCellBlockEntity blockEntity) {
        if (!level.isClientSide()) {
            blockEntity.sendEnergy(level, pos);
        }
    }

    public SimpleEnergyHandler getEnergyStorage() {
        return this.energyStorage;
    }

    public int getEnergyStored() {
        return this.energyStorage.getAmountAsInt();
    }

    private void sendEnergy(Level level, BlockPos pos) {
        for (Direction facing : Direction.values()) {
            EnergyUtils.getEnergy(level, pos.relative(facing), facing.getOpposite())
                    .ifPresent((energy) -> {
                        try (Transaction transaction = Transaction.openRoot()) {
                            energy.insert(CreativePowerCellBlockEntity.this.getEnergyStored(), transaction);

                            transaction.commit();
                        }
                    });
        }
    }

}
