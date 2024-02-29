package net.themcbrothers.usefulmachinery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.themcbrothers.usefulmachinery.block.AbstractMachineBlock;
import net.themcbrothers.usefulmachinery.block.entity.AbstractMachineBlockEntity;
import net.themcbrothers.usefulmachinery.machine.MachineTier;
import org.jetbrains.annotations.Nullable;

import static net.themcbrothers.usefulmachinery.UsefulMachinery.TEXT_UTILS;

public class TierUpgradeItem extends UpgradeItem {
    public TierUpgradeItem(Properties props, AbstractMachineBlock... machines) {
        super(props, machines);
    }

    @Override
    public InteractionResult useOn(@Nullable Player player, InteractionHand hand, ItemStack stack, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        BlockState state = level.getBlockState(pos);

        if (blockEntity instanceof AbstractMachineBlockEntity abstractMachineBlockEntity) {
            MachineTier machineTier = abstractMachineBlockEntity.getMachineTier(state);

            if (stack.getTag() != null && stack.getTag().contains("Tier", Tag.TAG_ANY_NUMERIC)) {
                MachineTier itemTier = MachineTier.byOrdinal(stack.getTag().getInt("Tier"));

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
            } else if (player != null) {
                player.displayClientMessage(TEXT_UTILS.translate("message", "upgrade.applied.fail")
                        .withStyle(ChatFormatting.RED), true);
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        CompoundTag tag = new CompoundTag();

        tag.putInt("Tier", 0);
        stack.setTag(tag);

        return stack;
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        if (stack.hasTag() && stack.getTag() != null) {
            if (stack.getTag().contains("Tier", Tag.TAG_INT)) {
                MachineTier tier = MachineTier.byOrdinal(stack.getTag().getInt("Tier"));

                return String.format("%s_%s", this.getDescriptionId(), tier.getSerializedName());
            }
        }

        return this.getDescriptionId();
    }

    @Override
    public boolean isSupported(BlockState machine) {
        return false;
    }
}
