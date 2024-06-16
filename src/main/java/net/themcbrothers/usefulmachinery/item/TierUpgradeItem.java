package net.themcbrothers.usefulmachinery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.themcbrothers.usefulmachinery.block.AbstractMachineBlock;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import net.themcbrothers.usefulmachinery.core.MachineryDataComponentTypes;
import net.themcbrothers.usefulmachinery.machine.MachineTier;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public class TierUpgradeItem extends UpgradeItem {
    public TierUpgradeItem(Properties props, AbstractMachineBlock... machines) {
        super(props, machines);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof AbstractMachineBlockEntity abstractMachineBlockEntity) {
            MachineTier machineTier = abstractMachineBlockEntity.getMachineTier(state);


            MachineTier itemTier = stack.getOrDefault(MachineryDataComponentTypes.TIER, MachineTier.SIMPLE);

            if (itemTier.ordinal() == machineTier.ordinal() + 1) {
                abstractMachineBlockEntity.setMachineTier(itemTier);

                level.sendBlockUpdated(pos, state, state, 4);

                if (player != null) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }

                    player.displayClientMessage(TEXT_UTILS.translate("message", "upgrade.applied.success")
                            .append(itemTier.getSerializedName())
                            .withStyle(ChatFormatting.GREEN), true);
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.set(MachineryDataComponentTypes.TIER, MachineTier.SIMPLE);

        return stack;
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        MachineTier tier = stack.getOrDefault(MachineryDataComponentTypes.TIER, MachineTier.SIMPLE);

        return String.format("%s_%s", this.getDescriptionId(), tier.getSerializedName());
    }

    @Override
    public boolean isSupported(BlockState machine) {
        return false;
    }
}
