package themcbros.usefulmachinery.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import themcbros.usefulmachinery.blockentity.AbstractMachineBlockEntity;
import themcbros.usefulmachinery.machine.MachineTier;

import javax.annotation.Nonnull;

public class TierUpgradeItem extends UpgradeItem {
    public TierUpgradeItem(Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public InteractionResult useOn(@Nullable Player player, InteractionHand hand, ItemStack stack, Level level, BlockPos pos) {
        final BlockEntity blockEntity = level.getBlockEntity(pos);
        final BlockState blockState = level.getBlockState(pos);

        if (blockEntity instanceof AbstractMachineBlockEntity abstractMachineBlockEntity) {
            MachineTier machineTier = abstractMachineBlockEntity.getMachineTier();

            if (stack.getTag() != null && stack.getTag().contains("Tier", Tag.TAG_ANY_NUMERIC)) {
                MachineTier itemTier = MachineTier.byOrdinal(stack.getTag().getInt("Tier"));
                if (itemTier.ordinal() == machineTier.ordinal() + 1) {
                    abstractMachineBlockEntity.setMachineTier(itemTier);
                    level.sendBlockUpdated(pos, blockState, blockState, 4);
                    if (player != null) {
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        player.displayClientMessage(Component.literal("Successfully upgraded machine to " + itemTier.getSerializedName()).withStyle(ChatFormatting.GREEN), true);
                    }
                    return InteractionResult.SUCCESS;
                }
            } else if (player != null) {
                player.displayClientMessage(Component.literal("This is not a valid upgrade item").withStyle(ChatFormatting.RED), true);
            }
        }

        return InteractionResult.PASS;
    }

    @Nonnull
    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        CompoundTag tag = new CompoundTag();

        tag.putInt("Tier", 0);
        stack.setTag(tag);

        return stack;
    }

    @Nonnull
    @Override
    public String getDescriptionId(ItemStack stack) {
        if (!stack.isEmpty() && stack.hasTag() && stack.getTag() != null) {
            if (stack.getTag().contains("Tier", Tag.TAG_INT)) {
                MachineTier tier = MachineTier.byOrdinal(stack.getTag().getInt("Tier"));
                return this.getDescriptionId() + "_" + tier.getSerializedName();
            }
        }

        return this.getDescriptionId();
    }
}
